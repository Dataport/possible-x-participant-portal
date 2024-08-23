package eu.possible_x.backend.application.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possible_x.backend.application.control.ConsumerApiMapper;
import eu.possible_x.backend.application.entity.ConsumeOfferRequestTO;
import eu.possible_x.backend.application.entity.OfferDetailsTO;
import eu.possible_x.backend.application.entity.SelectOfferRequestTO;
import eu.possible_x.backend.application.entity.TransferDetailsTO;
import eu.possible_x.backend.business.control.ConsumerService;
import eu.possible_x.backend.business.entity.ConsumeOfferRequestBE;
import eu.possible_x.backend.business.entity.SelectOfferRequestBE;
import eu.possible_x.backend.business.entity.edc.catalog.DcatDataset;
import eu.possible_x.backend.business.entity.edc.transfer.TransferProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consumer")
@CrossOrigin("*") // TODO replace this with proper CORS configuration
public class ConsumerRestApi {

    private final ConsumerService consumerService;

    private final ObjectMapper objectMapper;

    private final ConsumerApiMapper consumerApiMapper;

    public ConsumerRestApi(@Autowired ConsumerService consumerService, @Autowired ObjectMapper objectMapper,
    @Autowired ConsumerApiMapper consumerApiMapper) {

        this.consumerService = consumerService;
        this.objectMapper = objectMapper;
        this.consumerApiMapper = consumerApiMapper;
    }

    /**
     * POST endpoint to select a Contract Offer
     *
     * @return Data Address of the transferred data
     */
    @PostMapping(value = "/offer/select", produces = MediaType.APPLICATION_JSON_VALUE)
    public OfferDetailsTO selectContractOffer(@RequestBody SelectOfferRequestTO request) {

        SelectOfferRequestBE bo = consumerApiMapper.selectOfferRequestTOtoBE(request);
        DcatDataset dataset = consumerService.selectContractOffer(bo);
        return OfferDetailsTO.builder().edcOffering(objectMapper.valueToTree(dataset)).build();
    }

    /**
     * POST endpoint to accept a Contract Offer
     *
     * @return Data Address of the transferred data
     */
    @PostMapping(value = "/offer/accept", produces = MediaType.APPLICATION_JSON_VALUE)
    public TransferDetailsTO acceptContractOffer(@RequestBody ConsumeOfferRequestTO request) {

        ConsumeOfferRequestBE bo = consumerApiMapper.consumeOfferRequestTOtoBE(request);
        TransferProcess process = consumerService.acceptContractOffer(bo);
        return consumerApiMapper.transferProcessToDetailsTO(process);
    }
}
