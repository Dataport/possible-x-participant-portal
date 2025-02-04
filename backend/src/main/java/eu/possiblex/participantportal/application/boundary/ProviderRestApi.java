package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.entity.*;
import jakarta.validation.Valid;
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
        @Valid @RequestBody CreateServiceOfferingRequestTO createServiceOfferingRequestTO);

    /**
     * POST endpoint to create a data offering
     *
     * @return create offer response object
     */
    @PostMapping(value = "/offer/data", produces = MediaType.APPLICATION_JSON_VALUE)
    CreateOfferResponseTO createDataOffering(@Valid @RequestBody CreateDataOfferingRequestTO createDataOfferingRequestTO);

    /**
     * GET endpoint to retrieve the prefill fields for providing offers.
     *
     * @return prefill fields
     */
    @GetMapping(value = "/prefillFields", produces = MediaType.APPLICATION_JSON_VALUE)
    PrefillFieldsTO getPrefillFields();
}
