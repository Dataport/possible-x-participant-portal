package eu.possible_x.edc_orchestrator.service;

import eu.possible_x.edc_orchestrator.entities.edc.asset.AssetCreateRequest;
import eu.possible_x.edc_orchestrator.entities.edc.asset.AssetProperties;
import eu.possible_x.edc_orchestrator.entities.edc.asset.DataAddress;
import eu.possible_x.edc_orchestrator.entities.edc.asset.ionoss3extension.IonosS3DataSource;
import eu.possible_x.edc_orchestrator.entities.edc.common.IdResponse;
import eu.possible_x.edc_orchestrator.entities.edc.contractdefinition.ContractDefinitionCreateRequest;
import eu.possible_x.edc_orchestrator.entities.edc.contractdefinition.Criterion;
import eu.possible_x.edc_orchestrator.entities.edc.policy.Policy;
import eu.possible_x.edc_orchestrator.entities.edc.policy.PolicyCreateRequest;
import eu.possible_x.edc_orchestrator.entities.fh.FhIdResponse;
import eu.possible_x.edc_orchestrator.entities.fh.catalog.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ProviderService {

    private final EdcClient edcClient;
    private final FhCatalogClient fhCatalogClient;

    @Value("${fh.catalog.secret-key}")
    private String fhCatalogSecretKey;

    public ProviderService(@Autowired EdcClient edcClient, @Autowired FhCatalogClient fhCatalogClient) {
        this.edcClient = edcClient;
        this.fhCatalogClient = fhCatalogClient;
    }

    public IdResponse createOffer() {
        createDatasetEntryInFhCatalog("test-provider");
        return createEdcOffer();
    }
  
    private IdResponse createEdcOffer() {
        // create asset
        DataAddress dataAddress = IonosS3DataSource.builder()
            .bucketName("dev-provider-edc-bucket-possible-31952746")
            .blobName("ssss.txt")
            .keyName("ssss.txt")
            .storage("s3-eu-central-2.ionoscloud.com")
            .build();
        AssetCreateRequest assetCreateRequest = AssetCreateRequest.builder()
                .id("assetId_" + UUID.randomUUID())
                .properties(AssetProperties.builder()
                        .name("assetName")
                        .description("assetDescription")
                        .version("assetVersion")
                        .contenttype("application/json")
                        .build())
                .dataAddress(dataAddress)
                .build();

        log.info("Creating Asset {}", assetCreateRequest);
        IdResponse assetIdResponse = edcClient.createAsset(assetCreateRequest);

        // create policy
        String policyId = "policyId_" + UUID.randomUUID();
        PolicyCreateRequest policyCreateRequest = PolicyCreateRequest.builder()
                .id(policyId)
                .policy(Policy.builder()
                        .id(policyId)
                        .obligation(Collections.emptyList())
                        .prohibition(Collections.emptyList())
                        .permission(Collections.emptyList())
                        .build())
                .build();
        log.info("Creating Policy {}", policyCreateRequest);
        IdResponse policyIdResponse = edcClient.createPolicy(policyCreateRequest);

        // create contract definition
        String contractDefinitionId = "contractDefinitionId_" + UUID.randomUUID();
        ContractDefinitionCreateRequest contractDefinitionCreateRequest = ContractDefinitionCreateRequest.builder()
                .id(contractDefinitionId)
                .contractPolicyId(policyIdResponse.getId())
                .accessPolicyId(policyIdResponse.getId())
                .assetsSelector(List.of(Criterion.builder()
                        .operandLeft("https://w3id.org/edc/v0.0.1/ns/id")
                        .operator("=")
                        .operandRight(assetIdResponse.getId())
                        .build()))
                .build();
        log.info("Creating Contract Definition {}", contractDefinitionCreateRequest);
        return edcClient.createContractDefinition(contractDefinitionCreateRequest);
    }

    private FhIdResponse createDatasetEntryInFhCatalog(String cat_name) {
        DatasetToCatalogRequest datasetToCatalogRequest = DatasetToCatalogRequest.builder()
                .graphElements(List.of(
                        GraphFirstElement.builder()
                                .id("_:b4")
                                .foafmbox(FoafMbox.builder()
                                        .id("mailto:info@gv.hamburg.de")
                                        .build())
                                .type("foaf:Organization")
                                .foafname("Landesbetrieb für Geoinformation und Vermessung")
                                .build(),
                        GraphSecondElement.builder()
                                .id("https://piveau.io/set/distribution/6c2122e6-59d6-4342-ada9-a2f336450add")
                                .type("dcat:Distribution")
                                //.title("my_file.pdf")
                                .identifier("https://possible.fokus.fraunhofer.de/set/distribution/1")
                                .accessURL(AccessURL.builder()
                                        .id("http://85.215.193.145:9192/api/v1/data/assets/test-document_company2")
                                        .build())
                                .build(),
                        GraphThirdElement.builder()
                                .id("https://piveau.io/set/data/hamburg_geo_id")
                                .language(DctLanguage.builder()
                                        .id("http://publications.europa.eu/resource/authority/language/DEU")
                                        .build())
                                .producedBy(GaxTrustFrameworkProducedBy.builder()
                                        .id("https://www.hamburg.de/politik-und-verwaltung/behoerden/behoerde-fuer-stadtentwicklung-und-wohnen/aemter-und-landesbetrieb/landesbetrieb-geoinformation-und-vermessung/wir-ueber-uns/impressum-244100")
                                        .build())
                                .title(DctTitle.builder()
                                        .language("de")
                                        .value("Schulstandorte Hamburg")
                                        .build())
                                .distribution(DcatDistribution.builder()
                                        .id("https://piveau.io/set/distribution/6c2122e6-59d6-4342-ada9-a2f336450add")
                                        .build())
                                .description(DctDescription.builder()
                                        .language("de")
                                        .value("Für jede Schule und ggf. ihre Zweigstellen werden dargestellt: - Geoposition und ggf. - Adresse (Straße, Hausnummer, PLZ, Ort) - Kontaktdaten (Telefon, E-Mail-Funktionspostfach, Fax, Homepage) - Schulmerkmale (Schulnummer, Zweigstelle ja/nein, Schulform nach Haushaltskapitel, Erwachsenenbildung ja/nein, Telefonnummer der Schulaufsicht, zugehöriger ReBBZ-Standort) - Zahl der Schüler")
                                        .build())
                                .publisher(DctPublisher.builder()
                                        .id("_:b4")
                                        .build())
                                .build()))
                .build();

        String value_type = "identifiers";
        Map<String, String> auth = Map.of(
                "Content-Type", "application/json",
                "Authorization", "Bearer " + fhCatalogSecretKey);
        log.info("Adding Dataset to Fraunhofer Catalog {}", datasetToCatalogRequest);
        FhIdResponse response = fhCatalogClient.addDatasetToFhCatalog(auth, datasetToCatalogRequest, cat_name, value_type);
        log.info("Response from FH Catalog: {}", response.getId());
        return response;
    }
}
