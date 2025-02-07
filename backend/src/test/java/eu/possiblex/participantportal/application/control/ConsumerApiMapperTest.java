package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.*;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxSOTermsAndConditions;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.application.entity.policies.EverythingAllowedPolicy;
import eu.possiblex.participantportal.business.entity.*;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDataset;
import eu.possiblex.participantportal.business.entity.edc.negotiation.NegotiationState;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcessState;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@SpringBootTest
@ContextConfiguration(classes = { ConsumerApiMapperTest.TestConfig.class })
class ConsumerApiMapperTest {

    @Autowired
    private ConsumerApiMapper consumerApiMapper;

    @Test
    void mapSelectOfferRequestTOToBE() {

        SelectOfferRequestTO to = SelectOfferRequestTO.builder().fhCatalogOfferId("someCatalogId").build();

        SelectOfferRequestBE be = consumerApiMapper.selectOfferRequestTOToBE(to);

        assertEquals(to.getFhCatalogOfferId(), be.getFhCatalogOfferId());
    }

    @Test
    void mapConsumeOfferRequestTOToBE() {

        ConsumeOfferRequestTO to = ConsumeOfferRequestTO.builder().counterPartyAddress("counterPartyAddress")
            .edcOfferId("edcOfferId").dataOffering(true).build();

        ConsumeOfferRequestBE be = consumerApiMapper.consumeOfferRequestTOToBE(to);

        assertEquals(to.getCounterPartyAddress(), be.getCounterPartyAddress());
        assertEquals(to.getEdcOfferId(), be.getEdcOfferId());
        assertEquals(to.isDataOffering(), be.isDataOffering());
    }

    @Test
    void mapTransferOfferRequestTOToBE() {

        TransferOfferRequestTO to = TransferOfferRequestTO.builder().counterPartyAddress("counterPartyAddress")
            .edcOfferId("edcOfferId").contractAgreementId("contractAgreementId").build();

        TransferOfferRequestBE be = consumerApiMapper.transferOfferRequestTOToBE(to);

        assertEquals(to.getEdcOfferId(), be.getEdcOfferId());
        assertEquals(to.getContractAgreementId(), be.getContractAgreementId());
        assertEquals(to.getCounterPartyAddress(), be.getCounterPartyAddress());
    }

    @Test
    void mapSelectOfferResponseBEToOfferDetailsTO() {

        SelectOfferResponseBE be = SelectOfferResponseBE.builder().dataOffering(true)
            .edcOffer(DcatDataset.builder().assetId("assetId").build()).catalogOffering(
                PxExtendedServiceOfferingCredentialSubject.builder().id("offerId").name("offerName")
                    .description("offerDescription").policy(List.of("policy1", "policy2"))
                    .providedBy(new NodeKindIRITypeId("some-id"))
                    .termsAndConditions(List.of(GxSOTermsAndConditions.builder().url("url").hash("hash").build()))
                    .build()).enforcementPolicies(List.of(new EverythingAllowedPolicy())).providerDetails(
                ParticipantWithMailBE.builder().mailAddress("example@example.com").name("providerName")
                    .did("providerDid").build()).offerRetrievalDate(OffsetDateTime.now()).build();

        OfferDetailsTO to = consumerApiMapper.selectOfferResponseBEToOfferDetailsTO(be);

        assertEquals(be.isDataOffering(), to.isDataOffering());
        assertEquals(be.getEdcOffer().getAssetId(), to.getEdcOfferId());
        assertThat(be.getCatalogOffering()).usingRecursiveComparison().isEqualTo(to.getCatalogOffering());
        assertIterableEquals(be.getEnforcementPolicies(), to.getEnforcementPolicies());
        assertEquals(be.getProviderDetails().getMailAddress(), to.getProviderDetails().getParticipantEmail());
        assertEquals(be.getProviderDetails().getName(), to.getProviderDetails().getParticipantName());
        assertEquals(be.getProviderDetails().getDid(), to.getProviderDetails().getParticipantId());
        assertEquals(be.getOfferRetrievalDate(), to.getOfferRetrievalDate());

    }

    @Test
    void mapAcceptOfferResponseBEToAcceptOfferResponseTO() {

        AcceptOfferResponseBE be = AcceptOfferResponseBE.builder().contractAgreementId("contractAgreementId")
            .dataOffering(true).negotiationState(NegotiationState.FINALIZED).build();

        AcceptOfferResponseTO to = consumerApiMapper.acceptOfferResponseBEToAcceptOfferResponseTO(be);

        assertEquals(be.getContractAgreementId(), to.getContractAgreementId());
        assertEquals(be.isDataOffering(), to.isDataOffering());
        assertEquals(be.getNegotiationState(), to.getNegotiationState());
    }

    @Test
    void mapTransferOfferResponseBEToTransferOfferResponseTO() {

        TransferOfferResponseBE be = TransferOfferResponseBE.builder()
            .transferProcessState(TransferProcessState.COMPLETED).build();

        TransferOfferResponseTO to = consumerApiMapper.transferOfferResponseBEToTransferOfferResponseTO(be);

        assertEquals(be.getTransferProcessState(), to.getTransferProcessState());
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ConsumerApiMapper consumerApiMapper() {

            return Mappers.getMapper(ConsumerApiMapper.class);
        }

    }
}
