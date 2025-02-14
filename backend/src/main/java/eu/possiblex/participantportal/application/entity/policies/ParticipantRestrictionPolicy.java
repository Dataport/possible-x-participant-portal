package eu.possiblex.participantportal.application.entity.policies;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Schema(description = "Policy that restricts the contractual booking to specific participants", example = """
{
  "@type": "ParticipantRestrictionPolicy",
  "allowedParticipants": ["did:web:example.com:participant:someorgltd"]
}
""")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ParticipantRestrictionPolicy extends EnforcementPolicy {
    public static final String EDC_OPERAND = "did";

    @Schema(description = "List of allowed participants", example = "[\"did:web:example.com:participant:someorgltd\"]")
    private List<String> allowedParticipants;
}
