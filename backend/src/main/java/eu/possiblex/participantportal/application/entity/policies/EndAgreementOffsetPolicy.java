package eu.possiblex.participantportal.application.entity.policies;

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
public class EndAgreementOffsetPolicy extends TimeAgreementOffsetPolicy  {
}
