package eu.possiblex.participantportal.application.entity.policies;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Schema(description = "Policy that restricts the transfer period", example = """
{
  "@type": "EndAgreementOffsetPolicy",
  "offsetNumber": 1,
  "offsetUnit": "d"
}
""")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EndAgreementOffsetPolicy extends TimeAgreementOffsetPolicy  {
}
