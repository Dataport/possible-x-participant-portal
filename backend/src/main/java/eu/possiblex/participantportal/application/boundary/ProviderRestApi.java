package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.entity.CreateOfferRequestTO;
import eu.possiblex.participantportal.application.entity.CreateOfferResponseTO;
import eu.possiblex.participantportal.application.entity.ParticipantIdTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/provider")
public interface ProviderRestApi {
    /**
     * POST endpoint to create an offer
     *
     * @return create offer response object
     */
    @PostMapping(value = "/offer", produces = MediaType.APPLICATION_JSON_VALUE)
    CreateOfferResponseTO createOffer(@RequestBody CreateOfferRequestTO createOfferRequestTO);

    /**
     * GET endpoint to retrieve the participant's id
     *
     * @return participant id
     */
    @GetMapping(value = "/id", produces = MediaType.APPLICATION_JSON_VALUE)
    ParticipantIdTO getParticipantId();
}
