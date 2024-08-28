package eu.possiblex.participantportal.application.boundary;

import com.fasterxml.jackson.databind.JsonNode;
import eu.possiblex.participantportal.application.entity.CreateOfferRequestTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/provider")
public interface ProviderRestApi {
    /**
     * POST endpoint to create an offer
     *
     * @return success message
     */
    @PostMapping(value = "/offer", produces = MediaType.APPLICATION_JSON_VALUE)
    JsonNode createOffer(@RequestBody CreateOfferRequestTO createOfferRequestTO);
}
