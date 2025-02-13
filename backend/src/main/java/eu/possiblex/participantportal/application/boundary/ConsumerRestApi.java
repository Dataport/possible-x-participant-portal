package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.entity.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/consumer")
public interface ConsumerRestApi {
    @Operation(summary = "Select an offer", tags = {
        "ConsumeOffer" }, description = "Select an offer and retrieve the offering details for the given catalog offer ID.")
    @PostMapping(value = "/offer/select", produces = MediaType.APPLICATION_JSON_VALUE)
    OfferDetailsTO selectContractOffer(@Valid @RequestBody SelectOfferRequestTO request);

    @Operation(summary = "Accept an offer", tags = {
        "ConsumeOffer" }, description = "Accept an offer and establish a contract agreement with the given counter party and EDC offer ID.")
    @PostMapping(value = "/offer/accept", produces = MediaType.APPLICATION_JSON_VALUE)
    AcceptOfferResponseTO acceptContractOffer(@Valid @RequestBody ConsumeOfferRequestTO request);

    @Operation(summary = "Initiate a data transfer", tags = {
        "ConsumeOffer" }, description = "Initiate a data transfer for an offering with the given counter party, contract agreement and EDC offer ID.")
    @PostMapping(value = "/offer/transfer", produces = MediaType.APPLICATION_JSON_VALUE)
    TransferOfferResponseTO transferDataOffer(@Valid @RequestBody TransferOfferRequestTO request);
}