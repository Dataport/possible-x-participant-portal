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

package eu.possiblex.participantportal.business.entity.edc.policy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.possiblex.participantportal.business.entity.common.JsonLdConstants;
import eu.possiblex.participantportal.business.entity.serialization.OdrlActionDeserializer;
import eu.possiblex.participantportal.business.entity.serialization.OdrlActionSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OdrlPermission {

    @Schema(description = "Target of the ODRL permission", example = "bec206d7-090c-4192-be49-dadd71fcc881")
    @JsonProperty(JsonLdConstants.ODRL_PREFIX + "target")
    private String target;

    @Schema(description = "ODRL action", example = "http://www.w3.org/ns/odrl/2/use")
    @JsonProperty(JsonLdConstants.ODRL_PREFIX + "action")
    @JsonSerialize(using = OdrlActionSerializer.class)
    @JsonDeserialize(using = OdrlActionDeserializer.class)
    private OdrlAction action;

    @Schema(description = "List of ODRL constraints")
    @Builder.Default
    @JsonProperty(JsonLdConstants.ODRL_PREFIX + "constraint")
    @JsonFormat(with = { JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
        JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED })
    private List<OdrlConstraint> constraint = new ArrayList<>();
}
