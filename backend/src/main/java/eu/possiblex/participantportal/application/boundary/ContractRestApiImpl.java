package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.control.ConsumerApiMapper;
import eu.possiblex.participantportal.application.control.ContractApiMapper;
import eu.possiblex.participantportal.application.entity.*;
import eu.possiblex.participantportal.business.control.ContractService;
import eu.possiblex.participantportal.business.entity.TransferOfferRequestBE;
import eu.possiblex.participantportal.business.entity.TransferOfferResponseBE;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.TransferFailedException;
import eu.possiblex.participantportal.utilities.PossibleXException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing contract-related operations.
 */
@RestController
@CrossOrigin("*") // TODO replace this with proper CORS configuration
@Slf4j
public class ContractRestApiImpl implements ContractRestApi {
    private final ContractService contractService;

    private final ContractApiMapper contractApiMapper;

    private final ConsumerApiMapper consumerApiMapper;

    public ContractRestApiImpl(@Autowired ContractService contractService,
        @Autowired ContractApiMapper contractApiMapper, ConsumerApiMapper consumerApiMapper) {

        this.contractService = contractService;
        this.contractApiMapper = contractApiMapper;
        this.consumerApiMapper = consumerApiMapper;
    }

    /**
     * GET request for retrieving all contract agreements.
     *
     * @return list of contract agreements
     */
    @Override
    public List<ContractAgreementTO> getContractAgreements() {
        try {
            return contractService.getContractAgreements().stream().map(contractApiMapper::contractAgreementBEToTO)
                .toList();
        } catch (OfferNotFoundException e) {
            throw new PossibleXException("" + e,
                HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public ContractDetailsTO getContractDetailsByContractAgreementId(String contractAgreementId) {
        try {
            return contractApiMapper.contractDetailsBEToTO(contractService.getContractDetailsByContractAgreementId(
                contractAgreementId));
        } catch (OfferNotFoundException e) {
            throw new PossibleXException("" + e,
                HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public OfferWithTimestampTO getOfferWithTimestampByContractAgreementId(String contractAgreementId) {
       try {
           return contractApiMapper.offerRetrievalResponseBEToOfferWithTimestampTO(
               contractService.getOfferDetailsByContractAgreementId(contractAgreementId));
       } catch (OfferNotFoundException e) {
           throw new PossibleXException("" + e,
               HttpStatus.NOT_FOUND);
       }
    }

    @Override
    public TransferOfferResponseTO transferDataOfferAgain(@RequestBody TransferOfferRequestTO request) {
        TransferOfferRequestBE be = consumerApiMapper.transferOfferRequestTOToBE(request);
        TransferOfferResponseBE responseBE;
        try {
            responseBE = contractService.transferDataOfferAgain(be);
        } catch (OfferNotFoundException e) {
            throw new PossibleXException("" + e,
                HttpStatus.NOT_FOUND);
        } catch (TransferFailedException e) {
            throw new PossibleXException(
                "" + e);
        } catch (Exception e) {
            throw new PossibleXException(
                "" + e);
        }
        TransferOfferResponseTO responseTO = consumerApiMapper.transferOfferResponseBEToTransferOfferResponseTO(responseBE);
        log.info("Returning for transferring data of contract again: " + responseTO);
        return responseTO;
    }
}
