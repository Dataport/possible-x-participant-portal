package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.*;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.TransferFailedException;

import java.util.List;

public interface ContractService {

    /**
     * Get all contract agreements.
     *
     * @return List of contract agreements.
     */
    List<ContractAgreementBE> getContractAgreements() throws OfferNotFoundException;

    /**
     * Get contract details by id.
     *
     * @param contractAgreementId Contract agreement id.
     * @return Contract details.
     */
    ContractDetailsBE getContractDetailsByContractAgreementId(String contractAgreementId) throws OfferNotFoundException;

    /**
     * Get the referenced offer with timestamp by contract agreement id.
     *
     * @param contractAgreementId Contract agreement id.
     * @return Referenced offer with timestamp.
     */
    OfferRetrievalResponseBE getOfferDetailsByContractAgreementId(String contractAgreementId) throws OfferNotFoundException;

    TransferOfferResponseBE transferDataOfferAgain(TransferOfferRequestBE request) throws OfferNotFoundException,
        TransferFailedException;
}
