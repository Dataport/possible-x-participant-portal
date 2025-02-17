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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.possiblex.participantportal.business.entity.common.JsonLdConstants;
import eu.possiblex.participantportal.business.entity.serialization.OdrlOperatorDeserializer;
import eu.possiblex.participantportal.business.entity.serialization.OdrlOperatorSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OdrlConstraint {

    private static final String TYPE = "AtomicConstraint";

    @JsonProperty(JsonLdConstants.ODRL_PREFIX + "leftOperand")
    private String leftOperand;

    @JsonSerialize(using = OdrlOperatorSerializer.class)
    @JsonDeserialize(using = OdrlOperatorDeserializer.class)
    @JsonProperty(JsonLdConstants.ODRL_PREFIX + "operator")
    private OdrlOperator operator;

    @JsonProperty(JsonLdConstants.ODRL_PREFIX + "rightOperand")
    private String rightOperand; // technically this can be any object but the EDC only supports strings

    @JsonProperty("@type")
    public String getType() {

        return TYPE;
    }
}
