package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.application.entity.policies.*;
import eu.possiblex.participantportal.business.entity.*;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.edc.asset.ionoss3extension.IonosS3DataSource;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAsset;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAssetDataAccountExport;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAssetProperties;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAssetTnC;
import eu.possiblex.participantportal.business.entity.edc.contractagreement.ContractAgreement;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.edc.policy.PolicyTarget;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcessState;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.TransferFailedException;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ContextConfiguration(classes = { ContractServiceTest.TestConfig.class, ContractServiceImpl.class })
class ContractServiceTest {

    @Autowired
    private EdcClient edcClient;

    @Autowired
    private FhCatalogClient fhCatalogClient;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ConsumerService consumerService;

    @Value("${participant-id}")
    private String participantId;

    @Value("${edc.protocol-base-url}")
    private String participantEdcProtocolUrl;

    @Test
    void testGetContractAgreementsWhenParticipantIsProvider() throws OfferNotFoundException {

        reset(fhCatalogClient);
        reset(edcClient);

        Mockito.when(fhCatalogClient.getParticipantDetailsByIds(any())).thenReturn(
            Map.of(OmejdnConnectorApiClientFake.PARTICIPANT_ID,
                ParticipantDetailsSparqlQueryResult.builder().name(OmejdnConnectorApiClientFake.PARTICIPANT_NAME)
                    .build()));
        Mockito.when(fhCatalogClient.getOfferingDetailsByAssetIds(any())).thenReturn(Map.of(EdcClientFake.FAKE_ID,
            OfferingDetailsSparqlQueryResult.builder().assetId(EdcClientFake.FAKE_ID)
                .providerUrl(participantEdcProtocolUrl).build()));

        List<ContractAgreementBE> expected = getContractAgreementBEs();
        List<ContractAgreementBE> actual = contractService.getContractAgreements();

        verify(fhCatalogClient).getParticipantDetailsByIds(any());
        verify(fhCatalogClient).getOfferingDetailsByAssetIds(any());
        verify(edcClient).queryContractAgreements();

        assertThat(actual).isNotEmpty();
        assertThat(actual.size()).isEqualTo(1).isEqualTo(expected.size());
        assertThat(actual.get(0).getOfferingDetails().getAssetId()).isEqualTo(EdcClientFake.FAKE_ID);
        assertThat(actual.get(0).getProviderDetails().getName()).isEqualTo("Unknown");
        assertThat(actual.get(0).getConsumerDetails().getName()).isEqualTo("Unknown");
        assertThat(actual.get(0).isProvider()).isTrue();

    }

    @Test
    void testGetContractAgreementsWhenParticipantIsNotProvider() throws OfferNotFoundException {

        reset(fhCatalogClient);
        reset(edcClient);

        String otherParticipantEdcProtocolUrl = "other";

        Mockito.when(fhCatalogClient.getParticipantDetailsByIds(any())).thenReturn(
            Map.of(OmejdnConnectorApiClientFake.PARTICIPANT_ID,
                ParticipantDetailsSparqlQueryResult.builder().name(OmejdnConnectorApiClientFake.PARTICIPANT_NAME)
                    .build()));
        Mockito.when(fhCatalogClient.getOfferingDetailsByAssetIds(any())).thenReturn(Map.of(EdcClientFake.FAKE_ID,
            OfferingDetailsSparqlQueryResult.builder().assetId(EdcClientFake.FAKE_ID)
                .providerUrl(otherParticipantEdcProtocolUrl).build()));

        List<ContractAgreementBE> expected = getContractAgreementBEs();
        List<ContractAgreementBE> actual = contractService.getContractAgreements();

        verify(fhCatalogClient).getParticipantDetailsByIds(any());
        verify(fhCatalogClient).getOfferingDetailsByAssetIds(any());
        verify(edcClient).queryContractAgreements();

        assertThat(actual).isNotEmpty();
        assertThat(actual.size()).isEqualTo(1).isEqualTo(expected.size());
        assertThat(actual.get(0).getOfferingDetails().getAssetId()).isEqualTo(EdcClientFake.FAKE_ID);
        assertThat(actual.get(0).getProviderDetails().getName()).isEqualTo("Unknown");
        assertThat(actual.get(0).getConsumerDetails().getName()).isEqualTo("Unknown");
        assertThat(actual.get(0).isProvider()).isFalse();

    }

    @Test
    void testGetContractAgreementsWhenOfferingIsNotInCatalog() throws OfferNotFoundException {

        reset(fhCatalogClient);
        reset(edcClient);

        Mockito.when(fhCatalogClient.getParticipantDetailsByIds(any())).thenReturn(
            Map.of(OmejdnConnectorApiClientFake.PARTICIPANT_ID,
                ParticipantDetailsSparqlQueryResult.builder().name(OmejdnConnectorApiClientFake.PARTICIPANT_NAME)
                    .build()));
        Mockito.when(fhCatalogClient.getOfferingDetailsByAssetIds(any())).thenReturn(new HashMap<>());

        List<ContractAgreementBE> expected = getContractAgreementBEs();
        List<ContractAgreementBE> actual = contractService.getContractAgreements();

        verify(fhCatalogClient).getParticipantDetailsByIds(any());
        verify(fhCatalogClient).getOfferingDetailsByAssetIds(any());
        verify(edcClient).queryContractAgreements();

        assertThat(actual).isNotEmpty();
        assertThat(actual.size()).isEqualTo(1).isEqualTo(expected.size());
        assertThat(actual.get(0).getOfferingDetails().getAssetId()).isEqualTo(EdcClientFake.FAKE_ID);
        assertThat(actual.get(0).getProviderDetails().getName()).isEqualTo("Unknown");
        assertThat(actual.get(0).getConsumerDetails().getName()).isEqualTo("Unknown");
        assertThat(actual.get(0).isProvider()).isFalse();

    }

    @Test
    void testGetContractDetailsByContractAgreementId() throws OfferNotFoundException {

        reset(fhCatalogClient);
        reset(edcClient);

        String serviceName = "test name";
        String serviceDescription = "test description";
        PxExtendedServiceOfferingCredentialSubject pxExtendedServiceOfferingCredentialSubject = PxExtendedServiceOfferingCredentialSubject.builder()
            .aggregationOf(List.of()).name(serviceName).description(serviceDescription).assetId(EdcClientFake.FAKE_ID)
            .build();
        OffsetDateTime offerRetrievalDate = OffsetDateTime.now();
        Mockito.when(fhCatalogClient.getFhCatalogOffer(any()))
            .thenReturn(new OfferRetrievalResponseBE(pxExtendedServiceOfferingCredentialSubject, offerRetrievalDate));

        Mockito.when(fhCatalogClient.getParticipantDetailsByIds(any())).thenReturn(
            Map.of(OmejdnConnectorApiClientFake.PARTICIPANT_ID,
                ParticipantDetailsSparqlQueryResult.builder().name(OmejdnConnectorApiClientFake.PARTICIPANT_NAME)
                    .build()));
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
        assertThat(actual.getProviderDetails().getName()).isEqualTo("Unknown");
        assertThat(actual.getConsumerDetails().getName()).isEqualTo("Unknown");

    }

    @Test
    void testGetOfferDetailsByContractAgreementId() throws OfferNotFoundException {

        reset(fhCatalogClient);
        reset(edcClient);

        PxExtendedServiceOfferingCredentialSubject pxExtendedServiceOfferingCredentialSubject = PxExtendedServiceOfferingCredentialSubject.builder()
            .aggregationOf(List.of()).name("test name").description("test description").assetId(EdcClientFake.FAKE_ID)
            .build();
        OffsetDateTime offerRetrievalDate = OffsetDateTime.now();
        Mockito.when(fhCatalogClient.getFhCatalogOffer(any()))
            .thenReturn(new OfferRetrievalResponseBE(pxExtendedServiceOfferingCredentialSubject, offerRetrievalDate));

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
    void policyValidityAllValid() throws OfferNotFoundException {

        reset(fhCatalogClient);
        reset(edcClient);

        PxExtendedServiceOfferingCredentialSubject pxExtendedServiceOfferingCredentialSubject = PxExtendedServiceOfferingCredentialSubject.builder()
            .aggregationOf(List.of()).name("test name").description("test description").assetId(EdcClientFake.FAKE_ID)
            .build();
        OffsetDateTime offerRetrievalDate = OffsetDateTime.now();
        Mockito.when(fhCatalogClient.getFhCatalogOffer(any()))
            .thenReturn(new OfferRetrievalResponseBE(pxExtendedServiceOfferingCredentialSubject, offerRetrievalDate));

        Mockito.when(fhCatalogClient.getOfferingDetailsByAssetIds(any())).thenReturn(Map.of(EdcClientFake.FAKE_ID,
            OfferingDetailsSparqlQueryResult.builder().assetId(EdcClientFake.FAKE_ID).uri("some uri").build()));

        ContractAgreement contractAgreement = getContractAgreement();
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime signingDate = now.minusDays(7);
        Timestamp signingDateTimestamp = Timestamp.valueOf(
            LocalDateTime.ofInstant(signingDate.toInstant(), ZoneOffset.UTC));
        contractAgreement.setContractSigningDate(BigInteger.valueOf(signingDateTimestamp.getTime()));

        ParticipantRestrictionPolicy participantRestrictionPolicy = ParticipantRestrictionPolicy.builder()
            .allowedParticipants(List.of("did:web:123", "did:web:456", participantId)).build();
        StartAgreementOffsetPolicy startAgreementOffsetPolicy = StartAgreementOffsetPolicy.builder().offsetNumber(5)
            .offsetUnit(AgreementOffsetUnit.DAYS).build();
        EndAgreementOffsetPolicy endAgreementOffsetPolicy = EndAgreementOffsetPolicy.builder().offsetNumber(10)
            .offsetUnit(AgreementOffsetUnit.DAYS).build();
        StartDatePolicy startDatePolicy = StartDatePolicy.builder().date(now.minusDays(3)).build();
        EndDatePolicy endDatePolicy = EndDatePolicy.builder().date(now.plusDays(3)).build();

        List<EnforcementPolicy> policies = List.of(participantRestrictionPolicy, startAgreementOffsetPolicy,
            endAgreementOffsetPolicy, startDatePolicy, endDatePolicy);

        Mockito.when(edcClient.getContractAgreementById(any())).thenReturn(contractAgreement);
        Mockito.when(consumerService.getEnforcementPoliciesFromEdcPolicies(any())).thenReturn(policies);

        ContractDetailsBE actual = contractService.getContractDetailsByContractAgreementId("some id");

        List<EnforcementPolicy> validatedPolicies = actual.getEnforcementPolicies();

        for (EnforcementPolicy p : validatedPolicies) {
            assertTrue(p.isValid());
        }
    }

    @Test
    void policyValidityAllInvalid() throws OfferNotFoundException {

        reset(fhCatalogClient);
        reset(edcClient);

        PxExtendedServiceOfferingCredentialSubject pxExtendedServiceOfferingCredentialSubject = PxExtendedServiceOfferingCredentialSubject.builder()
            .aggregationOf(List.of()).name("test name").description("test description").assetId(EdcClientFake.FAKE_ID)
            .build();
        OffsetDateTime offerRetrievalDate = OffsetDateTime.now();
        Mockito.when(fhCatalogClient.getFhCatalogOffer(any()))
            .thenReturn(new OfferRetrievalResponseBE(pxExtendedServiceOfferingCredentialSubject, offerRetrievalDate));

        Mockito.when(fhCatalogClient.getOfferingDetailsByAssetIds(any())).thenReturn(Map.of(EdcClientFake.FAKE_ID,
            OfferingDetailsSparqlQueryResult.builder().assetId(EdcClientFake.FAKE_ID).uri("some uri").build()));

        ContractAgreement contractAgreement = getContractAgreement();
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime signingDate = now.minusDays(7);
        Timestamp signingDateTimestamp = Timestamp.valueOf(
            LocalDateTime.ofInstant(signingDate.toInstant(), ZoneOffset.UTC));
        contractAgreement.setContractSigningDate(BigInteger.valueOf(signingDateTimestamp.getTime()));

        ParticipantRestrictionPolicy participantRestrictionPolicy = ParticipantRestrictionPolicy.builder()
            .allowedParticipants(List.of("garbage")).build();
        StartAgreementOffsetPolicy startAgreementOffsetPolicy = StartAgreementOffsetPolicy.builder().offsetNumber(10)
            .offsetUnit(AgreementOffsetUnit.DAYS).build();
        EndAgreementOffsetPolicy endAgreementOffsetPolicy = EndAgreementOffsetPolicy.builder().offsetNumber(5)
            .offsetUnit(AgreementOffsetUnit.DAYS).build();
        StartDatePolicy startDatePolicy = StartDatePolicy.builder().date(now.plusDays(3)).build();
        EndDatePolicy endDatePolicy = EndDatePolicy.builder().date(now.minusDays(3)).build();

        List<EnforcementPolicy> policies = List.of(participantRestrictionPolicy, startAgreementOffsetPolicy,
            endAgreementOffsetPolicy, startDatePolicy, endDatePolicy);

        Mockito.when(edcClient.getContractAgreementById(any())).thenReturn(contractAgreement);
        Mockito.when(consumerService.getEnforcementPoliciesFromEdcPolicies(any())).thenReturn(policies);

        ContractDetailsBE actual = contractService.getContractDetailsByContractAgreementId("some id");

        List<EnforcementPolicy> validatedPolicies = actual.getEnforcementPolicies();

        for (EnforcementPolicy p : validatedPolicies) {
            assertFalse(p.isValid());
        }
    }

    @Test
    void transferDataOfferAgain() throws OfferNotFoundException, TransferFailedException {
        //GIVEN
        reset(fhCatalogClient);
        reset(consumerService);

        TransferOfferRequestBE request = TransferOfferRequestBE.builder().edcOfferId(EdcClientFake.FAKE_ID)
            .contractAgreementId(EdcClientFake.VALID_CONTRACT_AGREEEMENT_ID).build();
        TransferOfferResponseBE response = TransferOfferResponseBE.builder()
            .transferProcessState(TransferProcessState.COMPLETED).build();
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

    private List<ContractAgreementBE> getContractAgreementBEs() {

        ContractAgreement contractAgreement = getContractAgreement();

        ContractAgreementBE contractAgreementBE = ContractAgreementBE.builder().contractAgreement(contractAgreement)
            .offeringDetails(OfferingDetailsBE.builder().name("name").description("description").build())
            .consumerDetails(
                ParticipantWithDapsBE.builder().name(OmejdnConnectorApiClientFake.PARTICIPANT_NAME).build())
            .providerDetails(
                ParticipantWithDapsBE.builder().name(OmejdnConnectorApiClientFake.PARTICIPANT_NAME).build())
            .isDataOffering(false).build();

        return List.of(contractAgreementBE);
    }

    private PossibleAsset getPossibleAsset(String assetId) {

        PossibleAssetDataAccountExport dataAccountExport = PossibleAssetDataAccountExport.builder()
            .accessType("digital").requestType("API").formatType("application/json").build();

        PossibleAssetTnC assetTnC = PossibleAssetTnC.builder().url("https://example.com").hash("hash1234").build();

        PossibleAssetProperties properties = PossibleAssetProperties.builder().termsAndConditions(List.of(assetTnC))
            .producedBy(new NodeKindIRITypeId(EdcClientFake.FAKE_ID))
            .providedBy(new NodeKindIRITypeId(EdcClientFake.FAKE_ID)).license(List.of("MIT"))
            .copyrightOwnedBy(new NodeKindIRITypeId(EdcClientFake.FAKE_ID))
            .exposedThrough(new NodeKindIRITypeId(EdcClientFake.FAKE_ID)).offerId(EdcClientFake.FAKE_ID).name("name")
            .description("description").dataAccountExport(List.of(dataAccountExport)).build();

        Map<String, String> context = Map.of("edc", "https://w3id.org/edc/v0.0.1/ns/", "odrl",
            "http://www.w3.org/ns/odrl/2/", "@vocab", "https://w3id.org/edc/v0.0.1/ns/");

        IonosS3DataSource dataAddress = IonosS3DataSource.builder().bucketName("bucket").blobName("name")
            .keyName("name").region("storage").build();

        return PossibleAsset.builder().id(assetId).type("Asset").properties(properties).context(context)
            .dataAddress(dataAddress).build();
    }

    private ContractAgreement getContractAgreement() {

        return ContractAgreement.builder().contractSigningDate(BigInteger.valueOf(1728549145)).id(EdcClientFake.FAKE_ID)
            .assetId(EdcClientFake.FAKE_ID).consumerId(OmejdnConnectorApiClientFake.PARTICIPANT_ID)
            .providerId(OmejdnConnectorApiClientFake.PARTICIPANT_ID)
            .policy(Policy.builder().target(PolicyTarget.builder().id(EdcClientFake.FAKE_ID).build()).build()).build();
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
        public FhCatalogClient fhCatalogClient() {

            return Mockito.spy(new FhCatalogClientFake());
        }

        @Bean
        public OmejdnConnectorApiClient omejdnConnectorApiClient() {

            return Mockito.spy(new OmejdnConnectorApiClientFake());
        }
    }

}