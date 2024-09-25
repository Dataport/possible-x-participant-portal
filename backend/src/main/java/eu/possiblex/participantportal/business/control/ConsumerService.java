package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.AcceptOfferResponseBE;
import eu.possiblex.participantportal.business.entity.ConsumeOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferResponseBE;
import eu.possiblex.participantportal.business.entity.exception.NegotiationFailedException;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.TransferFailedException;

public interface ConsumerService {
    /**
     * Given a request for an offer, select it and return the details of this offer from the data transfer component.
     *
     * @param request request for selecting the offer
     * @exception OfferNotFoundException could not find the offer from the request
     * @return details of the offer
     */
    SelectOfferResponseBE selectContractOffer(SelectOfferRequestBE request) throws OfferNotFoundException;

    /**
     * Given a request for an offer, accept the offer on the data transfer component and perform the transfer.
     *
     * @param request request for accepting the offer
     * @exception OfferNotFoundException could not find the offer from the request
     * @exception NegotiationFailedException failed to negotiate over the offer
     * @exception TransferFailedException failed to transfer the data
     * @return final result of the transfer
     */
    AcceptOfferResponseBE acceptContractOffer(ConsumeOfferRequestBE request) throws OfferNotFoundException, NegotiationFailedException, TransferFailedException;
}
