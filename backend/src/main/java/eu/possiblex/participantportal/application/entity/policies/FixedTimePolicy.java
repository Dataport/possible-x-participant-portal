package eu.possiblex.participantportal.application.entity.policies;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FixedTimePolicy extends EnforcementPolicy {
    private OffsetDateTime endDate;
}
