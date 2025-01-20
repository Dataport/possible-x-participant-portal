package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;

import java.math.BigInteger;
import java.util.List;

public interface EnforcementPolicyParserService {

    /**
     * Given the ODRL Policy stored in the EDC, build the corresponding list of enforcement policies.
     *
     * @param policies ODRL Policies
     * @return enforcement policies
     */
    List<EnforcementPolicy> getEnforcementPoliciesFromEdcPolicies(List<Policy> policies);

    /**
     * Given a list of edc policies, compute their validity based on the contract signing date and the provider DID.
     *
     * @param edcPolicies list of edc policies
     * @param contractSigningDate contract signing date
     * @param providerDid provider DID
     * @return list of enforcement policies with validity
     */
    List<EnforcementPolicy> getEnforcementPoliciesWithValidity(List<Policy> edcPolicies, BigInteger contractSigningDate,
        String providerDid);

    /**
     * Given a list of enforcement policies, convert them to a single policy that can be given to the EDC for
     * evaluation.
     *
     * @param enforcementPolicies list of enforcement policies and their constraints
     * @return edc policy
     */
    Policy getEdcPolicyFromEnforcementPolicies(List<EnforcementPolicy> enforcementPolicies);

    /**
     * Get EDC base policy that can be extended with constraints.
     *
     * @return everything allowed policy
     */
    Policy getEverythingAllowedPolicy();
}
