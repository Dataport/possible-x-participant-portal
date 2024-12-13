package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.ContractAgreementBE;
import eu.possiblex.participantportal.business.entity.ContractDetailsBE;
import eu.possiblex.participantportal.business.entity.TransferOfferRequestBE;
import eu.possiblex.participantportal.business.entity.TransferOfferResponseBE;
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

    TransferOfferResponseBE transferDataOfferAgain(TransferOfferRequestBE request) throws OfferNotFoundException,
        TransferFailedException;
}
