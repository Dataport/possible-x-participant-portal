/*
 *  Copyright 2024-2025 Dataport. All rights reserved. Developed as part of the POSSIBLE project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
