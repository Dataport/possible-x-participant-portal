package eu.possiblex.participantportal.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.edc.common.IdResponse;
import eu.possiblex.participantportal.business.control.EdcClient;
import eu.possiblex.participantportal.business.control.FhCatalogClient;
import eu.possiblex.participantportal.business.control.ProviderService;
import eu.possiblex.participantportal.business.entity.fh.CreateDatasetEntryBE;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ContextConfiguration(classes = { ProviderServiceTest.TestConfig.class, ProviderService.class })
class ProviderServiceTest {
    @Autowired
    ProviderService providerService;

    @Autowired
    EdcClient edcClient;

    @Autowired
    FhCatalogClient fhCatalogClient;

    @MockBean
    ObjectMapper objectMapper;

    @Test
    void testCreateOffer() {

        IdResponse response = providerService.createOffer(new CreateDatasetEntryBE(), new CreateEdcOfferBE());

        verify(fhCatalogClient).addDatasetToFhCatalog(any(), any(), any(), any());

        verify(edcClient).createAsset(any());
        verify(edcClient).createPolicy(any());
        verify(edcClient).createContractDefinition(any());

        assertNotNull(response);
        assertNotNull(response.getId());
        assertFalse(response.getId().isBlank());
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
    }

}