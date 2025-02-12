package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.entity.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/provider")
public interface ProviderRestApi {
    @Operation(summary = "Create an offering", tags = {
        "ProvideOffer" }, description = "Create an offering based on the given input.")
    @PostMapping(value = "/offer/service", produces = MediaType.APPLICATION_JSON_VALUE)
    CreateOfferResponseTO createServiceOffering(
        @RequestBody CreateServiceOfferingRequestTO createServiceOfferingRequestTO);

    @Operation(summary = "Create an offering with data", tags = {
        "ProvideOffer" }, description = "Create an offering which contains data based on the given input.")
    @PostMapping(value = "/offer/data", produces = MediaType.APPLICATION_JSON_VALUE)
    CreateOfferResponseTO createDataOffering(@RequestBody CreateDataOfferingRequestTO createDataOfferingRequestTO);

    @Operation(summary = "Get the prefill fields for providing offerings", tags = {
        "ProvideOffer" }, description = "Get values to help prefill specific fields for providing offerings.")
    @GetMapping(value = "/prefillFields", produces = MediaType.APPLICATION_JSON_VALUE)
    PrefillFieldsTO getPrefillFields();
}
