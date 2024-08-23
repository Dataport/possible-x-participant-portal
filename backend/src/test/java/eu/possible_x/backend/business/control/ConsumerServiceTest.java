package eu.possible_x.backend.business.control;

import eu.possiblex.participantportal.business.control.ConsumerService;
import eu.possiblex.participantportal.business.control.ConsumerServiceImpl;
import eu.possiblex.participantportal.business.control.EdcClient;
import eu.possiblex.participantportal.business.entity.ConsumeOfferRequestBO;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcess;
import eu.possiblex.participantportal.service.EdcClientFake;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ContextConfiguration(classes = { ConsumerServiceTest.TestConfig.class, ConsumerServiceImpl.class })
class ConsumerServiceTest {
    @Autowired
    ConsumerService consumerService;

    @Autowired
    EdcClient edcClient;

    @Test
    void shouldAcceptContractOffer() {

        TransferProcess response = consumerService.acceptContractOffer(
            ConsumeOfferRequestBO.builder().counterPartyAddress("http://example.com").build());

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
