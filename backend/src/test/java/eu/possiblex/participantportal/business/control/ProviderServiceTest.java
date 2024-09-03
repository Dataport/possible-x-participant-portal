package eu.possiblex.participantportal.business.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.edc.asset.AssetCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.asset.ionoss3extension.IonosS3DataSource;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.edc.policy.PolicyCreateRequest;
import eu.possiblex.participantportal.business.entity.fh.CreateFhOfferBE;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ContextConfiguration(classes = { ProviderServiceTest.TestConfig.class, ProviderServiceImpl.class })
class ProviderServiceTest {
    private static final String FILE_NAME = "file.txt";

    private static final String POLICY_JSON_STRING =
        "{\n" + "    \"@id\": \"GENERATED_POLICY_ID\",\n" + "    \"@type\": \"odrl:Set\",\n"
            + "    \"odrl:permission\": [\n" + "      {\n" + "        \"odrl:target\": \"GENERATED_ASSET_ID\",\n"
            + "        \"odrl:action\": {\n" + "          \"odrl:type\": \"http://www.w3.org/ns/odrl/2/use\"\n"
            + "        }\n" + "      },\n" + "      {\n" + "        \"odrl:target\": \"GENERATED_ASSET_ID\",\n"
            + "        \"odrl:action\": {\n" + "          \"odrl:type\": \"http://www.w3.org/ns/odrl/2/transfer\"\n"
            + "        }\n" + "      }\n" + "    ],\n" + "    \"odrl:prohibition\": [],\n"
            + "    \"odrl:obligation\": []\n" + "  }";

    @Autowired
    ProviderService providerService;

    @Autowired
    EdcClient edcClient;

    @Autowired
    FhCatalogClient fhCatalogClient;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testCreateOffer() throws JsonProcessingException {
        //given
        CreateEdcOfferBE createEdcOfferBE = CreateEdcOfferBE.builder().fileName(FILE_NAME)
            .policy(objectMapper.readValue(POLICY_JSON_STRING, Policy.class)).build();
        CreateFhOfferBE createFhOfferBE = CreateFhOfferBE.builder()
            .policy(objectMapper.readValue(POLICY_JSON_STRING, Policy.class)).build();

        //when
        var response = providerService.createOffer(createFhOfferBE, createEdcOfferBE);

        //then
        ArgumentCaptor<AssetCreateRequest> assetCreateRequestCaptor = forClass(AssetCreateRequest.class);
        ArgumentCaptor<PolicyCreateRequest> policyCreateRequestCaptor = forClass(PolicyCreateRequest.class);

        verify(fhCatalogClient).addDatasetToFhCatalog(any(), any(), any(), any());

        verify(edcClient).createAsset(assetCreateRequestCaptor.capture());
        verify(edcClient).createPolicy(policyCreateRequestCaptor.capture());
        verify(edcClient).createContractDefinition(any());

        AssetCreateRequest assetCreateRequest = assetCreateRequestCaptor.getValue();
        //check if file name is set correctly
        assertEquals(FILE_NAME, assetCreateRequest.getDataAddress().getKeyName());
        assertEquals(FILE_NAME, ((IonosS3DataSource) assetCreateRequest.getDataAddress()).getBlobName());

        PolicyCreateRequest policyCreateRequest = policyCreateRequestCaptor.getValue();
        //check if policyId is set correctly
        assertTrue(policyCreateRequest.getId()
            .matches("policyId_[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));
        assertEquals("GENERATED_POLICY_ID", policyCreateRequest.getPolicy().getId());
        //check if target of permissions and prohibitions is not placeholder value anymore
        policyCreateRequest.getPolicy().getPermission()
            .forEach(p -> assertNotEquals("GENERATED_ASSET_ID", p.get("odrl:target").textValue()));
        policyCreateRequest.getPolicy().getProhibition()
            .forEach(p -> assertNotEquals("GENERATED_ASSET_ID", p.get("odrl:target").textValue()));

        assertNotNull(response);
        assertNotNull(response.get("EDC-ID"));
        assertNotNull(response.get("FH-ID"));
    }

    // Test-specific configuration to provide a fake implementation of EdcClient and FhCatalogClient
    @TestConfiguration
    static class TestConfig {
        @Bean
        public EdcClient edcClient() {

            return Mockito.spy(new EdcClientFake());
        }

        @Bean
        public FhCatalogClient fhCatalogClient() {

            return Mockito.spy(new FhCatalogClientFake());
        }

        @Bean
        public ObjectMapper objectMapper() {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper;
        }
    }

}