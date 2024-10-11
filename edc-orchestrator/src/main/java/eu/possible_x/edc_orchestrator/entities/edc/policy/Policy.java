/*
 *  Copyright 2024 Dataport. All rights reserved. Developed as part of the MERLOT project.
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

package eu.possible_x.edc_orchestrator.entities.edc.policy;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.possible_x.edc_orchestrator.entities.edc.EdcConstants;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Policy {

    private static final String TYPE = EdcConstants.ODRL_PREFIX + "Set";

    @JsonProperty("@id")
    private String id;

    @JsonProperty(EdcConstants.ODRL_PREFIX + "permission")
    private List<String> permission; // replace this with proper classes once needed

    @JsonProperty(EdcConstants.ODRL_PREFIX + "prohibition")
    private List<String> prohibition; // replace this with proper classes once needed

    @JsonProperty(EdcConstants.ODRL_PREFIX + "obligation")
    private List<String> obligation; // replace this with proper classes once needed

    @JsonProperty(EdcConstants.ODRL_PREFIX + "target")
    private PolicyTarget target;

    @JsonProperty("@type")
    public String getType() {
        return TYPE;
    }
}
