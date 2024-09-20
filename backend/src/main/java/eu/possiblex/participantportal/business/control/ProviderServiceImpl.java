package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.CreateOfferResponseTO;
import eu.possiblex.participantportal.application.entity.ParticipantIdTO;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.edc.asset.AssetCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.common.IdResponse;
import eu.possiblex.participantportal.business.entity.edc.contractdefinition.ContractDefinitionCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.policy.PolicyCreateRequest;
import eu.possiblex.participantportal.business.entity.exception.EdcOfferCreationException;
import eu.possiblex.participantportal.business.entity.exception.FhOfferCreationException;
import eu.possiblex.participantportal.business.entity.fh.CreateFhDataOfferingBE;
import eu.possiblex.participantportal.business.entity.fh.CreateFhServiceOfferingBE;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.resources.GxDataResourceCredentialSubject;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.selfdescriptions.px.PxExtendedServiceOfferingCredentialSubject;
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

    @Value("${fh.catalog.secret-key}")
    private String fhCatalogSecretKey;

    @Value("${edc.protocol-base-url}")
    private String edcProtocolUrl;

    @Value("${participant-id}")
    private String participantId;

    private ProviderServiceMapper providerServiceMapper;

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
    }

    /**
     * Creates a service offering by interacting with both FH catalog and EDC.
     *
     * @param createFhServiceOfferingBE the FH offer business entity
     * @param createEdcOfferBE the EDC offer business entity
     * @return the response transfer object containing offer IDs
     */
    @Override
    public CreateOfferResponseTO createServiceOffering(CreateFhServiceOfferingBE createFhServiceOfferingBE,
        CreateEdcOfferBE createEdcOfferBE) {

        String assetId = generateAssetId();
        String serviceOfferingId = UUID.randomUUID().toString();

        PxExtendedServiceOfferingCredentialSubject pxExtendedServiceOfferingCs = getFhCatalogRequest(assetId,
            createFhServiceOfferingBE, serviceOfferingId);

        return createFhCatalogOfferAndEdcOffer(createEdcOfferBE, assetId, pxExtendedServiceOfferingCs);
    }

    /**
     * Creates a data offering by interacting with both FH catalog and EDC.
     *
     * @param createFhDataOfferingBE the FH offer business entity
     * @param createEdcOfferBE the EDC offer business entity
     * @return the response transfer object containing offer IDs
     */
    @Override
    public CreateOfferResponseTO createDataOffering(CreateFhDataOfferingBE createFhDataOfferingBE,
        CreateEdcOfferBE createEdcOfferBE) {

        String assetId = generateAssetId();
        String serviceOfferingId = UUID.randomUUID().toString();

        PxExtendedServiceOfferingCredentialSubject pxExtendedServiceOfferingCs = getFhCatalogRequest(assetId,
            createFhDataOfferingBE, serviceOfferingId);
        return createFhCatalogOfferAndEdcOffer(createEdcOfferBE, assetId, pxExtendedServiceOfferingCs);
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
     * Generates a unique asset ID.
     *
     * @return the generated asset ID
     */
    private String generateAssetId() {

        return "assetId_" + UUID.randomUUID();
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

    private PxExtendedServiceOfferingCredentialSubject getFhCatalogRequest(String assetId,
        CreateFhServiceOfferingBE createFhServiceOfferingBE, String serviceOfferingId) {

        GxServiceOfferingCredentialSubject serviceOfferingCredentialSubject = createFhServiceOfferingBE.getServiceOfferingCredentialSubject();
        serviceOfferingCredentialSubject.setId(serviceOfferingId);

        return providerServiceMapper.getExtendedServiceOfferingCredentialSubject(serviceOfferingCredentialSubject,
            assetId, edcProtocolUrl);

    }

    private PxExtendedServiceOfferingCredentialSubject getFhCatalogRequest(String assetId,
        CreateFhDataOfferingBE createFhDataOfferingBE, String serviceOfferingId) {

        String datasetId = UUID.randomUUID().toString();

        GxServiceOfferingCredentialSubject serviceOfferingCredentialSubject = createFhDataOfferingBE.getServiceOfferingCredentialSubject();
        serviceOfferingCredentialSubject.setId(serviceOfferingId);

        GxDataResourceCredentialSubject dataResourceCredentialSubject = createFhDataOfferingBE.getDataResourceCredentialSubject();
        dataResourceCredentialSubject.setId(datasetId);
        dataResourceCredentialSubject.setExposedThrough(new NodeKindIRITypeId(serviceOfferingId));

        return providerServiceMapper.getExtendedServiceOfferingCredentialSubject(serviceOfferingCredentialSubject,
            dataResourceCredentialSubject, assetId, edcProtocolUrl);

    }

    private CreateOfferResponseTO createFhCatalogOfferAndEdcOffer(CreateEdcOfferBE createEdcOfferBE, String assetId,
        PxExtendedServiceOfferingCredentialSubject pxExtendedServiceOfferingCs) {

        try {
            FhCatalogIdResponse fhResponseId = createFhCatalogOffer(pxExtendedServiceOfferingCs);
            IdResponse edcResponseId = createEdcOffer(assetId, createEdcOfferBE);
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

}
