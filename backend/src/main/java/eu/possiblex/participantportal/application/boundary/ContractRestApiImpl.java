package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.control.ConsumerApiMapper;
import eu.possiblex.participantportal.application.control.ContractApiMapper;
import eu.possiblex.participantportal.application.entity.ContractAgreementTO;
import eu.possiblex.participantportal.application.entity.TransferOfferRequestTO;
import eu.possiblex.participantportal.application.entity.TransferOfferResponseTO;
import eu.possiblex.participantportal.business.control.ContractService;
import eu.possiblex.participantportal.business.entity.TransferOfferRequestBE;
import eu.possiblex.participantportal.business.entity.TransferOfferResponseBE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

        return contractService.getContractAgreements().stream().map(contractApiMapper::contractAgreementBEToTO)
            .toList();
    }

    @Override
    public TransferOfferResponseTO transferDataOfferAgain(@RequestBody TransferOfferRequestTO request) {

        TransferOfferRequestBE be = consumerApiMapper.transferOfferRequestTOtoBE(request);
        TransferOfferResponseBE responseBE = contractService.transferDataOfferAgain(be);
        TransferOfferResponseTO responseTO = consumerApiMapper.transferOfferResponseBEtoTransferOfferResponseTO(responseBE);
        log.info("Returning for transferring data of contract again: " + responseTO);
        return responseTO;
    }
}
