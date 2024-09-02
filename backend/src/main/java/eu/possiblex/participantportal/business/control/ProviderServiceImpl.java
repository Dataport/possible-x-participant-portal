package eu.possiblex.participantportal.business.control;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.possiblex.participantportal.business.entity.common.JsonLdConstants;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.edc.asset.AssetCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.asset.AssetProperties;
import eu.possiblex.participantportal.business.entity.edc.asset.DataAddress;
import eu.possiblex.participantportal.business.entity.edc.asset.ionoss3extension.IonosS3DataSource;
import eu.possiblex.participantportal.business.entity.edc.common.IdResponse;
import eu.possiblex.participantportal.business.entity.edc.contractdefinition.ContractDefinitionCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.contractdefinition.Criterion;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.edc.policy.PolicyCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.policy.PolicyTarget;
import eu.possiblex.participantportal.business.entity.fh.CreateFhOfferBE;
import eu.possiblex.participantportal.business.entity.fh.FhIdResponse;
import eu.possiblex.participantportal.business.entity.fh.catalog.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class ProviderServiceImpl implements ProviderService {

    @Value("${fh.catalog.secret-key}")
    private String fhCatalogSecretKey;

    @Value("${fh.catalog.catalog-name}")
    private String catalogName;

    private final EdcClient edcClient;

    private final FhCatalogClient fhCatalogClient;

    private final ObjectMapper objectMapper;

    public ProviderServiceImpl(@Autowired EdcClient edcClient, @Autowired FhCatalogClient fhCatalogClient,
        @Autowired ObjectMapper objectMapper) {

        this.edcClient = edcClient;
        this.fhCatalogClient = fhCatalogClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Given a request for creating a dataset entry in the Fraunhofer catalog and a request for creating an EDC offer,
     * create the dataset entry and the offer in the EDC catalog.
     *
     * @param createFhOfferBE request for creating a dataset entry
     * @param createEdcOfferBE request for creating an EDC offer
     * @return success message (currently an IdResponse)
     */
    @Override
    public ObjectNode createOffer(CreateFhOfferBE createFhOfferBE, CreateEdcOfferBE createEdcOfferBE) {

        String assetId = "assetId_" + UUID.randomUUID();

        ObjectNode node = objectMapper.createObjectNode();

        var fhIdResponse = createFhCatalogOffer(createFhOfferBE, assetId);
        node.put("FH-ID", fhIdResponse.getId());

        var idResponse = createEdcOffer(createEdcOfferBE, assetId);
        node.put("EDC-ID", idResponse.getId());

        return node;

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
        String policyId = "policyId_" + UUID.randomUUID();
        Policy policy = getPolicy(createEdcOfferBE.getPolicy(), assetIdResponse.getId());
        PolicyCreateRequest policyCreateRequest = PolicyCreateRequest.builder().id(policyId).policy(policy).build();
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

        Policy policy = getPolicy(createFhOfferBE.getPolicy(), assetId);

        DcatDataset dataset = DcatDataset.builder().id(assetId).hasPolicy(policy).title(createFhOfferBE.getOfferName())
            .description(createFhOfferBE.getOfferDescription()).build();

        String value_type = "identifiers";
        Map<String, String> auth = Map.of("Content-Type", "application/json", "Authorization",
            "Bearer " + fhCatalogSecretKey);
        log.info("Adding Dataset to Fraunhofer Catalog {}", dataset);
        FhIdResponse response = fhCatalogClient.addDatasetToFhCatalog(auth, dataset, catalogName,
            value_type);
        log.info("Response from FH Catalog: {}", response.getId());
        return response;
    }

    private Policy getPolicy(JsonNode policyJsonNode, String assetId) {

        Policy policy = null;
        String policyAttibuteString = "policy";
        String targetAttributeString = JsonLdConstants.ODRL_PREFIX + "target";
        try {
            JsonNode policyNode = policyJsonNode.get(policyAttibuteString);

            policy = objectMapper.treeToValue(policyNode, Policy.class);

            //set target to assetId in permissions and prohibitions
            policy.getPermission().forEach(p -> ((ObjectNode) p).put(targetAttributeString, assetId));
            policy.getProhibition().forEach(p -> ((ObjectNode) p).put(targetAttributeString, assetId));

            //set target with assetId
            policy.setTarget(PolicyTarget.builder().id(assetId).build());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return policy;
    }
}
