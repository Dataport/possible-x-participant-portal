package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.*;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.daps.OmejdnConnectorDetailsBE;
import eu.possiblex.participantportal.business.entity.edc.contractagreement.ContractAgreement;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.edc.policy.PolicyTarget;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcessState;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.fh.OfferingDetailsSparqlQueryResult;
import eu.possiblex.participantportal.business.entity.fh.ParticipantDetailsSparqlQueryResult;
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
    private ContractService contractService;

    @Autowired
    private ConsumerService consumerService;

    @Value("${participant-id}")
    private String participantId;

    @Test
    void testGetContractAgreementsWhenParticipantIsProvider() {

        resetMocks();

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

        List<ContractAgreementBE> actual = contractService.getContractAgreements();

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
    void testGetContractAgreementsWhenParticipantIsNotProvider() {

        resetMocks();

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

        List<ContractAgreementBE> actual = contractService.getContractAgreements();

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
    void testGetContractAgreementsWhenNoConnectorDetailsAvailable() {

        resetMocks();

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

        List<ContractAgreementBE> actual = contractService.getContractAgreements();

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
    void testGetContractAgreementsWhenNoContractAgreementsAvailable() {

        resetMocks();

        // set up mock returning no contract agreements
        Mockito.when(edcClient.queryContractAgreements(any())).thenReturn(List.of());

        List<ContractAgreementBE> actual = contractService.getContractAgreements();

        verifyNoInteractions(fhCatalogClient);
        verify(edcClient).queryContractAgreements(any());

        assertThat(actual).isEmpty();

    }

    @Test
    void testGetContractDetailsByContractAgreementId() {

        resetMocks();

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

        ContractDetailsBE actual = contractService.getContractDetailsByContractAgreementId("some id");

        verify(fhCatalogClient).getFhCatalogOffer(any());
        verify(fhCatalogClient).getParticipantDetailsByIds(any());
        verify(fhCatalogClient).getOfferingDetailsByAssetIds(any());
        verify(edcClient).getContractAgreementById(any());

        assertThat(actual).isNotNull();
        assertThat(actual.getOfferingDetails().getOfferRetrievalDate()).isEqualTo(offerRetrievalDate);
        assertThat(actual.getOfferingDetails().getCatalogOffering().getName()).isEqualTo(serviceName);
        assertThat(actual.getOfferingDetails().getCatalogOffering().getDescription()).isEqualTo(serviceDescription);
        assertThat(actual.getProviderDetails().getName()).isEqualTo(OmejdnConnectorApiClientFake.PARTICIPANT_NAME);
        assertThat(actual.getConsumerDetails().getName()).isEqualTo(
            OmejdnConnectorApiClientFake.OTHER_PARTICIPANT_NAME);
        assertThat(actual.isDataOffering()).isFalse();

    }

    @Test
    void testGetOfferDetailsByContractAgreementId() {

        resetMocks();

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

        OfferRetrievalResponseBE actual = contractService.getOfferDetailsByContractAgreementId("some id");

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
    void transferDataOfferAgain() {

        reset(fhCatalogClient);
        reset(consumerService);

        //GIVEN

        TransferOfferRequestBE request = TransferOfferRequestBE.builder().edcOfferId(EdcClientFake.FAKE_ID)
            .contractAgreementId(EdcClientFake.VALID_CONTRACT_AGREEEMENT_ID).build();
        TransferOfferResponseBE response = TransferOfferResponseBE.builder()
            .transferProcessState(TransferProcessState.COMPLETED).build();

        // set up mock to return offering details from the catalog
        OfferingDetailsSparqlQueryResult queryResult = new OfferingDetailsSparqlQueryResult();
        queryResult.setAssetId(EdcClientFake.FAKE_ID);
        queryResult.setProviderUrl(EdcClientFake.VALID_COUNTER_PARTY_ADDRESS);
        Mockito.when(fhCatalogClient.getOfferingDetailsByAssetIds(any()))
            .thenReturn(Map.of(EdcClientFake.FAKE_ID, queryResult));

        //WHEN
        TransferOfferResponseBE actual = contractService.transferDataOfferAgain(request);

        //THEN
        assertThat(actual.getTransferProcessState()).isEqualTo(response.getTransferProcessState());
        verify(consumerService).transferDataOffer(any());
        verify(fhCatalogClient).getOfferingDetailsByAssetIds(List.of(EdcClientFake.FAKE_ID));
    }

    @Test
    void transferDataOfferAgainOfferingNotFound() {

        reset(fhCatalogClient);
        reset(consumerService);

        //GIVEN

        TransferOfferRequestBE request = TransferOfferRequestBE.builder().edcOfferId(EdcClientFake.FAKE_ID)
            .contractAgreementId(EdcClientFake.VALID_CONTRACT_AGREEEMENT_ID).build();

        // set up mock to return no offering details from the catalog
        Mockito.when(fhCatalogClient.getOfferingDetailsByAssetIds(any())).thenReturn(Map.of());

        //WHEN / THEN
        assertThrows(OfferNotFoundException.class, () -> contractService.transferDataOfferAgain(request));

        verify(fhCatalogClient).getOfferingDetailsByAssetIds(List.of(EdcClientFake.FAKE_ID));
        verifyNoInteractions(consumerService);
    }

    private void resetMocks() {

        reset(fhCatalogClient);
        reset(edcClient);
        reset(omejdnConnectorApiClient);
    }

    // Test-specific configuration to provide mocks
    @TestConfiguration
    static class TestConfig {
        @Bean
        public ConsumerService consumerService() {

            return Mockito.spy(new ConsumerServiceFake());
        }

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