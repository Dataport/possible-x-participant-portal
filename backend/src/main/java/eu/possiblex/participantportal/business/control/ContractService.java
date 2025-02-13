package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.*;

import java.util.List;

public interface ContractService {

    /**
     * Get all contract agreements.
     *
     * @return List of contract agreements.
     */
    List<ContractAgreementBE> getContractAgreements();

    /**
     * Get contract details by id.
     *
     * @param contractAgreementId Contract agreement id.
     * @return Contract details.
     */
    ContractDetailsBE getContractDetailsByContractAgreementId(String contractAgreementId);

    /**
     * Get the referenced offer with timestamp by contract agreement id.
     *
     * @param contractAgreementId Contract agreement id.
     * @return Referenced offer with timestamp.
     */
    OfferRetrievalResponseBE getOfferDetailsByContractAgreementId(String contractAgreementId);
}
