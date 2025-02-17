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

import eu.possiblex.participantportal.application.entity.*;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/provider")
public interface ProviderRestApi {
    /**
     * POST endpoint to create a service offering
     *
     * @return create offer response object
     */
    @PostMapping(value = "/offer/service", produces = MediaType.APPLICATION_JSON_VALUE)
    CreateOfferResponseTO createServiceOffering(
        @Valid @RequestBody CreateServiceOfferingRequestTO createServiceOfferingRequestTO);

    /**
     * POST endpoint to create a data offering
     *
     * @return create offer response object
     */
    @PostMapping(value = "/offer/data", produces = MediaType.APPLICATION_JSON_VALUE)
    CreateOfferResponseTO createDataOffering(@Valid @RequestBody CreateDataOfferingRequestTO createDataOfferingRequestTO);

    /**
     * GET endpoint to retrieve the prefill fields for providing offers.
     *
     * @return prefill fields
     */
    @GetMapping(value = "/prefillFields", produces = MediaType.APPLICATION_JSON_VALUE)
    PrefillFieldsTO getPrefillFields();
}
