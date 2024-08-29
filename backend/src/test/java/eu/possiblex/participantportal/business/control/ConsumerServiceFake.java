package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.ConsumeOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferRequestBE;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDataset;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.edc.transfer.IonosS3TransferProcess;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcess;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcessState;
import eu.possiblex.participantportal.business.entity.exception.NegotiationFailedException;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.TransferFailedException;

import java.util.Collections;
import java.util.List;

public class ConsumerServiceFake implements ConsumerService {

    public static final String VALID_OFFER_ID = "validOffer";

    public static final String MISSING_OFFER_ID = "missingOffer";

    public static final String BAD_NEGOTIATION_OFFER_ID = "badNegotiation";

    public static final String BAD_TRANSFER_OFFER_ID = "badTransfer";

    @Override
    public DcatDataset selectContractOffer(SelectOfferRequestBE request) throws OfferNotFoundException {

        if(request.getOfferId().equals(MISSING_OFFER_ID)) {
            throw new OfferNotFoundException("not found");
        }

        return DcatDataset
            .builder()
            .assetId(request.getOfferId())
            .name(request.getOfferId())
            .description(request.getOfferId())
            .version("v1.2.3")
            .contenttype("application/json")
            .hasPolicy(List.of(Policy
                .builder()
                .permission(Collections.emptyList())
                .prohibition(Collections.emptyList())
                .obligation(Collections.emptyList())
                .build()))
            .build();
    }

    @Override
    public TransferProcess acceptContractOffer(ConsumeOfferRequestBE request)
        throws OfferNotFoundException, NegotiationFailedException, TransferFailedException {

        return switch (request.getOfferId()) {
            case MISSING_OFFER_ID -> throw new OfferNotFoundException("not found");
            case BAD_NEGOTIATION_OFFER_ID -> throw new NegotiationFailedException("negotiation failed");
            case BAD_TRANSFER_OFFER_ID -> throw new TransferFailedException("transfer failed");
            default -> IonosS3TransferProcess.builder().state(TransferProcessState.COMPLETED).build();
        };

    }
}
