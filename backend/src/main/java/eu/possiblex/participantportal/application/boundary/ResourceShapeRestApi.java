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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/shapes")
public interface ResourceShapeRestApi {
    /**
     * GET request for retrieving the Gaia-X data resource shape.
     *
     * @return catalog shape
     */
    @GetMapping("/gx/resource/dataresource")
    String getGxDataResourceShape();

    /**
     * GET request for retrieving the Gaia-X instantiated virtual resource shape.
     *
     * @return catalog shape
     */
    @GetMapping("/gx/resource/instantiatedvirtualresource")
    String getGxInstantiatedVirtualResourceShape();

    /**
     * GET request for retrieving the Gaia-X physical resource shape.
     *
     * @return catalog shape
     */
    @GetMapping("/gx/resource/physicalresource")
    String getGxPhysicalResourceShape();

    /**
     * GET request for retrieving the Gaia-X software resource shape.
     *
     * @return catalog shape
     */
    @GetMapping("/gx/resource/softwareresource")
    String getGxSoftwareResourceShape();

    /**
     * GET request for retrieving the Gaia-X virtual resource shape.
     *
     * @return catalog shape
     */
    @GetMapping("/gx/resource/virtualresource")
    String getGxVirtualResourceShape();

    /**
     * GET request for retrieving the Gaia-X legitimate interest shape.
     *
     * @return catalog shape
     */
    @GetMapping("/gx/resource/legitimateinterest")
    String getGxLegitimateInterestShape();
}
