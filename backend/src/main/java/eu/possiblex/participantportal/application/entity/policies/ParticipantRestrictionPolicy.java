package eu.possiblex.participantportal.application.entity.policies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantRestrictionPolicy extends EnforcementPolicy {
    private List<String> allowedParticipants;
}
