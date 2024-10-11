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

package eu.possible_x.edc_orchestrator.entities.edc;

import java.util.Map;

public class EdcConstants {
    public static final Map<String, String> EDC_CONTEXT = Map.of(
            "@vocab", "https://w3id.org/edc/v0.0.1/ns/",
            "edc", "https://w3id.org/edc/v0.0.1/ns/",
            "odrl", "http://www.w3.org/ns/odrl/2/");
    public static final String ODRL_PREFIX = "odrl:";
    public static final String DCAT_PREFIX = "dcat:";
    public static final String DCT_PREFIX = "dct:";

    private EdcConstants() {}
}
