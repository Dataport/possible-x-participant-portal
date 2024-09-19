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
import eu.possiblex.participantportal.business.entity.fh.CreateFhOfferBE;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.business.entity.fh.catalog.DcatDataset;
import eu.possiblex.participantportal.utilities.PossibleXException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
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

    /**
     * Constructor for ProviderServiceImpl.
     *
     * @param edcClient the EDC client
     * @param fhCatalogClient the FH catalog client
     */
    @Autowired
    public ProviderServiceImpl(EdcClient edcClient, FhCatalogClient fhCatalogClient) {
        this.edcClient = edcClient;
        this.fhCatalogClient = fhCatalogClient;
    }

    /**
     * Creates an offer by interacting with both FH catalog and EDC.
     *
     * @param createFhOfferBE the FH offer business entity
     * @param createEdcOfferBE the EDC offer business entity
     * @return the response transfer object containing offer IDs
     * @throws FhOfferCreationException if FH offer creation fails
     * @throws EdcOfferCreationException if EDC offer creation fails
     */
    @Override
    public CreateOfferResponseTO createOffer(CreateFhOfferBE createFhOfferBE, CreateEdcOfferBE createEdcOfferBE) {


        String assetId = generateAssetId();
        ProviderRequestBuilder requestBuilder = new ProviderRequestBuilder(assetId, createFhOfferBE, createEdcOfferBE,
            edcProtocolUrl);

        try {
            FhCatalogIdResponse fhResponseId = createFhCatalogOffer(requestBuilder);
            IdResponse edcResponseId = createEdcOffer(requestBuilder);
            return new CreateOfferResponseTO(edcResponseId.getId(), fhResponseId.getId());
        } catch (EdcOfferCreationException e) {
            throw new PossibleXException("Failed to create offer. EdcOfferCreationException: " + e, HttpStatus.BAD_REQUEST);
        } catch (FhOfferCreationException e) {
            throw new PossibleXException("Failed to create offer. FhOfferCreationException: " + e, HttpStatus.BAD_REQUEST);
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
     * @param requestBuilder the request builder
     * @return the ID response from EDC
     * @throws EdcOfferCreationException if EDC offer creation fails
     */
    private IdResponse createEdcOffer(ProviderRequestBuilder requestBuilder) throws EdcOfferCreationException {

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
     * Creates an FH catalog offer by building and sending the necessary requests.
     *
     * @param requestBuilder the request builder
     * @return the ID response from FH catalog
     * @throws FhOfferCreationException if FH offer creation fails
     */
    private FhCatalogIdResponse createFhCatalogOffer(ProviderRequestBuilder requestBuilder) throws FhOfferCreationException {
        try {
            DcatDataset dcatDataset = requestBuilder.buildFhCatalogOfferRequest();
            log.info("Adding Dataset to Fraunhofer Catalog {}", dcatDataset);

            return fhCatalogClient.addDatasetToFhCatalog(dcatDataset);
        } catch (Exception e) {
            throw new FhOfferCreationException("An error occurred during Fh offer creation: " + e.getMessage());
        }
    }

}
