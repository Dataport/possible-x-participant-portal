package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.CreateOfferResponseTO;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.edc.asset.AssetCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.asset.AssetProperties;
import eu.possiblex.participantportal.business.entity.edc.asset.DataAddress;
import eu.possiblex.participantportal.business.entity.edc.asset.ionoss3extension.IonosS3DataSource;
import eu.possiblex.participantportal.business.entity.edc.common.IdResponse;
import eu.possiblex.participantportal.business.entity.edc.contractdefinition.ContractDefinitionCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.contractdefinition.Criterion;
import eu.possiblex.participantportal.business.entity.edc.policy.PolicyCreateRequest;
import eu.possiblex.participantportal.business.entity.exception.*;
import eu.possiblex.participantportal.business.entity.fh.CreateFhOfferBE;
import eu.possiblex.participantportal.business.entity.fh.FhIdResponse;
import eu.possiblex.participantportal.business.entity.fh.catalog.DcatDataset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class ProviderServiceImpl implements ProviderService {

    private final EdcClient edcClient;

    private final FhCatalogClient fhCatalogClient;

    @Value("${fh.catalog.secret-key}")
    private String fhCatalogSecretKey;

    @Value("${fh.catalog.catalog-name}")
    private String catalogName;

    public ProviderServiceImpl(@Autowired EdcClient edcClient, @Autowired FhCatalogClient fhCatalogClient) {

        this.edcClient = edcClient;
        this.fhCatalogClient = fhCatalogClient;
    }

    /**
     * Given a request for creating a dataset entry in the Fraunhofer catalog and a request for creating an EDC offer,
     * create the dataset entry and the offer in the EDC catalog.
     *
     * @param createFhOfferBE request for creating a dataset entry
     * @param createEdcOfferBE request for creating an EDC offer
     * @return create offer response object
     */
    @Override
    public CreateOfferResponseTO createOffer(CreateFhOfferBE createFhOfferBE, CreateEdcOfferBE createEdcOfferBE)
        throws AssetCreationFailedException, AssetConflictException, PolicyCreationFailedException,
        PolicyConflictException, ContractDefinitionConflictException, ContractDefinitionCreationException {

        String assetId = "assetId_" + UUID.randomUUID();
        CreateOfferResponseTO createOfferResponseTO = new CreateOfferResponseTO();
        var fhResponseId = createFhCatalogOffer(createFhOfferBE, assetId);
        var edcResponseId = createEdcOffer(createEdcOfferBE, assetId);
        createOfferResponseTO.setEdcResponseId(edcResponseId.getId());
        createOfferResponseTO.setFhResponseId(fhResponseId.getId());

        return createOfferResponseTO;

    }

    private IdResponse createEdcOffer(CreateEdcOfferBE createEdcOfferBE, String assetId)
        throws AssetCreationFailedException, AssetConflictException, PolicyConflictException,
        PolicyCreationFailedException, ContractDefinitionConflictException, ContractDefinitionCreationException {
        // create asset
        DataAddress dataAddress = IonosS3DataSource.builder().bucketName("dev-provider-edc-bucket-possible-31952746")
            .blobName(createEdcOfferBE.getFileName()).keyName(createEdcOfferBE.getFileName())
            .storage("s3-eu-central-2.ionoscloud.com").build();
        AssetCreateRequest assetCreateRequest = AssetCreateRequest.builder().id(assetId).properties(
            AssetProperties.builder().name(createEdcOfferBE.getAssetName())
                .description(createEdcOfferBE.getAssetDescription()).
                //version("assetVersion").
                    contenttype("application/json").build()).dataAddress(dataAddress).build();

        IdResponse assetIdResponse = null;
        try {
            log.info("Creating Asset {}", assetCreateRequest);
            assetIdResponse = edcClient.createAsset(assetCreateRequest);
        } catch (WebClientResponseException.Conflict e) {
            log.error("Conflict occurred: {}", e.getMessage(), e);
            throw new AssetConflictException(
                String.format("Asset with ID %s already exists.", assetCreateRequest.getId()));
        } catch (Exception e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            throw new AssetCreationFailedException("Asset creation failed.");
        }
        // create policy
        String policyDefinitionId = "policyDefinitionId_" + UUID.randomUUID();

        PolicyCreateRequest policyCreateRequest = PolicyCreateRequest.builder().id(policyDefinitionId)
            .policy(createEdcOfferBE.getPolicy()).build();

        IdResponse policyIdResponse = null;
        try {
            log.info("Creating Policy {}", policyCreateRequest);
            policyIdResponse = edcClient.createPolicy(policyCreateRequest);
        } catch (WebClientResponseException.Conflict e) {
            log.error("Conflict occurred: {}", e.getMessage(), e);
            throw new PolicyConflictException(
                String.format("Policy definition with ID %s already exists.", policyCreateRequest.getId()));
        } catch (Exception e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            throw new PolicyCreationFailedException("Policy definition creation failed.");
        }

        // create contract definition
        String contractDefinitionId = "contractDefinitionId_" + UUID.randomUUID();

        ContractDefinitionCreateRequest contractDefinitionCreateRequest = ContractDefinitionCreateRequest.builder()
            .id(contractDefinitionId).contractPolicyId(policyIdResponse.getId())
            .accessPolicyId(policyIdResponse.getId()).assetsSelector(List.of(
                Criterion.builder().operandLeft("https://w3id.org/edc/v0.0.1/ns/id").operator("=")
                    .operandRight(assetIdResponse.getId()).build())).build();

        IdResponse contractDefinitionIdResponse = null;
        try {
            log.info("Creating Contract Definition {}", contractDefinitionCreateRequest);
            contractDefinitionIdResponse = edcClient.createContractDefinition(contractDefinitionCreateRequest);
        } catch (WebClientResponseException.Conflict e) {
            log.error("Conflict occurred: {}", e.getMessage(), e);
            throw new ContractDefinitionConflictException(
                String.format("Contract definition with ID %s already exists.",
                    contractDefinitionCreateRequest.getId()));
        } catch (Exception e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            throw new ContractDefinitionCreationException("Contract definition creation failed.");
        }

        return contractDefinitionIdResponse;
    }

    private FhIdResponse createFhCatalogOffer(CreateFhOfferBE createFhOfferBE, String assetId) {

        DcatDataset dataset = DcatDataset.builder().id(assetId).hasPolicy(createFhOfferBE.getPolicy())
            .title(createFhOfferBE.getOfferName()).description(createFhOfferBE.getOfferDescription()).build();

        String value_type = "identifiers";
        Map<String, String> auth = Map.of("Content-Type", "application/json", "Authorization",
            "Bearer " + fhCatalogSecretKey);

        FhIdResponse response = null;
        try {
            log.info("Adding Dataset to Fraunhofer Catalog {}", dataset);
            response = fhCatalogClient.addDatasetToFhCatalog(auth, dataset, catalogName, value_type);
            log.info("Response from FH Catalog: {}", response.getId());
        } catch (WebClientResponseException e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            throw e;
        }

        return response;
    }

}
