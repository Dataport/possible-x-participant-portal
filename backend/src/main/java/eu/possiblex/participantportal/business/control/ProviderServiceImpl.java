package eu.possiblex.participantportal.business.control;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.application.entity.CreateOfferResponseTO;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.business.entity.CreateDataOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.DataProductPrefillFieldsBE;
import eu.possiblex.participantportal.business.entity.PrefillFieldsBE;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.edc.asset.AssetCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.common.IdResponse;
import eu.possiblex.participantportal.business.entity.edc.contractdefinition.ContractDefinitionCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.edc.policy.PolicyCreateRequest;
import eu.possiblex.participantportal.business.entity.exception.EdcOfferCreationException;
import eu.possiblex.participantportal.business.entity.exception.FhOfferCreationException;
import eu.possiblex.participantportal.business.entity.exception.OfferingComplianceException;
import eu.possiblex.participantportal.business.entity.exception.PrefillFieldsProcessingException;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.File;
import java.io.IOException;
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

    private final EnforcementPolicyParserService enforcementPolicyParserService;

    private final String bucketStorageRegion;

    private final String bucketName;

    private final String edcProtocolUrl;

    private final String participantId;

    private final ObjectMapper objectMapper;

    private final PrefillFieldsBE prefillFields;

    /**
     * Constructor for ProviderServiceImpl.
     *
     * @param edcClient the EDC client
     * @param fhCatalogClient the FH catalog client
     */
    @Autowired
    public ProviderServiceImpl(@Autowired EdcClient edcClient, @Autowired FhCatalogClient fhCatalogClient,
        @Autowired ProviderServiceMapper providerServiceMapper,
        @Autowired EnforcementPolicyParserService enforcementPolicyParserService,
        @Value("${edc.protocol-base-url}") String edcProtocolUrl, @Value("${participant-id}") String participantId,
        @Value("${s3.bucket-storage-region}") String bucketStorageRegion, @Value("${s3.bucket-name}") String bucketName,
        @Value("${prefill-fields.data-product.json-file-path}") String prefillFieldsDataProductJsonFilePath,
        @Autowired ObjectMapper objectMapper) {

        this.edcClient = edcClient;
        this.fhCatalogClient = fhCatalogClient;
        this.providerServiceMapper = providerServiceMapper;
        this.enforcementPolicyParserService = enforcementPolicyParserService;
        this.edcProtocolUrl = edcProtocolUrl;
        this.bucketStorageRegion = bucketStorageRegion;
        this.bucketName = bucketName;
        this.participantId = participantId;
        this.objectMapper = objectMapper;
        this.prefillFields = getPrefillFields(participantId, prefillFieldsDataProductJsonFilePath);
    }

    /**
     * Creates an offering by interacting with both FH catalog and EDC.
     *
     * @param request the service offering creation request
     * @return the response transfer object containing offer IDs
     */
    @Override
    public CreateOfferResponseTO createOffering(CreateServiceOfferingRequestBE request) {

        Policy policy = enforcementPolicyParserService.getEdcPolicyFromEnforcementPolicies(
            request.getEnforcementPolicies());

        PxExtendedServiceOfferingCredentialSubject pxExtendedServiceOfferingCs = createCombinedCsFromRequest(request,
            policy);
        CreateEdcOfferBE createEdcOfferBE = createEdcBEFromRequest(request, pxExtendedServiceOfferingCs.getId(),
            pxExtendedServiceOfferingCs.getAssetId(), policy);
        boolean isOfferWithData = isServiceOfferWithData(pxExtendedServiceOfferingCs);

        FhCatalogIdResponse fhResponseId;
        fhResponseId = createFhCatalogOffer(pxExtendedServiceOfferingCs);

        IdResponse edcResponseId;
        try {
            edcResponseId = createEdcOffer(createEdcOfferBE);
        } catch (EdcOfferCreationException e) {
            // rollback catalog offering creation
            fhCatalogClient.deleteServiceOfferingFromFhCatalog(fhResponseId.getId(), isOfferWithData);
            throw e;
        }
        return new CreateOfferResponseTO(edcResponseId.getId(), fhResponseId.getId());
    }

    /**
     * Return the prefill fields.
     *
     * @return prefill fields
     */
    @Override
    public PrefillFieldsBE getPrefillFields() {

        return this.prefillFields;

    }

    private PrefillFieldsBE getPrefillFields(String participantId, String filePath) {

        return new PrefillFieldsBE(participantId, readDataProductPrefillFieldsFromFile(filePath));
    }

    private DataProductPrefillFieldsBE readDataProductPrefillFieldsFromFile(String filePath) {

        Resource resource = getDataResourcePrefillFieldsResource(filePath);

        DataProductPrefillFieldsBE dataProductPrefillFields;

        try {
            dataProductPrefillFields = objectMapper.readValue(resource.getInputStream(),
                DataProductPrefillFieldsBE.class);
        } catch (IOException e) {
            throw new PrefillFieldsProcessingException(
                "Failed to process data product prefill fields from file: " + e.getMessage());
        }

        return dataProductPrefillFields;
    }

    /**
     * Get the resource for data product prefill fields from given path.
     *
     * @return the resource
     */
    private Resource getDataResourcePrefillFieldsResource(String filePath) {

        Resource resource;
        if (filePath == null || filePath.isEmpty()) {
            resource = new ClassPathResource("prefillFieldsDataProduct.json");
        } else {
            File file = new File(filePath);
            if (file.exists()) {
                resource = new FileSystemResource(file);
            } else {
                resource = new ClassPathResource("prefillFieldsDataProduct.json");
            }
        }
        return resource;
    }

    /**
     * Creates an EDC offer by building and sending the necessary requests.
     *
     * @param createEdcOfferBE the EDC offer business entity
     * @return the ID response from EDC
     */
    private IdResponse createEdcOffer(CreateEdcOfferBE createEdcOfferBE) {

        ProviderRequestBuilder requestBuilder = new ProviderRequestBuilder(createEdcOfferBE);

        try {
            AssetCreateRequest assetCreateRequest = requestBuilder.buildAssetRequest(bucketName, bucketStorageRegion);
            log.info("Creating Asset {}", assetCreateRequest);
            IdResponse assetIdResponse = edcClient.createAsset(assetCreateRequest);

            PolicyCreateRequest accessPolicyCreateRequest = requestBuilder.buildPolicyRequest(
                enforcementPolicyParserService.getEverythingAllowedPolicy());
            log.info("Creating access Policy {}", accessPolicyCreateRequest);
            IdResponse accessPolicyIdResponse = edcClient.createPolicy(accessPolicyCreateRequest);

            PolicyCreateRequest contractPolicyCreateRequest = requestBuilder.buildPolicyRequest();
            log.info("Creating contract Policy {}", contractPolicyCreateRequest);
            IdResponse contractPolicyIdResponse = edcClient.createPolicy(contractPolicyCreateRequest);

            ContractDefinitionCreateRequest contractDefinitionCreateRequest = requestBuilder.buildContractDefinitionRequest(
                accessPolicyIdResponse, contractPolicyIdResponse, assetIdResponse);
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
     */
    private FhCatalogIdResponse createFhCatalogOffer(
        PxExtendedServiceOfferingCredentialSubject serviceOfferingCredentialSubject) {

        try {
            boolean isOfferWithData = isServiceOfferWithData(serviceOfferingCredentialSubject);
            log.info("Adding Service Offering to Fraunhofer Catalog {}, with data: {}",
                serviceOfferingCredentialSubject, isOfferWithData);

            return fhCatalogClient.addServiceOfferingToFhCatalog(serviceOfferingCredentialSubject, isOfferWithData);
        } catch (WebClientResponseException e) {
            throw buildComplianceException(e);
        } catch (Exception e) {
            throw new FhOfferCreationException("An error occurred during Fh offer creation: " + e.getMessage());
        }
    }

    /**
     * Check if the service offer payload contains data. Currently, the check is just if gx:aggregationOf is empty.
     *
     * @param serviceOfferPayload the service offer payload
     * @return true: The service offer contains data. false: otherwise
     */
    private boolean isServiceOfferWithData(PxExtendedServiceOfferingCredentialSubject serviceOfferPayload) {

        boolean serviceOfferContainsData = !serviceOfferPayload.getAggregationOf().isEmpty();

        return serviceOfferContainsData;
    }

    private OfferingComplianceException buildComplianceException(WebClientResponseException e) {

        JsonNode error = e.getResponseBodyAs(JsonNode.class);
        if (error != null && error.get("error") != null) {
            return new OfferingComplianceException(error.get("error").textValue(), e);
        }
        return new OfferingComplianceException("Unknown catalog processing exception", e);
    }

    /**
     * Creates a combined credential subject for the offering from the request.
     *
     * @param request the offering creation request
     * @param policy policy for the offering
     * @return the combined credential subject
     */
    private PxExtendedServiceOfferingCredentialSubject createCombinedCsFromRequest(
        CreateServiceOfferingRequestBE request, Policy policy) {

        String assetId = UUID.randomUUID().toString();
        String serviceOfferingId = "urn:uuid:" + UUID.randomUUID();
        String dataResourceId = "urn:uuid:" + UUID.randomUUID();
        request.setProvidedBy(new NodeKindIRITypeId(participantId));

        if (request instanceof CreateDataOfferingRequestBE dataOfferingRequest) { // data offering
            dataOfferingRequest.getDataResource().setId(dataResourceId);
            dataOfferingRequest.getDataResource().setExposedThrough(new NodeKindIRITypeId(serviceOfferingId));
            return providerServiceMapper.getPxExtendedServiceOfferingCredentialSubject(dataOfferingRequest,
                serviceOfferingId, assetId, edcProtocolUrl, policy);
        } else { // base service offering
            return providerServiceMapper.getPxExtendedServiceOfferingCredentialSubject(request, serviceOfferingId,
                assetId, edcProtocolUrl, policy);
        }

    }

    /**
     * Creates the payload for the EDC offer from the request.
     *
     * @param request the offering creation request
     * @param offerId the id of the created offer
     * @param assetId the id of the created asset
     * @param policy the policy for the EDC
     * @return the EDC offer business entity
     */
    private CreateEdcOfferBE createEdcBEFromRequest(CreateServiceOfferingRequestBE request, String offerId,
        String assetId, Policy policy) {

        CreateEdcOfferBE createEdcOfferBE = null;
        if (request instanceof CreateDataOfferingRequestBE dataOfferingRequest) { // data offering
            createEdcOfferBE = providerServiceMapper.getCreateEdcOfferBE(dataOfferingRequest, offerId, assetId, policy);
            createEdcOfferBE.getProperties()
                .setOfferingPolicy(providerServiceMapper.combineSOPolicyAndPolicy(dataOfferingRequest, policy));

        } else { // base service offering
            createEdcOfferBE = providerServiceMapper.getCreateEdcOfferBE(request, offerId, assetId, policy);
            createEdcOfferBE.getProperties()
                .setOfferingPolicy(providerServiceMapper.combineSOPolicyAndPolicy(request, policy));
        }
        return createEdcOfferBE;
    }

}
