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
 * - Dataport (part of the POSSIBLE project) - 26 August, 2024 - Add POLICY_CONTEXT attribute
 */

package eu.possiblex.participantportal.business.entity.common;

import java.util.Map;

public class JsonLdConstants {

    public static final String EDC_NAMESPACE = "https://w3id.org/edc/v0.0.1/ns/";

    public static final Map<String, String> EDC_CONTEXT = Map.of("@vocab", EDC_NAMESPACE, "edc", EDC_NAMESPACE, "odrl",
        "http://www.w3.org/ns/odrl/2/");

    public static final Map<String, String> FH_CONTEXT = Map.of("skos", "http://www.w3.org/2004/02/skos/core#", "dct",
        "http://purl.org/dc/terms/", "dcat", "http://www.w3.org/ns/dcat#", "rdf",
        "http://www.w3.org/1999/02/22-rdf-syntax-ns#", "foaf", "http://xmlns.com/foaf/0.1/", "edc", EDC_NAMESPACE);

    public static final String POLICY_CONTEXT = "http://www.w3.org/ns/odrl.jsonld";

    public static final String ODRL_PREFIX = "odrl:";

    public static final String DCAT_PREFIX = "dcat:";

    public static final String DCT_PREFIX = "dct:";

    public static final String EDC_PREFIX = "edc:";

    private JsonLdConstants() {

    }
}
