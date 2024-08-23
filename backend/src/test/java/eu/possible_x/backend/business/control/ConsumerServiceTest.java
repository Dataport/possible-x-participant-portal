package eu.possible_x.backend.business.control;

import eu.possiblex.participantportal.business.control.ConsumerService;
import eu.possiblex.participantportal.business.control.ConsumerServiceImpl;
import eu.possiblex.participantportal.business.control.EdcClient;
import eu.possiblex.participantportal.business.entity.ConsumeOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferRequestBE;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDataset;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ContextConfiguration(classes = { ConsumerServiceTest.TestConfig.class, ConsumerServiceImpl.class })
class ConsumerServiceTest {

    // Test-specific configuration to provide a fake implementation of EdcClient
    @TestConfiguration
    static class TestConfig {
        @Bean
        public EdcClient edcClient() {

            return Mockito.spy(new EdcClientFake());
        }
    }

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private EdcClient edcClient;

    @Test
    void shouldSelectContractOffer() {

        DcatDataset response = consumerService.selectContractOffer(
            SelectOfferRequestBE
                .builder()
                .counterPartyAddress("http://example.com")
                .offerId(EdcClientFake.FAKE_ID)
                .build());

        verify(edcClient).queryCatalog(any());
        verify(edcClient, times(0)).initiateTransfer(any());

        assertNotNull(response);
    }

    @Test
    void shouldAcceptContractOffer() {

        TransferProcess response = consumerService.acceptContractOffer(
            ConsumeOfferRequestBE
                .builder()
                .counterPartyAddress("http://example.com")
                .offerId(EdcClientFake.FAKE_ID)
                .build());

        verify(edcClient).negotiateOffer(any());
        verify(edcClient).initiateTransfer(any());

        assertNotNull(response);
    }
}
