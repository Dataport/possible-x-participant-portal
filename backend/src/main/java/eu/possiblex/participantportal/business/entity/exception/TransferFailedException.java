package eu.possiblex.participantportal.business.entity.exception;

import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import lombok.Getter;

import java.util.List;

@Getter
public class TransferFailedException extends RuntimeException {
    private final List<EnforcementPolicy> enforcementPolicyList;

    public TransferFailedException(String message, List<EnforcementPolicy> enforcementPolicyList) {

        super(message);
        this.enforcementPolicyList = enforcementPolicyList;
    }
}
