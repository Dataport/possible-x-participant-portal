package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.ConsumeOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferRequestBE;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDataset;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcess;
import eu.possiblex.participantportal.business.entity.exception.NegotiationFailedException;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.TransferFailedException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    void shouldSelectContractOffer() throws OfferNotFoundException {

        reset(edcClient);
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
    @Disabled // TODO enable this once the user actually selects an existing offering
    void shouldSelectContractOfferNotFound() {

        assertThrows(OfferNotFoundException.class, () -> consumerService.selectContractOffer(
            SelectOfferRequestBE
                .builder()
                .counterPartyAddress("http://example.com")
                .offerId("someUnknownId")
                .build()));
    }

    @Test
    void shouldAcceptContractOffer()
        throws NegotiationFailedException, TransferFailedException, OfferNotFoundException {

        reset(edcClient);
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

    @Test
    void shouldAcceptContractOfferNotFound() {

        assertThrows(OfferNotFoundException.class, () -> consumerService.acceptContractOffer(
            ConsumeOfferRequestBE
                .builder()
                .counterPartyAddress("http://example.com")
                .offerId("someUnknownId")
                .build()));
    }

    @Test
    void shouldAcceptContractOfferBadNegotiation() {

        assertThrows(NegotiationFailedException.class, () -> consumerService.acceptContractOffer(
            ConsumeOfferRequestBE
                .builder()
                .counterPartyAddress("http://example.com")
                .offerId(EdcClientFake.BAD_NEGOTIATION_ID)
                .build()));
    }

    @Test
    void shouldAcceptContractOfferBadTransfer() {

        assertThrows(TransferFailedException.class, () -> consumerService.acceptContractOffer(
            ConsumeOfferRequestBE
                .builder()
                .counterPartyAddress("http://example.com")
                .offerId(EdcClientFake.BAD_TRANSFER_ID)
                .build()));
    }
}
