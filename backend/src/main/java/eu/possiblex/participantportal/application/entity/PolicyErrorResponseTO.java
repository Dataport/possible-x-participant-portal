package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyErrorResponseTO extends ErrorResponseTO {
    private List<EnforcementPolicy> enforcementPolicies;

    public PolicyErrorResponseTO(String message, List<EnforcementPolicy> enforcementPolicies) {

        super(message);
        this.enforcementPolicies = enforcementPolicies;
    }

    public PolicyErrorResponseTO(String message, String details, List<EnforcementPolicy> enforcementPolicies) {

        super(message, details);
        this.enforcementPolicies = enforcementPolicies;
    }

    public PolicyErrorResponseTO(OffsetDateTime date, String message, String details,
        List<EnforcementPolicy> enforcementPolicies) {

        super(date, message, details);
        this.enforcementPolicies = enforcementPolicies;
    }
}
