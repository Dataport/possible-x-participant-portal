package eu.possiblex.participantportal.application.entity.policies;

import java.time.OffsetDateTime;

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
public abstract class TimeDatePolicy extends EnforcementPolicy  {
    public static final String EDC_OPERAND = "https://w3id.org/edc/v0.0.1/ns/inForceDate";

    @Schema(description = "Date when the policy is in force", example = "2021-08-01T12:00:00Z")
    private OffsetDateTime date;
}
