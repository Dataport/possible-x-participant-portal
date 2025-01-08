package eu.possiblex.participantportal.application.entity.policies;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ParticipantRestrictionPolicy extends EnforcementPolicy {
    public static final String EDC_OPERAND = "did";
    private List<String> allowedParticipants;
}
