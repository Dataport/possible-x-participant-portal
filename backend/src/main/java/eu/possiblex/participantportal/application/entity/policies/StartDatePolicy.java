package eu.possiblex.participantportal.application.entity.policies;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Schema(description = "Policy that restricts the contract duration to a start date", example = """
{
  "@type": "StartDatePolicy",
  "date": "2021-08-01T12:00:00Z"
}
""")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StartDatePolicy extends TimeDatePolicy  {
}
