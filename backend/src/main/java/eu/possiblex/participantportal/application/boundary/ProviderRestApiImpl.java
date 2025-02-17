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

import eu.possiblex.participantportal.application.control.ProviderApiMapper;
import eu.possiblex.participantportal.application.entity.*;
import eu.possiblex.participantportal.business.control.ProviderService;
import eu.possiblex.participantportal.business.entity.CreateDataOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing provider-related operations.
 */
@RestController
@Slf4j
public class ProviderRestApiImpl implements ProviderRestApi {

    private final ProviderService providerService;

    private final ProviderApiMapper providerApiMapper;

    /**
     * Constructor for ProviderRestApiImpl.
     *
     * @param providerService the provider service
     * @param providerApiMapper the provider API mapper
     */
    @Autowired
    public ProviderRestApiImpl(ProviderService providerService, ProviderApiMapper providerApiMapper) {

        this.providerService = providerService;
        this.providerApiMapper = providerApiMapper;
    }

    /**
     * POST endpoint to create an offer.
     *
     * @param createServiceOfferingRequestTO the create offering request transfer object
     * @return the response transfer object containing offer IDs
     */
    @Override
    public CreateOfferResponseTO createServiceOffering(
        @RequestBody CreateServiceOfferingRequestTO createServiceOfferingRequestTO) {

        log.info("CreateServiceOfferingRequestTO: {}", createServiceOfferingRequestTO);

        CreateServiceOfferingRequestBE createOfferingRequestBE = providerApiMapper.getCreateOfferingRequestBE(
            createServiceOfferingRequestTO);

        return providerService.createOffering(createOfferingRequestBE);
    }

    /**
     * POST endpoint to create a data offering
     *
     * @param createDataOfferingRequestTO the create offering request transfer object
     * @return create offer response object
     */
    @Override
    public CreateOfferResponseTO createDataOffering(
        @RequestBody CreateDataOfferingRequestTO createDataOfferingRequestTO) {

        log.info("CreateDataOfferingRequestTO: {}", createDataOfferingRequestTO);

        CreateDataOfferingRequestBE createOfferingRequestBE = providerApiMapper.getCreateOfferingRequestBE(
            createDataOfferingRequestTO);

        return providerService.createOffering(createOfferingRequestBE);
    }

    /**
     * GET endpoint to retrieve the prefill fields.
     *
     * @return prefill fields
     */
    @Override
    public PrefillFieldsTO getPrefillFields() {

        return providerApiMapper.getPrefillFieldsTO(providerService.getPrefillFields());
    }
}