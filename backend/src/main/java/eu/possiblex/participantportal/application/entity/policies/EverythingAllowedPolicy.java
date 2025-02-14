package eu.possiblex.participantportal.application.entity.policies;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Schema(description = "Policy that allows everything", example = """
{
  "@type": "EverythingAllowedPolicy"
}
""")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EverythingAllowedPolicy extends EnforcementPolicy {
}
