package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.control.ConsumerApiMapper;
import eu.possiblex.participantportal.application.entity.*;
import eu.possiblex.participantportal.business.control.ConsumerService;
import eu.possiblex.participantportal.business.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ConsumerRestApiImpl implements ConsumerRestApi {

    private final ConsumerService consumerService;

    private final ConsumerApiMapper consumerApiMapper;

    public ConsumerRestApiImpl(@Autowired ConsumerService consumerService,
        @Autowired ConsumerApiMapper consumerApiMapper) {

        this.consumerService = consumerService;
        this.consumerApiMapper = consumerApiMapper;
    }

    @Override
    public OfferDetailsTO selectContractOffer(@RequestBody SelectOfferRequestTO request) {

        log.info("selecting contract with {}", request);
        SelectOfferRequestBE be = consumerApiMapper.selectOfferRequestTOToBE(request);
        SelectOfferResponseBE response = consumerService.selectContractOffer(be);

        log.info("returning for selecting contract: {}", response);
        return consumerApiMapper.selectOfferResponseBEToOfferDetailsTO(response);
    }

    @Override
    public AcceptOfferResponseTO acceptContractOffer(@RequestBody ConsumeOfferRequestTO request) {

        log.info("accepting contract with {}", request);
        ConsumeOfferRequestBE be = consumerApiMapper.consumeOfferRequestTOToBE(request);

        AcceptOfferResponseBE acceptOffer = consumerService.acceptContractOffer(be);
        AcceptOfferResponseTO response = consumerApiMapper.acceptOfferResponseBEToAcceptOfferResponseTO(acceptOffer);
        log.info("Returning for accepting contract: {}", response);
        return response;
    }

    @Override
    public TransferOfferResponseTO transferDataOffer(@RequestBody TransferOfferRequestTO request) {

        log.info("transferring data from contract with {}", request);
        TransferOfferRequestBE be = consumerApiMapper.transferOfferRequestTOToBE(request);

        TransferOfferResponseBE transferOfferResponseBE = consumerService.transferDataOffer(be);

        TransferOfferResponseTO response = consumerApiMapper.transferOfferResponseBEToTransferOfferResponseTO(
            transferOfferResponseBE);
        log.info("Returning for transferring data of contract: {}", response);
        return response;
    }
}