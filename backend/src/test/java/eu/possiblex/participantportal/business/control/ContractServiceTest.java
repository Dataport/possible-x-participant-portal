package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.*;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.daps.OmejdnConnectorDetailsBE;
import eu.possiblex.participantportal.business.entity.edc.contractagreement.ContractAgreement;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.edc.policy.PolicyTarget;
import eu.possiblex.participantportal.business.entity.exception.ContractAgreementNotFoundException;
import eu.possiblex.participantportal.business.entity.fh.OfferingDetailsSparqlQueryResult;
import eu.possiblex.participantportal.business.entity.fh.ParticipantDetailsSparqlQueryResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = { ContractServiceTest.TestConfig.class, ContractServiceImpl.class })
class ContractServiceTest {

    @Autowired
    private EdcClient edcClient;

    @Autowired
    private OmejdnConnectorApiClient omejdnConnectorApiClient;

    @Autowired
    private FhCatalogClient fhCatalogClient;

    @Autowired
    private ContractService sut;

    @Value("${participant-id}")
    private String participantId;

    @BeforeEach
    void setUp() {

        reset(fhCatalogClient);
        reset(edcClient);
        reset(omejdnConnectorApiClient);
    }

    @Test
    void getContractAgreementsWhenParticipantIsProvider() {

        // set up mock using the participantId from the test properties
        Mockito.when(fhCatalogClient.getParticipantDetailsByIds(any())).thenReturn(Map.of(participantId,
            ParticipantDetailsSparqlQueryResult.builder().name(OmejdnConnectorApiClientFake.PARTICIPANT_NAME).build(),
            OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_ID,
            ParticipantDetailsSparqlQueryResult.builder().name(OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_NAME)
                .build()));

        // set up mock using the participantId from the test properties
        Mockito.when(omejdnConnectorApiClient.getConnectorDetails(any())).thenReturn(
            Map.of(OmejdnConnectorApiClientFake.PARTICIPANT_ID,
                OmejdnConnectorDetailsBE.builder().clientId(OmejdnConnectorApiClientFake.PARTICIPANT_ID)
                    .clientName(OmejdnConnectorApiClientFake.PARTICIPANT_NAME).attributes(Map.of("did", participantId))
                    .build(), OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_ID,
                OmejdnConnectorDetailsBE.builder().clientId(OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_ID)
                    .clientName(OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_NAME)
                    .attributes(Map.of("did", OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_ID)).build()));

        List<ContractAgreementBE> actual = sut.getContractAgreements();

        verify(fhCatalogClient).getParticipantDetailsByIds(any());
        verify(fhCatalogClient).getOfferingDetailsByAssetIds(any());
        verify(edcClient).queryContractAgreements(any());

        assertThat(actual).isNotEmpty().hasSize(1);
        assertThat(actual.get(0).getOfferingDetails().getAssetId()).isEqualTo(EdcClientFake.FAKE_ID);
        assertThat(actual.get(0).getProviderDetails().getName()).isEqualTo(
            OmejdnConnectorApiClientFake.PARTICIPANT_NAME);
        assertThat(actual.get(0).getConsumerDetails().getName()).isEqualTo(
            OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_NAME);
        assertThat(actual.get(0).isDataOffering()).isFalse();
        assertThat(actual.get(0).isProvider()).isTrue(); // participant is provider

    }

    @Test
    void getContractAgreementsWhenParticipantIsNotProvider() {

        // set up mock using the participantId from the test properties
        Mockito.when(fhCatalogClient.getParticipantDetailsByIds(any())).thenReturn(Map.of(participantId,
            ParticipantDetailsSparqlQueryResult.builder().name(OmejdnConnectorApiClientFake.PARTICIPANT_NAME).build(),
            OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_ID,
            ParticipantDetailsSparqlQueryResult.builder().name(OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_NAME)
                .build()));

        // set up mock using the participantId from the test properties
        Mockito.when(omejdnConnectorApiClient.getConnectorDetails(any())).thenReturn(
            Map.of(OmejdnConnectorApiClientFake.PARTICIPANT_ID,
                OmejdnConnectorDetailsBE.builder().clientId(OmejdnConnectorApiClientFake.PARTICIPANT_ID)
                    .clientName(OmejdnConnectorApiClientFake.PARTICIPANT_NAME).attributes(Map.of("did", participantId))
                    .build(), OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_ID,
                OmejdnConnectorDetailsBE.builder().clientId(OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_ID)
                    .clientName(OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_NAME)
                    .attributes(Map.of("did", OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_ID)).build()));

        // set up mock making the participant the consumer
        Policy policy = Policy.builder().target(PolicyTarget.builder().id(EdcClientFake.FAKE_ID).build()).build();
        ContractAgreement contractAgreement = ContractAgreement.builder()
            .contractSigningDate(BigInteger.valueOf(1728549145)).id(EdcClientFake.FAKE_ID)
            .assetId(EdcClientFake.FAKE_ID)
            .consumerId(OmejdnConnectorApiClientFake.PARTICIPANT_ID) // participant is consumer
            .providerId(OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_ID).policy(policy).build();
        Mockito.when(edcClient.queryContractAgreements(any())).thenReturn(List.of(contractAgreement));

        List<ContractAgreementBE> actual = sut.getContractAgreements();

        verify(fhCatalogClient).getParticipantDetailsByIds(any());
        verify(fhCatalogClient).getOfferingDetailsByAssetIds(any());
        verify(edcClient).queryContractAgreements(any());

        assertThat(actual).isNotEmpty().hasSize(1);
        assertThat(actual.get(0).getOfferingDetails().getAssetId()).isEqualTo(EdcClientFake.FAKE_ID);
        assertThat(actual.get(0).getProviderDetails().getName()).isEqualTo(
            OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_NAME);
        assertThat(actual.get(0).getConsumerDetails().getName()).isEqualTo(
            OmejdnConnectorApiClientFake.PARTICIPANT_NAME);
        assertThat(actual.get(0).isDataOffering()).isFalse();
        assertThat(actual.get(0).isProvider()).isFalse();

    }

    @Test
    void getContractAgreementsWhenNoConnectorDetailsAvailable() {

        // set up mock using the participantId from the test properties
        Mockito.when(fhCatalogClient.getParticipantDetailsByIds(any())).thenReturn(Map.of(participantId,
            ParticipantDetailsSparqlQueryResult.builder().name(OmejdnConnectorApiClientFake.PARTICIPANT_NAME).build(),
            OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_ID,
            ParticipantDetailsSparqlQueryResult.builder().name(OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_NAME)
                .build()));

        // set up mock using the participantId from the test properties
        Mockito.when(omejdnConnectorApiClient.getConnectorDetails(any())).thenReturn(
            Map.of(OmejdnConnectorApiClientFake.PARTICIPANT_ID,
                OmejdnConnectorDetailsBE.builder().clientId(OmejdnConnectorApiClientFake.PARTICIPANT_ID)
                    .clientName(OmejdnConnectorApiClientFake.PARTICIPANT_NAME).attributes(Map.of("did", participantId))
                    .build(), OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_ID,
                OmejdnConnectorDetailsBE.builder().clientId(OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_ID)
                    .clientName(OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_NAME)
                    .attributes(Map.of("did", OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_ID)).build()));

        // set up mock such that no connector details are available
        Mockito.when(omejdnConnectorApiClient.getConnectorDetails(any())).thenReturn(new HashMap<>());

        List<ContractAgreementBE> actual = sut.getContractAgreements();

        verify(fhCatalogClient).getParticipantDetailsByIds(any());
        verify(fhCatalogClient).getOfferingDetailsByAssetIds(any());
        verify(edcClient).queryContractAgreements(any());

        assertThat(actual).isNotEmpty().hasSize(1);
        assertThat(actual.get(0).getOfferingDetails().getAssetId()).isEqualTo(EdcClientFake.FAKE_ID);
        assertThat(actual.get(0).getProviderDetails().getName()).isEqualTo("Unknown");
        assertThat(actual.get(0).getConsumerDetails().getName()).isEqualTo("Unknown");
        assertThat(actual.get(0).isDataOffering()).isFalse();
        assertThat(actual.get(0).isProvider()).isFalse();

    }

    @Test
    void getContractAgreementsWhenNoContractAgreementsAvailable() {

        // set up mock returning no contract agreements
        Mockito.when(edcClient.queryContractAgreements(any())).thenReturn(List.of());

        List<ContractAgreementBE> actual = sut.getContractAgreements();

        verifyNoInteractions(fhCatalogClient);
        verify(edcClient).queryContractAgreements(any());

        assertThat(actual).isEmpty();

    }

    @Test
    void getContractDetailsByContractAgreementId() {

        // set up mock using the participantId from the test properties
        Mockito.when(fhCatalogClient.getParticipantDetailsByIds(any())).thenReturn(Map.of(participantId,
            ParticipantDetailsSparqlQueryResult.builder().name(OmejdnConnectorApiClientFake.PARTICIPANT_NAME).build(),
            OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_ID,
            ParticipantDetailsSparqlQueryResult.builder().name(OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_NAME)
                .build()));

        // set up mock using the participantId from the test properties
        Mockito.when(omejdnConnectorApiClient.getConnectorDetails(any())).thenReturn(
            Map.of(OmejdnConnectorApiClientFake.PARTICIPANT_ID,
                OmejdnConnectorDetailsBE.builder().clientId(OmejdnConnectorApiClientFake.PARTICIPANT_ID)
                    .clientName(OmejdnConnectorApiClientFake.PARTICIPANT_NAME).attributes(Map.of("did", participantId))
                    .build(), OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_ID,
                OmejdnConnectorDetailsBE.builder().clientId(OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_ID)
                    .clientName(OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_NAME)
                    .attributes(Map.of("did", OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_ID)).build()));

        // set up mock to return an offering from the catalog with time of retrieval
        String serviceName = "test name";
        String serviceDescription = "test description";
        PxExtendedServiceOfferingCredentialSubject pxExtendedServiceOfferingCredentialSubject = PxExtendedServiceOfferingCredentialSubject.builder()
            .aggregationOf(List.of()).name(serviceName).description(serviceDescription).assetId(EdcClientFake.FAKE_ID)
            .build();
        OffsetDateTime offerRetrievalDate = OffsetDateTime.now();
        Mockito.when(fhCatalogClient.getFhCatalogOffer(any()))
            .thenReturn(new OfferRetrievalResponseBE(pxExtendedServiceOfferingCredentialSubject, offerRetrievalDate));

        // set up mock to return offering details from the catalog
        Mockito.when(fhCatalogClient.getOfferingDetailsByAssetIds(any())).thenReturn(Map.of(EdcClientFake.FAKE_ID,
            OfferingDetailsSparqlQueryResult.builder().assetId(EdcClientFake.FAKE_ID).uri("some uri").build()));

        ContractDetailsBE actual = sut.getContractDetailsByContractAgreementId("some id");

        verify(fhCatalogClient).getFhCatalogOffer(any());
        verify(fhCatalogClient).getParticipantDetailsByIds(any());
        verify(fhCatalogClient).getOfferingDetailsByAssetIds(any());
        verify(edcClient).getContractAgreementById(any());

        assertThat(actual).isNotNull();
        assertThat(actual.getOfferingDetails().getOfferRetrievalDate()).isEqualTo(offerRetrievalDate);
        assertThat(actual.getOfferingDetails().getCatalogOffering().getName()).isEqualTo(serviceName);
        assertThat(actual.getOfferingDetails().getCatalogOffering().getDescription()).isEqualTo(serviceDescription);
        assertThat(actual.getOfferingDetails().getCatalogOffering().getAssetId()).isEqualTo(EdcClientFake.FAKE_ID);
        assertThat(actual.getProviderDetails().getName()).isEqualTo(OmejdnConnectorApiClientFake.PARTICIPANT_NAME);
        assertThat(actual.getConsumerDetails().getName()).isEqualTo(
            OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_NAME);
        assertThat(actual.isDataOffering()).isFalse();

    }

    @Test
    void getContractDetailsByContractAgreementIdContractAgreementNotFound() {

        assertThrows(ContractAgreementNotFoundException.class,
            () -> sut.getContractDetailsByContractAgreementId(EdcClientFake.NOT_FOUND_AGREEMENT_ID));

        verifyNoInteractions(fhCatalogClient);
    }

    @Test
    void getContractDetailsByContractAgreementIdOfferNotFound() {

        // set up mock using the participantId from the test properties
        Mockito.when(fhCatalogClient.getParticipantDetailsByIds(any())).thenReturn(Map.of(participantId,
            ParticipantDetailsSparqlQueryResult.builder().name(OmejdnConnectorApiClientFake.PARTICIPANT_NAME).build(),
            OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_ID,
            ParticipantDetailsSparqlQueryResult.builder().name(OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_NAME)
                .build()));

        // set up mock using the participantId from the test properties
        Mockito.when(omejdnConnectorApiClient.getConnectorDetails(any())).thenReturn(
            Map.of(OmejdnConnectorApiClientFake.PARTICIPANT_ID,
                OmejdnConnectorDetailsBE.builder().clientId(OmejdnConnectorApiClientFake.PARTICIPANT_ID)
                    .clientName(OmejdnConnectorApiClientFake.PARTICIPANT_NAME).attributes(Map.of("did", participantId))
                    .build(), OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_ID,
                OmejdnConnectorDetailsBE.builder().clientId(OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_ID)
                    .clientName(OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_NAME)
                    .attributes(Map.of("did", OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_ID)).build()));

        // set up mock to return no offering details from the catalog
        Mockito.when(fhCatalogClient.getOfferingDetailsByAssetIds(any())).thenReturn(Map.of());

        ContractDetailsBE actual = sut.getContractDetailsByContractAgreementId("some id");

        verify(fhCatalogClient, never()).getFhCatalogOffer(any());
        verify(fhCatalogClient).getParticipantDetailsByIds(any());
        verify(fhCatalogClient).getOfferingDetailsByAssetIds(any());
        verify(edcClient).getContractAgreementById(any());

        String unknown = "Unknown";
        assertThat(actual).isNotNull();
        assertTrue(actual.getOfferingDetails().getOfferRetrievalDate().isBefore(OffsetDateTime.now()));
        assertThat(actual.getOfferingDetails().getCatalogOffering().getName()).isEqualTo(unknown);
        assertThat(actual.getOfferingDetails().getCatalogOffering().getDescription()).isEqualTo(unknown);
        assertThat(actual.getOfferingDetails().getCatalogOffering().getAssetId()).isNull();
        assertThat(actual.getProviderDetails().getName()).isEqualTo(OmejdnConnectorApiClientFake.PARTICIPANT_NAME);
        assertThat(actual.getConsumerDetails().getName()).isEqualTo(
            OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_NAME);
        assertThat(actual.isDataOffering()).isFalse();
    }

    @Test
    void getOfferDetailsByContractAgreementId() {

        // set up mock to return an offering with the asset id from the contract agreement from the catalog and time of retrieval
        PxExtendedServiceOfferingCredentialSubject pxExtendedServiceOfferingCredentialSubject = PxExtendedServiceOfferingCredentialSubject.builder()
            .aggregationOf(List.of()).name("test name").description("test description").assetId(EdcClientFake.FAKE_ID)
            .build();
        OffsetDateTime offerRetrievalDate = OffsetDateTime.now();
        Mockito.when(fhCatalogClient.getFhCatalogOffer(any()))
            .thenReturn(new OfferRetrievalResponseBE(pxExtendedServiceOfferingCredentialSubject, offerRetrievalDate));

        // set up mock to return offering details from the catalog with the asset id from the contract agreement
        Mockito.when(fhCatalogClient.getOfferingDetailsByAssetIds(any())).thenReturn(Map.of(EdcClientFake.FAKE_ID,
            OfferingDetailsSparqlQueryResult.builder().assetId(EdcClientFake.FAKE_ID).uri("some uri").build()));

        OfferRetrievalResponseBE actual = sut.getOfferDetailsByContractAgreementId("some id");

        verify(fhCatalogClient).getFhCatalogOffer(any());
        verify(fhCatalogClient).getOfferingDetailsByAssetIds(any());
        verify(edcClient).getContractAgreementById(any());

        assertThat(actual).isNotNull();
        assertThat(actual.getOfferRetrievalDate()).isEqualTo(offerRetrievalDate);
        assertThat(actual.getCatalogOffering().getName()).isEqualTo("test name");
        assertThat(actual.getCatalogOffering().getDescription()).isEqualTo("test description");
        assertThat(actual.getCatalogOffering().getAssetId()).isEqualTo(EdcClientFake.FAKE_ID);
    }

    @Test
    void getOfferDetailsByContractAgreementIdContractAgreementNotFound() {

        assertThrows(ContractAgreementNotFoundException.class,
            () -> sut.getOfferDetailsByContractAgreementId(EdcClientFake.NOT_FOUND_AGREEMENT_ID));

        verifyNoInteractions(fhCatalogClient);
    }

    @Test
    void getOfferDetailsByContractAgreementIdOfferNotFound() {

        // set up mock to return offering details from the catalog with the asset id from the contract agreement
        Mockito.when(fhCatalogClient.getOfferingDetailsByAssetIds(any())).thenReturn(Map.of());

        OfferRetrievalResponseBE actual = sut.getOfferDetailsByContractAgreementId("some id");

        verify(fhCatalogClient, never()).getFhCatalogOffer(any());
        verify(fhCatalogClient).getOfferingDetailsByAssetIds(any());
        verify(edcClient).getContractAgreementById(any());

        String unknown = "Unknown";
        assertThat(actual).isNotNull();
        assertTrue(actual.getOfferRetrievalDate().isBefore(OffsetDateTime.now()));
        assertThat(actual.getCatalogOffering().getName()).isEqualTo(unknown);
        assertThat(actual.getCatalogOffering().getDescription()).isEqualTo(unknown);
        assertThat(actual.getCatalogOffering().getAssetId()).isNull();
    }

    // Test-specific configuration to provide mocks
    @TestConfiguration
    static class TestConfig {
        @Bean
        public EdcClient edcClient() {

            return Mockito.spy(new EdcClientFake());
        }

        @Bean
        public EnforcementPolicyParserService enforcementPolicyParserService() {

            return Mockito.spy(new EnforcementPolicyParserServiceFake());
        }

        @Bean
        public FhCatalogClient fhCatalogClient() {

            return Mockito.spy(new FhCatalogClientFake());
        }

        @Bean
        public OmejdnConnectorApiClient omejdnConnectorApiClient() {

            return Mockito.spy(new OmejdnConnectorApiClientFake());
        }
    }

}