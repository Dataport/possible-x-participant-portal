package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.control.ConsumerApiMapper;
import eu.possiblex.participantportal.application.entity.ConsumeOfferRequestTO;
import eu.possiblex.participantportal.application.entity.OfferDetailsTO;
import eu.possiblex.participantportal.application.entity.SelectOfferRequestTO;
import eu.possiblex.participantportal.application.entity.TransferDetailsTO;
import eu.possiblex.participantportal.business.control.ConsumerService;
import eu.possiblex.participantportal.business.entity.ConsumeOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferRequestBE;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDataset;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcess;
import eu.possiblex.participantportal.business.entity.exception.NegotiationFailedException;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.TransferFailedException;
import eu.possiblex.participantportal.utilities.PossibleXException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin("*") // TODO replace this with proper CORS configuration
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

        SelectOfferRequestBE be = consumerApiMapper.selectOfferRequestTOtoBE(request);
        DcatDataset dataset = consumerService.selectContractOffer(be);
        if (dataset == null) {
            throw new PossibleXException("Couldn't select contract offer", HttpStatus.NOT_FOUND);
        }
        return consumerApiMapper.dcatDatasetToOfferDetailsTO(dataset);
    }

    @Override
    public TransferDetailsTO acceptContractOffer(@RequestBody ConsumeOfferRequestTO request) {

        ConsumeOfferRequestBE be = consumerApiMapper.consumeOfferRequestTOtoBE(request);
        TransferProcess process = consumerService.acceptContractOffer(be);
        if (process == null) {
            throw new PossibleXException("Couldn't accept contract offer", HttpStatus.NOT_FOUND);
        }
        return consumerApiMapper.transferProcessToDetailsTO(process);
    }
}