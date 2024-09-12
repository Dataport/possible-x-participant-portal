package eu.possiblex.participantportal.business.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.edc.asset.AssetCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.asset.ionoss3extension.IonosS3DataSource;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.edc.policy.PolicyCreateRequest;
import eu.possiblex.participantportal.business.entity.exception.EdcOfferCreationException;
import eu.possiblex.participantportal.business.entity.exception.FhOfferCreationException;
import eu.possiblex.participantportal.business.entity.fh.CreateFhOfferBE;
import eu.possiblex.participantportal.business.entity.fh.catalog.DcatDataset;
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

    private static final String POLICY_JSON_STRING = """
        {
            "@id": "GENERATED_POLICY_ID",
            "@type": "odrl:Set",
            "odrl:permission": [
              {
                "odrl:action": {
                  "odrl:type": "http://www.w3.org/ns/odrl/2/use"
                }
              },
              {
                "odrl:action": {
                  "odrl:type": "http://www.w3.org/ns/odrl/2/transfer"
                }
              }
            ],
            "odrl:prohibition": [],
            "odrl:obligation": []
          }""";

    @Autowired
    ProviderService providerService;

    @Autowired
    EdcClient edcClient;

    @Autowired
    FhCatalogClient fhCatalogClient;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testCreateOffer() throws JsonProcessingException, EdcOfferCreationException, FhOfferCreationException {
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

        ArgumentCaptor<DcatDataset> dcatDatasetCaptor = forClass(DcatDataset.class);

        verify(fhCatalogClient).addDatasetToFhCatalog(any(), dcatDatasetCaptor.capture(), any(), any());
        //check if assetId exists and AccessURL is set correctly
        DcatDataset dcatDataset = dcatDatasetCaptor.getValue();
        assertNotNull(dcatDataset);
        assertTrue(dcatDataset.getAssetId()
                .matches("assetId_[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));
        assertEquals("test", dcatDataset.getDistribution().get(0).getAccessUrl());

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
            .matches("policyDefinitionId_[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));
        assertEquals("GENERATED_POLICY_ID", policyCreateRequest.getPolicy().getId());

        assertNotNull(response);
        assertNotNull(response.getEdcResponseId());
        assertNotNull(response.getFhResponseId());
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