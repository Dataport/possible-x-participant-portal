/*
 *  Copyright 2024 Dataport. All rights reserved. Developed as part of the MERLOT project.
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
 *
 * Modifications:
 * - Dataport (part of the POSSIBLE project) - 14 August, 2024 - Adjust package names and imports
 */

package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.common.CommonConstants;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.business.entity.fh.OfferingDetailsQueryResult;
import eu.possiblex.participantportal.business.entity.fh.ParticipantNameQueryResult;
import eu.possiblex.participantportal.business.entity.fh.QueryResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PutExchange;

/**
 * The technical class to access the FH Catalog via REST.
 */
public interface TechnicalFhCatalogClient {

    @PutExchange(CommonConstants.REST_PATH_FH_CATALOG_SERVICE_OFFER)
    FhCatalogIdResponse addServiceOfferingToFhCatalog(
        @RequestBody PxExtendedServiceOfferingCredentialSubject serviceOfferingCs, @RequestParam String id);

    @PutExchange(CommonConstants.REST_PATH_FH_CATALOG_SERVICE_OFFER_WITH_DATA)
    FhCatalogIdResponse addServiceOfferingWithDataToFhCatalog(
            @RequestBody PxExtendedServiceOfferingCredentialSubject serviceOfferingCs, @RequestParam String id);

    @GetExchange("/resources/service-offering/{offering_id}")
    String getFhCatalogOffer(@PathVariable String offering_id);

    @GetExchange("/resources/legal-participant/{participant_id}")
    String getFhCatalogParticipant(@PathVariable String participant_id);

    @GetExchange("/resources/data-product/{offering_id}")
    String getFhCatalogOfferWithData(@PathVariable String offering_id);

    @DeleteExchange("/resources/service-offering/{offeringId}")
    void deleteServiceOfferingFromFhCatalog(@PathVariable String offeringId);

    @DeleteExchange("/resources/data-product/{offeringId}")
    void deleteServiceOfferingWithDataFromFhCatalog(@PathVariable String offeringId);

}

