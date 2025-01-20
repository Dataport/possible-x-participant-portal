package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import eu.possiblex.participantportal.application.entity.policies.EverythingAllowedPolicy;
import eu.possiblex.participantportal.business.entity.edc.policy.OdrlAction;
import eu.possiblex.participantportal.business.entity.edc.policy.OdrlPermission;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;

import java.math.BigInteger;
import java.util.List;

public class EnforcementPolicyParserServiceFake implements EnforcementPolicyParserService {

    @Override
    public List<EnforcementPolicy> getEnforcementPoliciesFromEdcPolicies(List<Policy> policies) {

        return List.of(new EverythingAllowedPolicy());
    }

    @Override
    public List<EnforcementPolicy> getEnforcementPoliciesWithValidity(List<Policy> edcPolicies,
        BigInteger contractSigningDate, String providerDid) {

        return List.of(new EverythingAllowedPolicy());
    }

    @Override
    public Policy getEdcPolicyFromEnforcementPolicies(List<EnforcementPolicy> enforcementPolicies) {

        return getEverythingAllowedPolicy();
    }

    @Override
    public Policy getEverythingAllowedPolicy() {

        return Policy.builder().permission(List.of(OdrlPermission.builder().action(OdrlAction.USE).build(),
            OdrlPermission.builder().action(OdrlAction.TRANSFER).build())).build();
    }
}
