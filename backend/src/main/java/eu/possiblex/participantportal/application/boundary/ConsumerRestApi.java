package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.entity.ConsumeOfferRequestTO;
import eu.possiblex.participantportal.application.entity.OfferDetailsTO;
import eu.possiblex.participantportal.application.entity.SelectOfferRequestTO;
import eu.possiblex.participantportal.application.entity.TransferDetailsTO;
import eu.possiblex.participantportal.utilities.PossibleXException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/consumer")
public interface ConsumerRestApi {
    /**
     * POST endpoint to select a contract offer
     *
     * @return details of the selected offer
     */
    @PostMapping(value = "/offer/select", produces = MediaType.APPLICATION_JSON_VALUE)
    OfferDetailsTO selectContractOffer(@RequestBody SelectOfferRequestTO request) throws PossibleXException;

    /**
     * POST endpoint to accept a contract offer
     *
     * @return finalized transfer details
     */
    @PostMapping(value = "/offer/accept", produces = MediaType.APPLICATION_JSON_VALUE)
    TransferDetailsTO acceptContractOffer(@RequestBody ConsumeOfferRequestTO request) throws PossibleXException;
}