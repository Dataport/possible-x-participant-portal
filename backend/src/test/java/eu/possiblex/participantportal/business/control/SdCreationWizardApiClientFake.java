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

package eu.possiblex.participantportal.business.control;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SdCreationWizardApiClientFake implements SdCreationWizardApiClient {

    private final Map<String, Map<String, List<String>>> shapesByEcosystem = Map.of("ecosystem1",
        Map.of("Resource", List.of("Resource1.json"), "Service", List.of("Offering1.json", "Offering2.json")),
        "ecosystem2",
        Map.of("Resource", List.of("Resource2.json"), "Service", List.of("Offering3.json", "Offering4.json")));

    private Set<String> getShapeFiles() {

        Set<String> shapeFiles = new HashSet<>();
        for (String ecoSystem : shapesByEcosystem.keySet()) {
            for (String shapeType : shapesByEcosystem.get(ecoSystem).keySet()) {
                shapeFiles.addAll(shapesByEcosystem.get(ecoSystem).get(shapeType));
            }
        }
        return shapeFiles;
    }

    @Override
    public Map<String, List<String>> getAvailableShapesCategorized(String ecosystem) {

        if (!shapesByEcosystem.containsKey(ecosystem)) {
            throw new WebClientResponseException(HttpStatus.NOT_FOUND.value(), "missing", null, null, null);
        }
        return shapesByEcosystem.get(ecosystem);
    }

    @Override
    public String getJSON(String ecosystem, String name) {

        if (!getShapeFiles().contains(name)) {
            throw new WebClientResponseException(HttpStatus.NOT_FOUND.value(), "missing", null, null, null);
        }
        return """
            {
                "someKey": "someValue"
            }
            """;
    }
}
