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
