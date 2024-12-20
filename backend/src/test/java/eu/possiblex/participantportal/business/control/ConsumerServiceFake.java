package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import eu.possiblex.participantportal.business.entity.*;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDataset;
import eu.possiblex.participantportal.business.entity.edc.negotiation.NegotiationState;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcessState;
import eu.possiblex.participantportal.business.entity.exception.NegotiationFailedException;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.TransferFailedException;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ConsumerServiceFake implements ConsumerService {

    public static final String VALID_FH_OFFER_ID = "validFhCatalogOfferId";

    public static final String VALID_EDC_OFFER_ID = "validEdcCatalogOfferId";

    public static final String BAD_EDC_OFFER_ID = "badEdcCatalogOfferId";

    public static final String VALID_ASSET_ID = "validAssetId";

    public static final String MISSING_OFFER_ID = "missingOfferFhCatalogOfferId";

    public static final String BAD_TRANSFER_OFFER_ID = "badTransfer";

    public static final String VALID_COUNTER_PARTY_ADDRESS = "some provider EDC URL";

    public static final String FAKE_DID = "did:web:123";

    public static final String FAKE_EMAIL_ADDRESS = "example@mail.com";

    @Override
    public SelectOfferResponseBE selectContractOffer(SelectOfferRequestBE request) throws OfferNotFoundException {

        if (request.getFhCatalogOfferId().equals(MISSING_OFFER_ID)) {
            throw new OfferNotFoundException("not found");
        }

        DcatDataset edcCatalogOfferMock = DcatDataset.builder().assetId(VALID_ASSET_ID).name("some name")
            .description("some description").version("v1.2.3").contenttype("application/json").hasPolicy(List.of(
                Policy.builder().permission(Collections.emptyList()).prohibition(Collections.emptyList())
                    .obligation(Collections.emptyList()).build())).build();

        SelectOfferResponseBE response = new SelectOfferResponseBE();
        response.setEdcOffer(edcCatalogOfferMock);
        PxExtendedServiceOfferingCredentialSubject cs = new PxExtendedServiceOfferingCredentialSubject();
        cs.setProviderUrl(VALID_COUNTER_PARTY_ADDRESS);
        response.setCatalogOffering(cs);
        response.setProviderDetails(ParticipantWithMailBE.builder().did(FAKE_DID).mailAddress(FAKE_EMAIL_ADDRESS).build());
        response.setOfferRetrievalDate(OffsetDateTime.now());

        return response;
    }

    @Override
    public AcceptOfferResponseBE acceptContractOffer(ConsumeOfferRequestBE request)
        throws OfferNotFoundException, NegotiationFailedException {

        return switch (request.getEdcOfferId()) {
            case MISSING_OFFER_ID -> throw new OfferNotFoundException("not found");
            case BAD_EDC_OFFER_ID -> throw new NegotiationFailedException("negotiation failed");
            default ->
                AcceptOfferResponseBE.builder().negotiationState(NegotiationState.FINALIZED).dataOffering(true).build();
        };

    }

    @Override
    public TransferOfferResponseBE transferDataOffer(TransferOfferRequestBE request)
        throws OfferNotFoundException, TransferFailedException {

        return switch (request.getEdcOfferId()) {
            case MISSING_OFFER_ID -> throw new OfferNotFoundException("not found");
            case BAD_TRANSFER_OFFER_ID -> throw new TransferFailedException("transfer failed");
            default -> TransferOfferResponseBE.builder().transferProcessState(TransferProcessState.COMPLETED).build();
        };
    }

    @Override
    public List<EnforcementPolicy> getEnforcementPoliciesFromEdcPolicies(List<Policy> policies) {

        return List.of();
    }
}
