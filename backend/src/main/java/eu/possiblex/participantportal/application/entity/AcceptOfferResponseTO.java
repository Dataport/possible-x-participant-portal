package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.business.entity.edc.negotiation.NegotiationState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcceptOfferResponseTO {
    @Schema(description = "State of the contract negotiation", example = "FINALIZED")
    private NegotiationState negotiationState;

    @Schema(description = "ID of the contract agreement", example = "db4c8c3a-32c7-4887-b3af-0393721345db")
    private String contractAgreementId;

    @Schema(description = "Flag whether the offering contains data resources or not", example = "true")
    private boolean dataOffering;
}
