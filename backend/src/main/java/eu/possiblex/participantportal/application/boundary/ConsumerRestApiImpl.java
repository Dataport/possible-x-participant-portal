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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*") // TODO replace this with proper CORS configuration
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

        SelectOfferRequestBE bo = consumerApiMapper.selectOfferRequestTOtoBO(request);
        DcatDataset dataset = consumerService.selectContractOffer(bo);
        return consumerApiMapper.dcatDatasetToOfferDetailsTo(dataset);
    }

    @Override
    public TransferDetailsTO acceptContractOffer(@RequestBody ConsumeOfferRequestTO request) {

        ConsumeOfferRequestBE bo = consumerApiMapper.consumeOfferRequestTOtoBO(request);
        TransferProcess process = consumerService.acceptContractOffer(bo);
        return consumerApiMapper.transferProcessToDetailsTO(process);
    }
}