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

import eu.possiblex.participantportal.application.entity.CreateOfferResponseTO;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.PrefillFieldsBE;

public interface ProviderService {
    /**
     * Given a request for creating an offering in the Fraunhofer catalog and a request for creating an EDC offer,
     * create the service offering and the offer in the EDC catalog.
     *
     * @param request request for creating a service offering
     * @return create offer response object
     */
    CreateOfferResponseTO createOffering(CreateServiceOfferingRequestBE request);

    /**
     * Return prefill fields.
     *
     * @return prefill fields
     */
    PrefillFieldsBE getPrefillFields();
}
