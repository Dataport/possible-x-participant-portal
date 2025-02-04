package eu.possiblex.participantportal.application.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumeOfferRequestTO {
    @NotBlank(message = "Counter party address is required")
    private String counterPartyAddress;

    @NotBlank(message = "EDC offer ID is required")
    private String edcOfferId;

    private boolean dataOffering;

}
