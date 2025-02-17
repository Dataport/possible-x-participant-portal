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

import eu.possiblex.participantportal.application.entity.ContractAgreementTO;
import eu.possiblex.participantportal.application.entity.ContractDetailsTO;
import eu.possiblex.participantportal.application.entity.OfferWithTimestampTO;
import eu.possiblex.participantportal.business.entity.ContractAgreementBE;
import eu.possiblex.participantportal.business.entity.ContractDetailsBE;
import eu.possiblex.participantportal.business.entity.OfferRetrievalResponseBE;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigInteger;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(componentModel = "spring", imports = { OffsetDateTime.class })
public interface ContractApiMapper {
    @Mapping(target = "id", source = "be.contractAgreement.id")
    @Mapping(target = "assetId", source = "be.contractAgreement.assetId")
    @Mapping(target = "assetDetails", source = "be.offeringDetails")
    @Mapping(target = "policy", source = "be.contractAgreement.policy")
    @Mapping(target = "contractSigningDate", source = "be.contractAgreement.contractSigningDate", qualifiedByName = "secondsToOffsetDateTime")
    @Mapping(target = "consumerDetails", source = "be.consumerDetails")
    @Mapping(target = "providerDetails", source = "be.providerDetails")
    @Mapping(target = "isDataOffering", source = "be.dataOffering")
    @Mapping(target = "isProvider", source = "be.provider")
    @Mapping(target = "enforcementPolicies", source = "be.enforcementPolicies")
    ContractAgreementTO contractAgreementBEToTO(ContractAgreementBE be);

    @Named("secondsToOffsetDateTime")
    default OffsetDateTime secondsToOffsetDateTime(BigInteger seconds) {

        Instant instant = Instant.ofEpochSecond(seconds.longValueExact());
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        return zonedDateTime.toOffsetDateTime();
    }

    @Mapping(target = "id", source = "be.contractAgreement.id")
    @Mapping(target = "assetId", source = "be.contractAgreement.assetId")
    @Mapping(target = "catalogOffering", source = "be.offeringDetails.catalogOffering")
    @Mapping(target = "offerRetrievalDate", source = "be.offeringDetails.offerRetrievalDate")
    @Mapping(target = "policy", source = "be.contractAgreement.policy")
    @Mapping(target = "contractSigningDate", source = "be.contractAgreement.contractSigningDate", qualifiedByName = "secondsToOffsetDateTime")
    @Mapping(target = "consumerDetails", source = "be.consumerDetails")
    @Mapping(target = "providerDetails", source = "be.providerDetails")
    @Mapping(target = "isDataOffering", source = "be.dataOffering")
    @Mapping(target = "enforcementPolicies", source = "be.enforcementPolicies")
    ContractDetailsTO contractDetailsBEToTO(ContractDetailsBE be);

    OfferWithTimestampTO offerRetrievalResponseBEToOfferWithTimestampTO(
        OfferRetrievalResponseBE offerRetrievalResponseBE);
}
