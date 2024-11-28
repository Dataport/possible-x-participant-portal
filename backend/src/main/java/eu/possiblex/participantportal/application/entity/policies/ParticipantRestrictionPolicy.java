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
    private List<String> allowedParticipants;
}
