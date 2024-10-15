package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.ContractAgreementBE;

import java.util.List;

public interface ContractService {

    /**
     * Get all contract agreements.
     *
     * @return List of contract agreements.
     */
    List<ContractAgreementBE> getContractAgreements();
}
