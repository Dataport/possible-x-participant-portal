package eu.possiblex.participantportal.application.entity.policies;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class TimeAgreementOffsetPolicy extends EnforcementPolicy  {
    public static final String EDC_OPERAND = "https://w3id.org/edc/v0.0.1/ns/inForceDate";

    @Schema(description = "Offset number", example = "1")
    private int offsetNumber;

    @Schema(description = "Offset unit", example = "d")
    private AgreementOffsetUnit offsetUnit;
}
