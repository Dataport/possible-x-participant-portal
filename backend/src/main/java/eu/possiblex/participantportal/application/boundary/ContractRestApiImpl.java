package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.control.ContractApiMapper;
import eu.possiblex.participantportal.application.entity.*;
import eu.possiblex.participantportal.business.control.ContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing contract-related operations.
 */
@RestController
@Slf4j
public class ContractRestApiImpl implements ContractRestApi {
    private final ContractService contractService;

    private final ContractApiMapper contractApiMapper;

    public ContractRestApiImpl(@Autowired ContractService contractService,
        @Autowired ContractApiMapper contractApiMapper) {

        this.contractService = contractService;
        this.contractApiMapper = contractApiMapper;
    }

    /**
     * GET request for retrieving all contract agreements.
     *
     * @return list of contract agreements
     */
    @Override
    public List<ContractAgreementTO> getContractAgreements() {

        return contractService.getContractAgreements().stream().map(contractApiMapper::contractAgreementBEToTO)
            .toList();
    }

    @Override
    public ContractDetailsTO getContractDetailsByContractAgreementId(String contractAgreementId) {

        return contractApiMapper.contractDetailsBEToTO(
            contractService.getContractDetailsByContractAgreementId(contractAgreementId));

    }

    @Override
    public OfferWithTimestampTO getOfferWithTimestampByContractAgreementId(String contractAgreementId) {

        return contractApiMapper.offerRetrievalResponseBEToOfferWithTimestampTO(
            contractService.getOfferDetailsByContractAgreementId(contractAgreementId));
    }
}
