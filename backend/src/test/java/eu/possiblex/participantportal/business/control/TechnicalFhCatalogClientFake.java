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

import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.utils.TestUtils;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class TechnicalFhCatalogClientFake implements TechnicalFhCatalogClient {

    public static final String VALID_FH_DATA_OFFER_ID = "validFhCatalogDataOfferId";

    public static final String MISSING_FH_DATA_OFFER_ID = "missingFhCatalogDataOfferId";

    public static final String VALID_FH_SERVICE_OFFER_ID = "validFhCatalogServiceOfferId";

    public static final String MISSING_FH_SERVICE_OFFER_ID = "missingFhCatalogServiceOfferId";

    public static final String VALID_FH_PARTICIPANT_ID = "validFhCatalogParticipantId";

    public static final String MISSING_FH_PARTICIPANT_ID = "missingFhCatalogParticipantId";

    public static final String BAD_PARSING_ID = "badParsingId";

    private final String fhCatalogDataOfferContent;

    private final String fhCatalogServiceOfferContent;

    private final String fhCatalogParticipantContent;

    public TechnicalFhCatalogClientFake() {

        this.fhCatalogDataOfferContent = TestUtils.loadTextFile("unit_tests/ConsumerModuleTest/validFhOffer.json");
        this.fhCatalogServiceOfferContent = TestUtils.loadTextFile(
            "unit_tests/ConsumerModuleTest/validFhOfferWithoutData.json");
        this.fhCatalogParticipantContent = TestUtils.loadTextFile(
            "unit_tests/ConsumerModuleTest/validFhParticipant.json");
    }

    @Override
    public FhCatalogIdResponse addServiceOfferingToFhCatalog(
        PxExtendedServiceOfferingCredentialSubject serviceOfferingCs, String id, String verificationMethod) {

        return new FhCatalogIdResponse(id);
    }

    @Override
    public FhCatalogIdResponse addServiceOfferingWithDataToFhCatalog(
        PxExtendedServiceOfferingCredentialSubject serviceOfferingCs, String id, String verificationMethod) {

        return new FhCatalogIdResponse(id);
    }

    @Override
    public String getFhCatalogOffer(String offeringId) {

        return switch (offeringId) {
            case VALID_FH_SERVICE_OFFER_ID -> this.fhCatalogServiceOfferContent;
            case BAD_PARSING_ID -> "invalid json";
            default -> throw new WebClientResponseException(404, "not found", null, null, null);
        };
    }

    @Override
    public String getFhCatalogParticipant(String participantId) {

        return switch (participantId) {
            case VALID_FH_PARTICIPANT_ID -> this.fhCatalogParticipantContent;
            case BAD_PARSING_ID -> "invalid json";
            default -> throw new WebClientResponseException(404, "not found", null, null, null);
        };
    }

    @Override
    public String getFhCatalogOfferWithData(String offeringId) {

        return switch (offeringId) {
            case VALID_FH_DATA_OFFER_ID -> this.fhCatalogDataOfferContent;
            case BAD_PARSING_ID -> "invalid json";
            default -> throw new WebClientResponseException(404, "not found", null, null, null);
        };
    }

    @Override
    public void deleteServiceOfferingFromFhCatalog(String offeringId) {

        if (offeringId.equals(MISSING_FH_SERVICE_OFFER_ID)) {
            throw WebClientResponseException.create(404, "Offer not found", null, null, null);
        }
    }

    @Override
    public void deleteServiceOfferingWithDataFromFhCatalog(String offeringId) {

        if (offeringId.equals(MISSING_FH_DATA_OFFER_ID)) {
            throw WebClientResponseException.create(404, "Offer not found", null, null, null);
        }
    }
}
