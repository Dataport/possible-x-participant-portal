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

import eu.possiblex.participantportal.business.control.SdCreationWizardApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShapeRestApiImpl implements ServiceOfferingShapeRestApi, ResourceShapeRestApi {

    private static final String ECOSYSTEM_GAIAX = "gx";

    private final SdCreationWizardApiService sdCreationWizardApiService;

    public ShapeRestApiImpl(@Autowired SdCreationWizardApiService sdCreationWizardApiService) {

        this.sdCreationWizardApiService = sdCreationWizardApiService;
    }

    /**
     * GET request for retrieving the Gaia-X data resource shape.
     *
     * @return catalog shape
     */
    @Override
    public String getGxDataResourceShape() {

        return sdCreationWizardApiService.getShapeByName(ECOSYSTEM_GAIAX, "Dataresource.json");
    }

    /**
     * GET request for retrieving the Gaia-X instantiated virtual resource shape.
     *
     * @return catalog shape
     */
    @Override
    public String getGxInstantiatedVirtualResourceShape() {

        return sdCreationWizardApiService.getShapeByName(ECOSYSTEM_GAIAX, "Instantiatedvirtualresource.json");
    }

    /**
     * GET request for retrieving the Gaia-X physical resource shape.
     *
     * @return catalog shape
     */
    @Override
    public String getGxPhysicalResourceShape() {

        return sdCreationWizardApiService.getShapeByName(ECOSYSTEM_GAIAX, "Physicalresource.json");
    }

    /**
     * GET request for retrieving the Gaia-X software resource shape.
     *
     * @return catalog shape
     */
    @Override
    public String getGxSoftwareResourceShape() {

        return sdCreationWizardApiService.getShapeByName(ECOSYSTEM_GAIAX, "Softwareresource.json");
    }

    /**
     * GET request for retrieving the Gaia-X virtual resource shape.
     *
     * @return catalog shape
     */
    @Override
    public String getGxVirtualResourceShape() {

        return sdCreationWizardApiService.getShapeByName(ECOSYSTEM_GAIAX, "Virtualresource.json");
    }

    /**
     * GET request for retrieving the Gaia-X legitimate interest shape.
     *
     * @return catalog shape
     */
    @Override
    public String getGxLegitimateInterestShape() {

        return sdCreationWizardApiService.getShapeByName(ECOSYSTEM_GAIAX, "Legitimateinterest.json");
    }

    /**
     * GET request for retrieving the Gaia-X service offering shape.
     *
     * @return catalog shape
     */
    @Override
    public String getGxServiceOfferingShape() {

        return sdCreationWizardApiService.getShapeByName(ECOSYSTEM_GAIAX, "Serviceoffering.json");
    }
}
