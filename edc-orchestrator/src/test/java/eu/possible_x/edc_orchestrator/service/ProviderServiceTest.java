package eu.possible_x.edc_orchestrator.service;

import eu.possible_x.edc_orchestrator.entities.edc.common.IdResponse;
import eu.possible_x.edc_orchestrator.entities.fh.FhIdResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ContextConfiguration(classes = {ProviderServiceTest.TestConfig.class, ProviderService.class})
class ProviderServiceTest {
    @Autowired
    ProviderService providerService;

    @Autowired
    EdcClient edcClient;

    @Autowired
    FhCatalogClient fhCatalogClient;

    // Test-specific configuration to provide a fake implementation of EdcClient
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
    }

    @Test
    void testCreateOffer () {
        IdResponse response = providerService.createOffer();

        verify(fhCatalogClient).addDatasetToFhCatalog(any(),any(),any(),any());

        verify(edcClient).createAsset(any());
        verify(edcClient).createPolicy(any());
        verify(edcClient).createContractDefinition(any());

        assertNotNull(response);
        assertNotNull(response.getId());
        assertFalse(response.getId().isBlank());
    }

}