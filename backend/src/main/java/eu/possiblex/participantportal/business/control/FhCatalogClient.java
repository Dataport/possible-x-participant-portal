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

import eu.possiblex.participantportal.business.entity.OfferRetrievalResponseBE;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubjectSubset;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.business.entity.fh.OfferingDetailsSparqlQueryResult;
import eu.possiblex.participantportal.business.entity.fh.ParticipantDetailsSparqlQueryResult;

import java.util.Collection;
import java.util.Map;

public interface FhCatalogClient {
    /**
     * Add an offer to the FH catalog.
     *
     * @param serviceOfferingCredentialSubject the offer to be added to the FH catalog
     * @param doesContainData true: The offer contains data. false: otherwise
     * @return the ID of the created offer
     */
    FhCatalogIdResponse addServiceOfferingToFhCatalog(
        PxExtendedServiceOfferingCredentialSubject serviceOfferingCredentialSubject, boolean doesContainData);

    OfferRetrievalResponseBE getFhCatalogOffer(String offeringId);

    PxExtendedLegalParticipantCredentialSubjectSubset getFhCatalogParticipant(String participantId);

    /**
     * Delete an offer form the FH catalog.
     *
     * @param offeringId the ID of the offer to be deleted
     * @param doesContainData true: The offer contains data. false: otherwise
     */
    void deleteServiceOfferingFromFhCatalog(String offeringId, boolean doesContainData);

    /**
     * Given the IDs, get selected details of the legal participants.
     *
     * @param participantDids the IDs of the participants
     * @return the details of the participants
     */
    Map<String, ParticipantDetailsSparqlQueryResult> getParticipantDetailsByIds(Collection<String> participantDids);

    /**
     * Get selected details of all legal participants.
     *
     * @return the details of all legal participants
     */
    Map<String, ParticipantDetailsSparqlQueryResult> getParticipantDetails();

    /**
     * Given the referenced asset IDs, get selected details of the offerings with the type
     * "px:PossibleXServiceOfferingExtension".
     *
     * @param assetIds the asset IDs referenced in the offerings
     * @return the details of the service offerings
     */
    Map<String, OfferingDetailsSparqlQueryResult> getOfferingDetailsByAssetIds(Collection<String> assetIds);
}