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

package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.CreateDataOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.CreateServiceOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.PrefillFieldsTO;
import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import eu.possiblex.participantportal.application.entity.policies.EverythingAllowedPolicy;
import eu.possiblex.participantportal.business.entity.CreateDataOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.PrefillFieldsBE;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProviderApiMapper {

    @Mapping(target = "enforcementPolicies", source = "enforcementPolicies", qualifiedByName = "enforcementPolicyMapper")
    @Mapping(target = "name", source = "serviceOfferingCredentialSubject.name")
    @Mapping(target = "description", source = "serviceOfferingCredentialSubject.description")
    @Mapping(target = "providedBy", source = "serviceOfferingCredentialSubject.providedBy")
    @Mapping(target = "termsAndConditions", source = "serviceOfferingCredentialSubject.termsAndConditions")
    @Mapping(target = "dataProtectionRegime", source = "serviceOfferingCredentialSubject.dataProtectionRegime")
    @Mapping(target = "dataAccountExport", source = "serviceOfferingCredentialSubject.dataAccountExport")
    @Mapping(target = "policy", source = "serviceOfferingCredentialSubject.policy")
    CreateServiceOfferingRequestBE getCreateOfferingRequestBE(
        CreateServiceOfferingRequestTO createServiceOfferingRequestTO);

    @InheritConfiguration
    @Mapping(target = "dataResource", source = "dataResourceCredentialSubject")
    @Mapping(target = "fileName", source = "fileName")
    CreateDataOfferingRequestBE getCreateOfferingRequestBE(CreateDataOfferingRequestTO createDataOfferingRequestTO);

    @Named("enforcementPolicyMapper")
    default List<EnforcementPolicy> enforcementPolicyMapper(List<EnforcementPolicy> enforcementPolicies) {

        if (enforcementPolicies == null || enforcementPolicies.isEmpty()) {
            return List.of(new EverythingAllowedPolicy());
        } else {
            return enforcementPolicies;
        }
    }

    PrefillFieldsTO getPrefillFieldsTO(PrefillFieldsBE prefillFieldsBE);

}
