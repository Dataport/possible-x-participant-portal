package eu.possiblex.participantportal.application.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferOfferRequestTO {
    @Schema(description = "ID of the contract agreement related to an offering containing the data to be transferred", example = "8d3c927a-9bb7-4bc8-a3e7-4f9c9a57d571")
    @NotBlank(message = "Contract agreement ID is required")
    private String contractAgreementId;

    @Schema(description = "EDC protocol URL of the offering provider", example = "https://edc.someorg.com/protocol")
    @NotBlank(message = "Counter party address is required")
    private String counterPartyAddress;

    @Schema(description = "The ID which is used to identify the offering in the EDC catalog. Currently, this is the asset ID, because an asset will only be used in one offering.", example = "8d3c927a-9bb7-4bc8-a3e7-4f9c9a57d571")
    @NotBlank(message = "EDC offer ID is required")
    private String edcOfferId;
}
