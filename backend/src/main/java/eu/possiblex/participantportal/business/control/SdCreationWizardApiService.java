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

import java.util.List;
import java.util.Map;

public interface SdCreationWizardApiService {
    /**
     * Return the map of shape-type:filename for a given ecosystem.
     *
     * @param ecosystem ecosystem to filter for
     * @return map of filenames as described above
     */
    Map<String, List<String>> getShapesByEcosystem(String ecosystem);

    /**
     * Return a list of service offering shape files for the given ecosystem.
     *
     * @param ecosystem ecosystem to filter for
     * @return list of service offering shape JSON files
     */
    List<String> getServiceOfferingShapesByEcosystem(String ecosystem);

    /**
     * Return a list of participant shape files for the given ecosystem.
     *
     * @param ecosystem ecosystem to filter for
     * @return list of resource shape JSON files
     */
    List<String> getResourceShapesByEcosystem(String ecosystem);

    /**
     * Given a JSON file name, return the corresponding JSON shape file
     *
     * @param ecosystem ecosystem of shape
     * @param jsonName JSON file name
     * @return JSON file
     */
    String getShapeByName(String ecosystem, String jsonName);
}
