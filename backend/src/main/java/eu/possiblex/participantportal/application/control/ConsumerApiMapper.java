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

package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.*;
import eu.possiblex.participantportal.business.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.OffsetDateTime;

@Mapper(componentModel = "spring", imports = { OffsetDateTime.class })
public interface ConsumerApiMapper {

    SelectOfferRequestBE selectOfferRequestTOToBE(SelectOfferRequestTO to);

    ConsumeOfferRequestBE consumeOfferRequestTOToBE(ConsumeOfferRequestTO to);

    TransferOfferRequestBE transferOfferRequestTOToBE(TransferOfferRequestTO to);

    @Mapping(target = "edcOfferId", source = "edcOffer.assetId")
    @Mapping(target = "catalogOffering", source = "catalogOffering")
    @Mapping(target = "dataOffering", source = "dataOffering")
    @Mapping(target = "enforcementPolicies", source = "enforcementPolicies")
    @Mapping(target = "providerDetails", source = "providerDetails")
    @Mapping(target = "offerRetrievalDate", source = "offerRetrievalDate")
    OfferDetailsTO selectOfferResponseBEToOfferDetailsTO(SelectOfferResponseBE selectOfferResponseBE);

    AcceptOfferResponseTO acceptOfferResponseBEToAcceptOfferResponseTO(AcceptOfferResponseBE acceptOfferResponseBE);

    TransferOfferResponseTO transferOfferResponseBEToTransferOfferResponseTO(
        TransferOfferResponseBE transferOfferResponseBE);

    @Mapping(target = "participantId", source = "did")
    @Mapping(target = "participantName", source = "name")
    @Mapping(target = "participantEmail", source = "mailAddress")
    ParticipantDetailsTO participantWithMailBEToParticipantWithMailTO(ParticipantWithMailBE participantWithMailBE);
}
