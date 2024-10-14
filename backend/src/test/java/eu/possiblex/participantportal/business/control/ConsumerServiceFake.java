package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.AcceptOfferResponseBE;
import eu.possiblex.participantportal.business.entity.ConsumeOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferResponseBE;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDataset;
import eu.possiblex.participantportal.business.entity.edc.negotiation.NegotiationState;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcessState;
import eu.possiblex.participantportal.business.entity.exception.NegotiationFailedException;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.TransferFailedException;

import java.util.Collections;
import java.util.List;

public class ConsumerServiceFake implements ConsumerService {

    public static final String VALID_FH_OFFER_ID = "validFhCatalogOfferId";

    public static final String VALID_EDC_OFFER_ID = "validEdcCatalogOfferId";

    public static final String BAD_EDC_OFFER_ID = "badEdcCatalogOfferId";

    public static final String VALID_ASSET_ID = "validAssetId";

    public static final String MISSING_OFFER_ID = "missingOfferFhCatalogOfferId";

    public static final String BAD_TRANSFER_OFFER_ID = "badTransfer";

    public static final String VALID_COUNTER_PARTY_ADDRESS = "some provider EDC URL";

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

        return response;
    }

    @Override
    public AcceptOfferResponseBE acceptContractOffer(ConsumeOfferRequestBE request)
        throws OfferNotFoundException, NegotiationFailedException, TransferFailedException {

        return switch (request.getEdcOfferId()) {
            case MISSING_OFFER_ID -> throw new OfferNotFoundException("not found");
            case BAD_EDC_OFFER_ID -> throw new NegotiationFailedException("negotiation failed");
            case BAD_TRANSFER_OFFER_ID -> throw new TransferFailedException("transfer failed");
            default -> AcceptOfferResponseBE.builder().transferProcessState(TransferProcessState.COMPLETED)
                .negotiationState(NegotiationState.FINALIZED).dataOffering(true).build();
            //IonosS3TransferProcess.builder().state(TransferProcessState.COMPLETED).build();
        };

    }
}
