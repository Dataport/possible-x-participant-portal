package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.*;
import eu.possiblex.participantportal.business.entity.exception.NegotiationFailedException;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.ParticipantNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.TransferFailedException;

public interface ConsumerService {
    /**
     * Given a request for an offer, select it and return the details of this offer from the data transfer component.
     *
     * @param request request for selecting the offer
     * @return details of the offer
     * @throws OfferNotFoundException could not find the offer from the request
     */
    SelectOfferResponseBE selectContractOffer(SelectOfferRequestBE request) throws OfferNotFoundException;

    /**
     * Given a request for an offer, accept the offer on the data transfer component and perform the transfer.
     *
     * @param request request for accepting the offer
     * @return final result of the transfer
     * @throws OfferNotFoundException could not find the offer from the request
     * @throws NegotiationFailedException failed to negotiate over the offer
     */
    AcceptOfferResponseBE acceptContractOffer(ConsumeOfferRequestBE request)
        throws OfferNotFoundException, NegotiationFailedException;

    /**
     * Given a request for a transfer, transfer the data using the data transfer component.
     *
     * @param request request for transferring the data
     * @return final result of the transfer
     * @throws OfferNotFoundException could not find the offer from the request
     * @throws TransferFailedException failed to transfer the data
     */
    TransferOfferResponseBE transferDataOffer(TransferOfferRequestBE request)
        throws OfferNotFoundException, TransferFailedException;
}
