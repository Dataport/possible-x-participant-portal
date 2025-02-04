package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.entity.*;
import jakarta.validation.Valid;
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
    OfferDetailsTO selectContractOffer(@Valid @RequestBody SelectOfferRequestTO request);

    /**
     * POST endpoint to accept a contract offer
     *
     * @return finalized transfer details
     */
    @PostMapping(value = "/offer/accept", produces = MediaType.APPLICATION_JSON_VALUE)
    AcceptOfferResponseTO acceptContractOffer(@Valid @RequestBody ConsumeOfferRequestTO request);

    /**
     * POST endpoint to trigger a transfer for a contract offer
     *
     * @return finalized transfer details
     */
    @PostMapping(value = "/offer/transfer", produces = MediaType.APPLICATION_JSON_VALUE)
    TransferOfferResponseTO transferDataOffer(@Valid @RequestBody TransferOfferRequestTO request);
}