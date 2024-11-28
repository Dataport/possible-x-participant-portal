package eu.possiblex.participantportal.business.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.business.entity.*;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubjectSubset;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.exception.NegotiationFailedException;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.ParticipantNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.TransferFailedException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        PxExtendedServiceOfferingCredentialSubject fhCatalogOffer = new PxExtendedServiceOfferingCredentialSubject();
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
    void acceptContractOfferSucceeds() throws NegotiationFailedException, ParticipantNotFoundException, OfferNotFoundException {

        // GIVEN

        reset(edcClient);
        reset(fhCatalogClient);
        PxExtendedLegalParticipantCredentialSubjectSubset fhCatalogParticipant = new PxExtendedLegalParticipantCredentialSubjectSubset();
        fhCatalogParticipant.setId(FhCatalogClientFake.FAKE_PROVIDER_ID);
        fhCatalogParticipant.setMailAddress(FhCatalogClientFake.FAKE_EMAIL_ADDRESS);
        Mockito.when(fhCatalogClient.getFhCatalogParticipant(Mockito.eq(FhCatalogClientFake.FAKE_PROVIDER_ID))).thenReturn(fhCatalogParticipant);

        // WHEN

        AcceptOfferResponseBE response = sut.acceptContractOffer(
            ConsumeOfferRequestBE.builder().counterPartyAddress("http://example.com").edcOfferId(EdcClientFake.FAKE_ID).providedBy(FhCatalogClientFake.FAKE_PROVIDER_ID)
                .dataOffering(true).build());

        // THEN

        verify(edcClient).negotiateOffer(any());
        verify(fhCatalogClient).getFhCatalogParticipant(any());

        assertNotNull(response);
    }

    @Test
    void acceptContractOfferSucceedsNoTransfer() throws NegotiationFailedException, ParticipantNotFoundException, OfferNotFoundException {

        // GIVEN

        reset(edcClient);
        reset(fhCatalogClient);
        PxExtendedLegalParticipantCredentialSubjectSubset fhCatalogParticipant = new PxExtendedLegalParticipantCredentialSubjectSubset();
        fhCatalogParticipant.setId(FhCatalogClientFake.FAKE_PROVIDER_ID);
        fhCatalogParticipant.setMailAddress(FhCatalogClientFake.FAKE_EMAIL_ADDRESS);
        Mockito.when(fhCatalogClient.getFhCatalogParticipant(Mockito.eq(FhCatalogClientFake.FAKE_PROVIDER_ID))).thenReturn(fhCatalogParticipant);
        // WHEN

        AcceptOfferResponseBE response = sut.acceptContractOffer(
            ConsumeOfferRequestBE.builder().counterPartyAddress("http://example.com").edcOfferId(EdcClientFake.FAKE_ID).providedBy(FhCatalogClientFake.FAKE_PROVIDER_ID)
                .dataOffering(false).build());

        // THEN

        verify(edcClient).negotiateOffer(any());
        verify(fhCatalogClient).getFhCatalogParticipant(any());

        assertNotNull(response);
    }

    @Test
    void shouldAcceptContractOfferNotFound() {

        reset(edcClient);
        assertThrows(OfferNotFoundException.class, () -> sut.acceptContractOffer(
            ConsumeOfferRequestBE.builder().counterPartyAddress("http://example.com").edcOfferId("someUnknownId")
                .build()));
    }

    @Test
    void shouldAcceptContractOfferBadNegotiation() {

        reset(edcClient);
        assertThrows(NegotiationFailedException.class, () -> sut.acceptContractOffer(
            ConsumeOfferRequestBE.builder().counterPartyAddress("http://example.com")
                .edcOfferId(EdcClientFake.BAD_NEGOTIATION_ID).build()));
    }

    @Test
    void shouldNotTransfer() {

        reset(edcClient);
        assertThrows(TransferFailedException.class, () -> sut.transferDataOffer(
            TransferOfferRequestBE.builder().counterPartyAddress("http://example.com")
                .edcOfferId(EdcClientFake.BAD_TRANSFER_ID).contractAgreementId(EdcClientFake.VALID_AGREEMENT_ID)
                .build()));
    }

    @Test
    void shouldTransfer() throws OfferNotFoundException, TransferFailedException {

        // GIVEN

        reset(edcClient);

        // WHEN

        TransferOfferResponseBE response = sut.transferDataOffer(
            TransferOfferRequestBE.builder().counterPartyAddress("http://example.com").edcOfferId(EdcClientFake.FAKE_ID)
                .contractAgreementId(EdcClientFake.VALID_AGREEMENT_ID).build());

        // THEN

        verify(edcClient).initiateTransfer(any());

        assertNotNull(response);
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
        public ObjectMapper objectMapper() {

            return new ObjectMapper();
        }

        @Bean
        public FhCatalogClient fhCatalogClient() {

            return Mockito.mock(FhCatalogClient.class);
        }
    }
}
