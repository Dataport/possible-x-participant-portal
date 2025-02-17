/*
 *  Copyright 2024 Dataport. All rights reserved. Developed as part of the MERLOT project.
 *  Copyright 2024-2025 Dataport. All rights reserved. Extended as part of the POSSIBLE project.
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
 *
 * Modifications:
 * - Dataport (part of the POSSIBLE project) - 14 August, 2024 - Adjust package names and imports
 */

package eu.possiblex.participantportal.business.entity.edc.contractdefinition;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.possiblex.participantportal.business.entity.common.JsonLdConstants;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@Builder
public class ContractDefinitionCreateRequest {
    private static final Map<String, String> CONTEXT = JsonLdConstants.EDC_CONTEXT;

    private static final String TYPE = "ContractDefinition";

    private List<Criterion> assetsSelector;

    @JsonProperty("@id")
    private String id;

    private String accessPolicyId;

    private String contractPolicyId;

    @JsonProperty("@context")
    public Map<String, String> getContext() {

        return CONTEXT;
    }

    @JsonProperty("@type")
    public String getType() {

        return TYPE;
    }
}
