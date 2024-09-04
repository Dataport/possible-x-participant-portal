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
import eu.possiblex.participantportal.business.entity.exception.EdcOfferCreationException;
import eu.possiblex.participantportal.business.entity.exception.FhOfferCreationException;
import eu.possiblex.participantportal.business.entity.fh.CreateFhOfferBE;
import eu.possiblex.participantportal.business.entity.fh.FhIdResponse;
import eu.possiblex.participantportal.business.entity.fh.catalog.DcatDataset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
        throws FhOfferCreationException, EdcOfferCreationException {

        String assetId = "assetId_" + UUID.randomUUID();

        CreateOfferResponseTO createOfferResponseTO = new CreateOfferResponseTO();

        FhIdResponse fhResponseId = null;
        try {
            fhResponseId = createFhCatalogOffer(createFhOfferBE, assetId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new FhOfferCreationException(e.getMessage());
        }

        IdResponse edcResponseId = null;
        try {
            edcResponseId = createEdcOffer(createEdcOfferBE, assetId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new EdcOfferCreationException(e.getMessage());
        }

        createOfferResponseTO.setEdcResponseId(edcResponseId.getId());
        createOfferResponseTO.setFhResponseId(fhResponseId.getId());

        return createOfferResponseTO;

    }

    private IdResponse createEdcOffer(CreateEdcOfferBE createEdcOfferBE, String assetId) {
        // create asset
        DataAddress dataAddress = IonosS3DataSource.builder().bucketName("dev-provider-edc-bucket-possible-31952746")
            .blobName(createEdcOfferBE.getFileName()).keyName(createEdcOfferBE.getFileName())
            .storage("s3-eu-central-2.ionoscloud.com").build();
        AssetCreateRequest assetCreateRequest = AssetCreateRequest.builder().id(assetId).properties(
            AssetProperties.builder().name(createEdcOfferBE.getAssetName())
                .description(createEdcOfferBE.getAssetDescription()).
                //version("assetVersion").
                    contenttype("application/json").build()).dataAddress(dataAddress).build();

        log.info("Creating Asset {}", assetCreateRequest);
        IdResponse assetIdResponse = edcClient.createAsset(assetCreateRequest);

        // create policy
        String policyDefinitionId = "policyDefinitionId_" + UUID.randomUUID();

        PolicyCreateRequest policyCreateRequest = PolicyCreateRequest.builder().id(policyDefinitionId)
            .policy(createEdcOfferBE.getPolicy()).build();

        log.info("Creating Policy {}", policyCreateRequest);
        IdResponse policyIdResponse = edcClient.createPolicy(policyCreateRequest);

        // create contract definition
        String contractDefinitionId = "contractDefinitionId_" + UUID.randomUUID();

        ContractDefinitionCreateRequest contractDefinitionCreateRequest = ContractDefinitionCreateRequest.builder()
            .id(contractDefinitionId).contractPolicyId(policyIdResponse.getId())
            .accessPolicyId(policyIdResponse.getId()).assetsSelector(List.of(
                Criterion.builder().operandLeft("https://w3id.org/edc/v0.0.1/ns/id").operator("=")
                    .operandRight(assetIdResponse.getId()).build())).build();

        log.info("Creating Contract Definition {}", contractDefinitionCreateRequest);

        return edcClient.createContractDefinition(contractDefinitionCreateRequest);
    }

    private FhIdResponse createFhCatalogOffer(CreateFhOfferBE createFhOfferBE, String assetId) {

        DcatDataset dataset = DcatDataset.builder().id(assetId).hasPolicy(createFhOfferBE.getPolicy())
            .title(createFhOfferBE.getOfferName()).description(createFhOfferBE.getOfferDescription()).build();

        String value_type = "identifiers";
        Map<String, String> auth = Map.of("Content-Type", "application/json", "Authorization",
            "Bearer " + fhCatalogSecretKey);

        log.info("Adding Dataset to Fraunhofer Catalog {}", dataset);
        FhIdResponse response = fhCatalogClient.addDatasetToFhCatalog(auth, dataset, catalogName, value_type);
        log.info("Response from FH Catalog: {}", response.getId());

        return response;
    }

}
