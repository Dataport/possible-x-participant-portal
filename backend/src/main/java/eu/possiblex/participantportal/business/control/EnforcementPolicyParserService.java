package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;

import java.math.BigInteger;
import java.util.List;

public interface EnforcementPolicyParserService {
    List<EnforcementPolicy> getEnforcementPoliciesFromEdcPolicies(List<Policy> policies);

    List<EnforcementPolicy> getEnforcementPoliciesWithValidity(List<Policy> edcPolicies, BigInteger contractSigningDate,
        String providerDid);

    Policy createEdcPolicyFromEnforcementPolicies(List<EnforcementPolicy> enforcementPolicies);

    Policy getEverythingAllowedPolicy();
}
