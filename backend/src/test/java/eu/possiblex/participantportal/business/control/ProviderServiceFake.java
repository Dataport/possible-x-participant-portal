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
import eu.possiblex.participantportal.business.entity.DataServiceOfferingPrefillFieldsBE;
import eu.possiblex.participantportal.business.entity.PrefillFieldsBE;
import eu.possiblex.participantportal.business.entity.exception.EdcOfferCreationException;
import eu.possiblex.participantportal.business.entity.exception.FhOfferCreationException;
import eu.possiblex.participantportal.business.entity.exception.OfferingComplianceException;

public class ProviderServiceFake implements ProviderService {

    public static final String CREATE_OFFER_RESPONSE_ID = "abc123";

    public static final String PARTICIPANT_ID = "did:web:test.eu";

    public static final String SERVICE_OFFERING_NAME = "Service Offering Name";

    public static final String SERVICE_OFFERING_DESCRIPTION = "Service Offering Description";

    public static final String EDC_OFFER_CREATION_FAILED_NAME = "edc-offer-creation-failed";

    public static final String CATALOG_OFFER_CREATION_FAILED_NAME = "catalog-offer-creation-failed";

    public static final String COMPLIANCE_ERROR_NAME = "compliance-error";

    @Override
    public CreateOfferResponseTO createOffering(CreateServiceOfferingRequestBE requestBE) {

        return switch (requestBE.getName()) {
            case EDC_OFFER_CREATION_FAILED_NAME -> throw new EdcOfferCreationException("EDC offer creation failed");
            case CATALOG_OFFER_CREATION_FAILED_NAME ->
                throw new FhOfferCreationException("Catalog offer creation failed");
            case COMPLIANCE_ERROR_NAME -> throw new OfferingComplianceException("Bad compliance");
            default -> CreateOfferResponseTO.builder().edcResponseId(CREATE_OFFER_RESPONSE_ID)
                .fhResponseId(CREATE_OFFER_RESPONSE_ID).build();
        };
    }

    @Override
    public PrefillFieldsBE getPrefillFields() {

        return new PrefillFieldsBE(PARTICIPANT_ID,
            new DataServiceOfferingPrefillFieldsBE(SERVICE_OFFERING_NAME, SERVICE_OFFERING_DESCRIPTION));
    }
}
