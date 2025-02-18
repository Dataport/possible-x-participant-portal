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
    @Schema(description = "Contract definition ID from the EDC", example = "785fbc8d-2e80-4c58-aeb0-3507bca27989")
    private String edcResponseId;

    @Schema(description = "Offering ID from the catalog", example = "urn:uuid:38770270-d122-4e2c-a461-f62e7cd0dbdd")
    private String fhResponseId;
}
