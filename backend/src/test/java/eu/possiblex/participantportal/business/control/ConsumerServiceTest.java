package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.ConsumeOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferResponseBE;
import eu.possiblex.participantportal.business.entity.AcceptOfferResponseBE;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcess;
import eu.possiblex.participantportal.business.entity.exception.NegotiationFailedException;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.TransferFailedException;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogOffer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = { ConsumerServiceTest.TestConfig.class, ConsumerServiceImpl.class })
class ConsumerServiceTest {

    @Autowired
    private ConsumerService sut;

    @Autowired
    private EdcClient edcClient;

    @Autowired
    private FhCatalogClient fhCatalogClient;

    @Test
    void selectContractOfferSucceeds() throws OfferNotFoundException {

        // GIVEN

        reset(edcClient);
        reset(fhCatalogClient);
        FhCatalogOffer fhCatalogOffer = new FhCatalogOffer();
        fhCatalogOffer.setAssetId(EdcClientFake.FAKE_ID);
        Mockito.when(fhCatalogClient.getFhCatalogOffer(Mockito.eq(EdcClientFake.FAKE_ID))).thenReturn(fhCatalogOffer);

        // WHEN

        SelectOfferResponseBE response = sut.selectContractOffer(
            SelectOfferRequestBE.builder().fhCatalogOfferId(EdcClientFake.FAKE_ID).build());

        // THEN

        verify(edcClient).queryCatalog(any());
        verify(edcClient, times(0)).initiateTransfer(any());

        assertNotNull(response);
    }

    @Test
    void acceptContractOfferSucceeds()
        throws NegotiationFailedException, TransferFailedException, OfferNotFoundException {

        // GIVEN

        reset(edcClient);
        reset(fhCatalogClient);

        // WHEN

        AcceptOfferResponseBE response = sut.acceptContractOffer(
            ConsumeOfferRequestBE.builder().counterPartyAddress("http://example.com").edcOfferId(EdcClientFake.FAKE_ID).dataOffering(true)
                .build());

        // THEN

        verify(edcClient).negotiateOffer(any());
        verify(edcClient).initiateTransfer(any());

        assertNotNull(response);
    }

    @Test
    void acceptContractOfferSucceedsNoTransfer()
            throws NegotiationFailedException, TransferFailedException, OfferNotFoundException {

        // GIVEN

        reset(edcClient);
        reset(fhCatalogClient);

        // WHEN

        AcceptOfferResponseBE response = sut.acceptContractOffer(
                ConsumeOfferRequestBE.builder().counterPartyAddress("http://example.com").edcOfferId(EdcClientFake.FAKE_ID).dataOffering(false)
                        .build());

        // THEN

        verify(edcClient).negotiateOffer(any());
        verify(edcClient, never()).initiateTransfer(any());

        assertNotNull(response);
    }

    @Test
    void shouldAcceptContractOfferNotFound() {

        reset(edcClient);
        reset(fhCatalogClient);
        assertThrows(OfferNotFoundException.class, () -> sut.acceptContractOffer(
            ConsumeOfferRequestBE.builder().counterPartyAddress("http://example.com").edcOfferId("someUnknownId")
                .build()));
    }

    @Test
    void shouldAcceptContractOfferBadNegotiation() {

        reset(edcClient);
        reset(fhCatalogClient);
        assertThrows(NegotiationFailedException.class, () -> sut.acceptContractOffer(
            ConsumeOfferRequestBE.builder().counterPartyAddress("http://example.com")
                .edcOfferId(EdcClientFake.BAD_NEGOTIATION_ID).build()));
    }

    @Test
    void shouldAcceptContractOfferBadTransfer() {

        reset(edcClient);
        reset(fhCatalogClient);
        assertThrows(TransferFailedException.class, () -> sut.acceptContractOffer(
            ConsumeOfferRequestBE.builder().counterPartyAddress("http://example.com")
                .edcOfferId(EdcClientFake.BAD_TRANSFER_ID).dataOffering(true).build()));
    }

    // Test-specific configuration to provide mocks
    @TestConfiguration
    static class TestConfig {
        @MockBean
        private TaskScheduler taskScheduler;

        @Bean
        public EdcClient edcClient() {

            return Mockito.spy(new EdcClientFake());
        }

        @Bean
        public FhCatalogClient fhCatalogClient() {

            return Mockito.mock(FhCatalogClient.class);
        }
    }
}
