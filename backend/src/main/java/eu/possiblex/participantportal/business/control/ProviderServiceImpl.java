package eu.possiblex.participantportal.business.control;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
public class ProviderServiceImpl implements ProviderService{

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
     * Given a request for creating a dataset entry in the Fraunhofer catalog and
     * a request for creating an EDC offer, create the dataset entry and the offer in the EDC catalog.
     * @param createFhOfferBE request for creating a dataset entry
     * @param createEdcOfferBE request for creating an EDC offer
     * @return success message (currently an IdResponse)
     */
    @Override
    public ObjectNode createOffer(CreateFhOfferBE createFhOfferBE, CreateEdcOfferBE createEdcOfferBE) {

        ObjectNode node = objectMapper.createObjectNode();

        var idResponse = createEdcOffer(createEdcOfferBE);
        node.put("EDC-ID", idResponse.getId());

        var fhIdResponse = createFhCatalogOffer(createFhOfferBE);
        node.put("FH-ID", fhIdResponse.getId());

        return node;

    }

    private IdResponse createEdcOffer(CreateEdcOfferBE createEdcOfferBE) {
        // create asset
        DataAddress dataAddress = IonosS3DataSource.builder().bucketName("dev-provider-edc-bucket-possible-31952746")
            .blobName(createEdcOfferBE.getFileName()).keyName(createEdcOfferBE.getFileName())
            .storage("s3-eu-central-2.ionoscloud.com").build();
        AssetCreateRequest assetCreateRequest = AssetCreateRequest.builder().id("assetId_" + UUID.randomUUID())
            .properties(
                AssetProperties.builder().name(createEdcOfferBE.getAssetName()).description(createEdcOfferBE.getAssetDescription()).
                        //version("assetVersion").
                        contenttype("application/json").build()).dataAddress(dataAddress).build();

        log.info("Creating Asset {}", assetCreateRequest);
        IdResponse assetIdResponse = edcClient.createAsset(assetCreateRequest);

        // create policy
        String policyId = "policyId_" + UUID.randomUUID();
        Policy policy = getPolicy(createEdcOfferBE, policyId, assetIdResponse);
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

    private FhIdResponse createFhCatalogOffer(CreateFhOfferBE createFhOfferBE) {

        DatasetToCatalogRequest datasetToCatalogRequest = DatasetToCatalogRequest.builder().graphElements(List.of(
                GraphFirstElement.builder().id("_:b4").foafmbox(FoafMbox.builder().id("mailto:info@gv.hamburg.de").build())
                        .type("foaf:Organization").foafname("Landesbetrieb für Geoinformation und Vermessung").build(),
                GraphSecondElement.builder().id("https://piveau.io/set/distribution/6c2122e6-59d6-4342-ada9-a2f336450add")
                        .type("dcat:Distribution")
                        //.title("my_file.pdf")
                        .identifier("https://possible.fokus.fraunhofer.de/set/distribution/1").accessURL(
                                AccessURL.builder().id("http://85.215.193.145:9192/api/v1/data/assets/test-document_company2").build())
                        .build(), GraphThirdElement.builder().id("https://piveau.io/set/data/hamburg_geo_id").language(
                                DctLanguage.builder().id("http://publications.europa.eu/resource/authority/language/DEU").build())
                        .producedBy(GaxTrustFrameworkProducedBy.builder()
                                .id("https://www.hamburg.de/politik-und-verwaltung/behoerden/behoerde-fuer-stadtentwicklung-und-wohnen/aemter-und-landesbetrieb/landesbetrieb-geoinformation-und-vermessung/wir-ueber-uns/impressum-244100")
                                .build()).title(DctTitle.builder().language("de").value("Schulstandorte Hamburg").build())
                        .distribution(DcatDistribution.builder()
                                .id("https://piveau.io/set/distribution/6c2122e6-59d6-4342-ada9-a2f336450add").build()).description(
                                DctDescription.builder().language("de").value(
                                                "Für jede Schule und ggf. ihre Zweigstellen werden dargestellt: - Geoposition und ggf. - Adresse (Straße, Hausnummer, PLZ, Ort) - Kontaktdaten (Telefon, E-Mail-Funktionspostfach, Fax, Homepage) - Schulmerkmale (Schulnummer, Zweigstelle ja/nein, Schulform nach Haushaltskapitel, Erwachsenenbildung ja/nein, Telefonnummer der Schulaufsicht, zugehöriger ReBBZ-Standort) - Zahl der Schüler")
                                        .build()).publisher(DctPublisher.builder().id("_:b4").build()).build())).build();

        String value_type = "identifiers";
        Map<String, String> auth = Map.of("Content-Type", "application/json", "Authorization",
                "Bearer " + fhCatalogSecretKey);
        log.info("Adding Dataset to Fraunhofer Catalog {}", datasetToCatalogRequest);
        FhIdResponse response = fhCatalogClient.addDatasetToFhCatalog(auth, datasetToCatalogRequest, catalogName,
                value_type);
        log.info("Response from FH Catalog: {}", response.getId());
        return response;
    }

    private Policy getPolicy(CreateEdcOfferBE createEdcOfferBE, String policyId, IdResponse assetIdResponse) {

        Policy policy = null;
        String policyAttibuteString = "policy";
        String targetAttributeString = "odrl:target";
        try {
            JsonNode policyNode = createEdcOfferBE.getPolicy().get(policyAttibuteString);
            policy = objectMapper.treeToValue(policyNode, Policy.class);

            //set policyId
            policy.setId(policyId);

            //set target to assetId in permissions and prohibitions
            policy.getPermission().forEach(p -> ((ObjectNode) p).put(targetAttributeString, assetIdResponse.getId()));
            policy.getProhibition().forEach(p -> ((ObjectNode) p).put(targetAttributeString, assetIdResponse.getId()));

            //set target with assetId
            policy.setTarget(PolicyTarget.builder().id(assetIdResponse.getId()).build());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return policy;
    }
}
