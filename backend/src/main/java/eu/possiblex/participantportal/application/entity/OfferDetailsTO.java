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
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "The ID which is used to identify the offering in the EDC catalog. Currently, this is the asset ID, because an asset will only be used in one offering.",
        example = "8d3c927a-9bb7-4bc8-a3e7-4f9c9a57d571")
    private String edcOfferId;

    @Schema(description = "Offering credential subject as retrieved from the catalog")
    private PxExtendedServiceOfferingCredentialSubject catalogOffering;

    @Schema(description = "Flag whether the offering contains data resources or not", example = "true")
    private boolean dataOffering;

    @Schema(description = "List of enforcement policies for this offering")
    private List<EnforcementPolicy> enforcementPolicies;

    @Schema(description = "Provider details")
    private ParticipantDetailsTO providerDetails;

    @Schema(description = "Date when the offering was retrieved from the catalog", example = "2024-12-31T23:59:59Z")
    private OffsetDateTime offerRetrievalDate;
}
