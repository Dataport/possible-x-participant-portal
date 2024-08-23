package eu.possible_x.backend.service;

import eu.possible_x.backend.application.entity.ConsumeOfferRequestTO;
import eu.possible_x.backend.business.entity.edc.asset.ionoss3extension.IonosS3DataAddress;
import eu.possible_x.backend.business.control.ConsumerService;
import eu.possible_x.backend.business.control.EdcClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ContextConfiguration(classes = { ConsumerServiceTest.TestConfig.class, ConsumerService.class })
class ConsumerServiceTest {
    @Autowired
    ConsumerService consumerService;

    @Autowired
    EdcClient edcClient;

    @Test
    void shouldAcceptContractOffer() {

        IonosS3DataAddress response = consumerService.acceptContractOffer(
            ConsumeOfferRequestTO.builder().counterPartyAddress("http://example.com").build());

        assertNotNull(response);
    }

    // Test-specific configuration to provide a fake implementation of EdcClient
    @TestConfiguration
    static class TestConfig {
        @Bean
        public EdcClient edcClient() {

            return Mockito.spy(new EdcClientFake());
        }
    }
}
