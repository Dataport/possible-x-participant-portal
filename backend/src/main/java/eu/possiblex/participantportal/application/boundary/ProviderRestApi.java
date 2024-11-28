package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.entity.CreateDataOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.CreateOfferResponseTO;
import eu.possiblex.participantportal.application.entity.CreateServiceOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.ParticipantIdTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/provider")
public interface ProviderRestApi {
    /**
     * POST endpoint to create a service offering
     *
     * @return create offer response object
     */
    @PostMapping(value = "/offer/service", produces = MediaType.APPLICATION_JSON_VALUE)
    CreateOfferResponseTO createServiceOffering(
        @RequestBody CreateServiceOfferingRequestTO createServiceOfferingRequestTO);

    /**
     * POST endpoint to create a data offering
     *
     * @return create offer response object
     */
    @PostMapping(value = "/offer/data", produces = MediaType.APPLICATION_JSON_VALUE)
    CreateOfferResponseTO createDataOffering(@RequestBody CreateDataOfferingRequestTO createDataOfferingRequestTO);

}
