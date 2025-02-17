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

package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.entity.VersionTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/common")
public interface CommonPortalRestApi {
    /**
     * Get the version of the participant portal.
     * @return the version of the participant portal
     */
    @GetMapping(value = "/version", produces = MediaType.APPLICATION_JSON_VALUE)
    VersionTO getVersion();

    /**
     * Get the id to name mapping for all participants that are published in the catalogue.
     * @return the id to name mapping for all participants.
     */
    @GetMapping(value = "/participant/name-mapping", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, String> getNameMapping();
}
