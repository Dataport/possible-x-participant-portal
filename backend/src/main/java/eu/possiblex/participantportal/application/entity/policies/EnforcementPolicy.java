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

package eu.possiblex.participantportal.application.entity.policies;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({ @JsonSubTypes.Type(value = EverythingAllowedPolicy.class, name = "EverythingAllowedPolicy"),
    @JsonSubTypes.Type(value = ParticipantRestrictionPolicy.class, name = "ParticipantRestrictionPolicy"),
    @JsonSubTypes.Type(value = StartDatePolicy.class, name = "StartDatePolicy"),
    @JsonSubTypes.Type(value = EndDatePolicy.class, name = "EndDatePolicy"),
    @JsonSubTypes.Type(value = EndAgreementOffsetPolicy.class, name = "EndAgreementOffsetPolicy"),
})
@EqualsAndHashCode
public abstract class EnforcementPolicy {
    @Schema(description = "Flag whether the policy is valid or not", example = "true")
    private boolean isValid;
}
