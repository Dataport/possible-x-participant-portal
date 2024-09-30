package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.CreateOfferResponseTO;
import eu.possiblex.participantportal.application.entity.ParticipantIdTO;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.business.entity.CreateDataOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.edc.asset.AssetCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.common.IdResponse;
import eu.possiblex.participantportal.business.entity.edc.contractdefinition.ContractDefinitionCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.policy.PolicyCreateRequest;
import eu.possiblex.participantportal.business.entity.exception.EdcOfferCreationException;
import eu.possiblex.participantportal.business.entity.exception.FhOfferCreationException;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.utilities.PossibleXException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service implementation for managing provider-related operations.
 */
@Service
@Slf4j
public class ProviderServiceImpl implements ProviderService {

    private final EdcClient edcClient;

    private final FhCatalogClient fhCatalogClient;

    private final ProviderServiceMapper providerServiceMapper;

    @Value("${edc.protocol-base-url}")
    private String edcProtocolUrl;

    @Value("${participant-id}")
    private String participantId;

    /**
     * Constructor for ProviderServiceImpl.
     *
     * @param edcClient the EDC client
     * @param fhCatalogClient the FH catalog client
     */
    @Autowired
    public ProviderServiceImpl(EdcClient edcClient, FhCatalogClient fhCatalogClient,
        ProviderServiceMapper providerServiceMapper) {

        this.edcClient = edcClient;
        this.fhCatalogClient = fhCatalogClient;
        this.providerServiceMapper = providerServiceMapper;
    }

    /**
     * Creates an offering by interacting with both FH catalog and EDC.
     *
     * @param request the service offering creation request
     * @return the response transfer object containing offer IDs
     */
    @Override
    public CreateOfferResponseTO createOffering(CreateServiceOfferingRequestBE request) {

        PxExtendedServiceOfferingCredentialSubject pxExtendedServiceOfferingCs = createCombinedCsFromRequest(request);
        CreateEdcOfferBE createEdcOfferBE = createEdcBEFromRequest(request);

        try {
            FhCatalogIdResponse fhResponseId = createFhCatalogOffer(pxExtendedServiceOfferingCs);
            IdResponse edcResponseId = createEdcOffer(pxExtendedServiceOfferingCs.getAssetId(), createEdcOfferBE);
            return new CreateOfferResponseTO(edcResponseId.getId(), fhResponseId.getId());
        } catch (EdcOfferCreationException e) {
            throw new PossibleXException("Failed to create offer. EdcOfferCreationException: " + e,
                HttpStatus.BAD_REQUEST);
        } catch (FhOfferCreationException e) {
            throw new PossibleXException("Failed to create offer. FhOfferCreationException: " + e,
                HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new PossibleXException("Failed to create offer. Other Exception: " + e);
        }
    }

    /**
     * Return the participant's id.
     *
     * @return participant id
     */
    @Override
    public ParticipantIdTO getParticipantId() {

        return new ParticipantIdTO(participantId);
    }

    /**
     * Creates an EDC offer by building and sending the necessary requests.
     *
     * @param assetId the asset ID
     * @param createEdcOfferBE the EDC offer business entity
     * @return the ID response from EDC
     * @throws EdcOfferCreationException if EDC offer creation fails
     */
    private IdResponse createEdcOffer(String assetId, CreateEdcOfferBE createEdcOfferBE)
        throws EdcOfferCreationException {

        ProviderRequestBuilder requestBuilder = new ProviderRequestBuilder(assetId, createEdcOfferBE);

        try {
            AssetCreateRequest assetCreateRequest = requestBuilder.buildAssetRequest();
            log.info("Creating Asset {}", assetCreateRequest);
            IdResponse assetIdResponse = edcClient.createAsset(assetCreateRequest);

            PolicyCreateRequest policyCreateRequest = requestBuilder.buildPolicyRequest();
            log.info("Creating Policy {}", policyCreateRequest);
            IdResponse policyIdResponse = edcClient.createPolicy(policyCreateRequest);

            ContractDefinitionCreateRequest contractDefinitionCreateRequest = requestBuilder.buildContractDefinitionRequest(
                policyIdResponse, assetIdResponse);
            log.info("Creating Contract Definition {}", contractDefinitionCreateRequest);

            return edcClient.createContractDefinition(contractDefinitionCreateRequest);
        } catch (Exception e) {
            throw new EdcOfferCreationException("An error occurred during Edc offer creation: " + e.getMessage());
        }
    }

    /**
     * Creates an FH catalog offer by sending the service offering creation requests.
     *
     * @param serviceOfferingCredentialSubject the service offering credential subject
     * @return the ID response from FH catalog
     * @throws FhOfferCreationException if FH offer creation fails
     */
    private FhCatalogIdResponse createFhCatalogOffer(
        PxExtendedServiceOfferingCredentialSubject serviceOfferingCredentialSubject) throws FhOfferCreationException {

        try {
            log.info("Adding Service Offering to Fraunhofer Catalog {}", serviceOfferingCredentialSubject);

            return fhCatalogClient.addServiceOfferingToFhCatalog(serviceOfferingCredentialSubject);
        } catch (Exception e) {
            throw new FhOfferCreationException("An error occurred during Fh offer creation: " + e.getMessage());
        }
    }

    /**
     * Creates a combined credential subject for the offering from the request.
     *
     * @param request the offering creation request
     * @return the combined credential subject
     */
    private PxExtendedServiceOfferingCredentialSubject createCombinedCsFromRequest(
        CreateServiceOfferingRequestBE request) {

        String assetId = UUID.randomUUID().toString();
        String serviceOfferingId = UUID.randomUUID().toString();
        String dataResourceId = UUID.randomUUID().toString();

        if (request instanceof CreateDataOfferingRequestBE dataOfferingRequest) { // data offering
            dataOfferingRequest.getDataResource().setId(dataResourceId);
            dataOfferingRequest.getDataResource().setExposedThrough(new NodeKindIRITypeId(serviceOfferingId));

            return providerServiceMapper.getPxExtendedServiceOfferingCredentialSubject(dataOfferingRequest,
                serviceOfferingId, assetId, edcProtocolUrl);
        } else { // base service offering
            return providerServiceMapper.getPxExtendedServiceOfferingCredentialSubject(request, serviceOfferingId,
                assetId, edcProtocolUrl);
        }

    }

    /**
     * Creates the payload for the EDC offer from the request.
     *
     * @param request the offering creation request
     * @return the EDC offer business entity
     */
    private CreateEdcOfferBE createEdcBEFromRequest(CreateServiceOfferingRequestBE request) {

        if (request instanceof CreateDataOfferingRequestBE dataOfferingRequest) { // data offering
            return providerServiceMapper.getCreateEdcOfferBE(dataOfferingRequest);
        } else { // base service offering
            return providerServiceMapper.getCreateEdcOfferBE(request);
        }
    }

}
