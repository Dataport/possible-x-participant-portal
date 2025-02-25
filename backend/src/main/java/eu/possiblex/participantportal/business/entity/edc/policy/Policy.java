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
 * - Dataport (part of the POSSIBLE project) - 26 August, 2024 - Add CONTEXT attribute, replace String with JsonNode
 */

package eu.possiblex.participantportal.business.entity.edc.policy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import eu.possiblex.participantportal.business.entity.common.JsonLdConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Policy {

    private static final String TYPE = JsonLdConstants.ODRL_PREFIX + "Set";

    private static final String CONTEXT = JsonLdConstants.POLICY_CONTEXT;

    @Schema(description = "Policy ID", example = "9ca628fb-515a-44ff-90e3-39c34ef4e912")
    @JsonProperty("@id")
    private String id;

    @Schema(description = "List of ODRL permissions as retrieved from the EDC")
    @Builder.Default
    @JsonProperty(JsonLdConstants.ODRL_PREFIX + "permission")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<OdrlPermission> permission = new ArrayList<>();

    @Schema(description = "List of ODRL prohibitions as retrieved from the EDC", example = "[]")
    @Builder.Default
    @JsonProperty(JsonLdConstants.ODRL_PREFIX + "prohibition")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<JsonNode> prohibition = new ArrayList<>(); // replace this with proper classes once needed

    @Schema(description = "List of ODRL obligations as retrieved from the EDC", example = "[]")
    @Builder.Default
    @JsonProperty(JsonLdConstants.ODRL_PREFIX + "obligation")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<JsonNode> obligation = new ArrayList<>(); // replace this with proper classes once needed

    @Schema(description = "Policy target")
    @JsonProperty(JsonLdConstants.ODRL_PREFIX + "target")
    private PolicyTarget target;

    @Schema(description = "JSON-LD type", example = "odrl:Set")
    @JsonProperty("@type")
    public String getType() {

        return TYPE;
    }

    @Schema(description = "JSON-LD context", example = "http://www.w3.org/ns/odrl.jsonld")
    @JsonProperty("@context")
    public String getContext() {

        return CONTEXT;
    }

}
