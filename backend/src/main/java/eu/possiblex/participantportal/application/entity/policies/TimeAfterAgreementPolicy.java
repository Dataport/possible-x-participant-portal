package eu.possiblex.participantportal.application.entity.policies;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.OffsetTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TimeAfterAgreementPolicy extends EnforcementPolicy {
    private OffsetTime offsetTime;
}
