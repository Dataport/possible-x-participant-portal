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

package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferDetailsTO {
    /**
     * The ID which is used to identify the offer in the EDC Catalog. Currently this is the asset-ID, because an asset
     * will only be used in one offer.
     */
    private String edcOfferId;

    /**
     * The content of the offering as retrieved from the catalog.
     */
    private PxExtendedServiceOfferingCredentialSubject catalogOffering;

    /**
     * Does this offer contain Data Resources.
     */
    private boolean dataOffering;

    /**
     * The enforcement policies for this offer.
     */
    private List<EnforcementPolicy> enforcementPolicies;

    /**
     * The provider details.
     */
    private ParticipantDetailsTO providerDetails;

    /**
     * The timestamp when the offer was retrieved from the catalog.
     */
    private OffsetDateTime offerRetrievalDate;
}
