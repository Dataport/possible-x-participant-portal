package eu.possiblex.participantportal.application.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOfferResponseTO {
    @Schema(description = "Contract definition ID from the EDC")
    private String edcResponseId;

    @Schema(description = "Offering ID from the catalog")
    private String fhResponseId;
}
