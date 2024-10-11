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

package eu.possible_x.edc_orchestrator.entities.edc.catalog;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.possible_x.edc_orchestrator.entities.edc.EdcConstants;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class DcatDistribution {
    @JsonProperty("@type")
    private String type;

    @JsonProperty(EdcConstants.DCT_PREFIX + "format")
    private Map<String, String> format;

    @JsonProperty(EdcConstants.DCAT_PREFIX + "accessService")
    private String accessService;
}
