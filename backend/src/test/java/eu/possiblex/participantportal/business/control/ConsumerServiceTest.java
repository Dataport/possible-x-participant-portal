package eu.possiblex.participantportal.business.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.business.entity.*;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubjectSubset;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatCatalog;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDataset;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
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

import java.util.Collections;
import java.util.List;

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
        Mockito.when(fhCatalogClient.getFhCatalogOffer(EdcClientFake.FAKE_ID)).thenReturn(fhCatalogOffer);
        DcatCatalog catalog = new DcatCatalog();
        DcatDataset dataset = new DcatDataset();
        dataset.setId(EdcClientFake.FAKE_ID);
        dataset.setAssetId(EdcClientFake.FAKE_ID);
        dataset.setName("correctName");
        dataset.setContenttype("correctContentType");
        dataset.setDescription("correctDescription");
        dataset.setHasPolicy(Collections.emptyList());
        catalog.setDataset(List.of(dataset));
        Mockito.when(edcClient.queryCatalog(any())).thenReturn(catalog);
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
        DcatCatalog catalog = new DcatCatalog();
        DcatDataset dataset = new DcatDataset();
        dataset.setId(EdcClientFake.FAKE_ID);
        dataset.setAssetId(EdcClientFake.FAKE_ID);
        dataset.setName("correctName");
        dataset.setContenttype("correctContentType");
        dataset.setDescription("correctDescription");
        dataset.setHasPolicy(List.of(new Policy()));
        catalog.setDataset(List.of(dataset));
        Mockito.when(edcClient.queryCatalog(any())).thenReturn(catalog);
        // WHEN

        AcceptOfferResponseBE response = sut.acceptContractOffer(
            ConsumeOfferRequestBE.builder().counterPartyAddress("http://example.com").edcOfferId(EdcClientFake.FAKE_ID)
                .dataOffering(true).build());

        // THEN

        verify(edcClient).negotiateOffer(any());

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
        DcatCatalog catalog = new DcatCatalog();
        DcatDataset dataset = new DcatDataset();
        dataset.setId(EdcClientFake.FAKE_ID);
        dataset.setAssetId(EdcClientFake.FAKE_ID);
        dataset.setName("correctName");
        dataset.setContenttype("correctContentType");
        dataset.setDescription("correctDescription");
        dataset.setHasPolicy(List.of(new Policy()));
        catalog.setDataset(List.of(dataset));
        Mockito.when(edcClient.queryCatalog(any())).thenReturn(catalog);
        // WHEN

        AcceptOfferResponseBE response = sut.acceptContractOffer(
            ConsumeOfferRequestBE.builder().counterPartyAddress("http://example.com").edcOfferId(EdcClientFake.FAKE_ID)
                .dataOffering(false).build());

        // THEN

        verify(edcClient).negotiateOffer(any());

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
        DcatCatalog catalog = new DcatCatalog();
        DcatDataset dataset = new DcatDataset();
        dataset.setId(EdcClientFake.BAD_NEGOTIATION_ID);
        dataset.setAssetId(EdcClientFake.BAD_NEGOTIATION_ID);
        dataset.setName("correctName");
        dataset.setContenttype("correctContentType");
        dataset.setDescription("correctDescription");
        dataset.setHasPolicy(List.of(new Policy()));
        catalog.setDataset(List.of(dataset));
        Mockito.when(edcClient.queryCatalog(any())).thenReturn(catalog);
        assertThrows(NegotiationFailedException.class, () -> sut.acceptContractOffer(
            ConsumeOfferRequestBE.builder().counterPartyAddress("http://example.com")
                .edcOfferId(EdcClientFake.BAD_NEGOTIATION_ID).build()));
    }

    @Test
    void shouldNotTransfer() {

        reset(edcClient);
        DcatCatalog catalog = new DcatCatalog();
        DcatDataset dataset = new DcatDataset();
        dataset.setId(EdcClientFake.BAD_TRANSFER_ID);
        dataset.setAssetId(EdcClientFake.BAD_TRANSFER_ID);
        dataset.setName("correctName");
        dataset.setContenttype("correctContentType");
        dataset.setDescription("correctDescription");
        dataset.setHasPolicy(Collections.emptyList());
        catalog.setDataset(List.of(dataset));
        Mockito.when(edcClient.queryCatalog(any())).thenReturn(catalog);
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
        DcatCatalog catalog = new DcatCatalog();
        DcatDataset dataset = new DcatDataset();
        dataset.setId(EdcClientFake.FAKE_ID);
        dataset.setAssetId(EdcClientFake.FAKE_ID);
        dataset.setName("correctName");
        dataset.setContenttype("correctContentType");
        dataset.setDescription("correctDescription");
        dataset.setHasPolicy(Collections.emptyList());
        catalog.setDataset(List.of(dataset));
        Mockito.when(edcClient.queryCatalog(any())).thenReturn(catalog);
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
