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

import eu.possiblex.participantportal.business.entity.*;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.edc.contractagreement.ContractAgreement;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.edc.policy.PolicyTarget;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcessState;
import eu.possiblex.participantportal.business.entity.exception.ContractAgreementNotFoundException;

import java.math.BigInteger;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class ContractServiceFake implements ContractService {

    public static final BigInteger DATE_IN_SECONDS = BigInteger.valueOf(1728549145);

    public static final String FAKE_ID_CONTRACT_AGREEMENT = "FAKE_ID_CONTRACT_AGREEMENT";

    public static final String FAKE_ID_PROVIDER = "FAKE_ID_PROVIDER";

    public static final String FAKE_ID_CONSUMER = "FAKE_ID_CONSUMER";

    public static final String FAKE_ID_ASSET = "FAKE_ID_ASSET";

    public static final String FAKE_ID_OFFERING = "FAKE_ID_OFFERING";

    public static final String FAKE_URL_PROVIDER = "FAKE_URL_PROVIDER";

    public static final String NAME = "NAME";

    public static final String DESCRIPTION = "DESCRIPTION";

    public static final TransferProcessState TRANSFER_PROCESS_STATE = TransferProcessState.COMPLETED;

    public static final String NOT_FOUND_ID = "notFound";

    public static OffsetDateTime getDateAsOffsetDateTime() {

        Instant instant = Instant.ofEpochSecond(DATE_IN_SECONDS.longValueExact());
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        return zonedDateTime.toOffsetDateTime();
    }

    /**
     * Get all contract agreements.
     *
     * @return List of contract agreements.
     */
    @Override
    public List<ContractAgreementBE> getContractAgreements() {

        return getContractAgreementBEs();
    }

    @Override
    public ContractDetailsBE getContractDetailsByContractAgreementId(String contractAgreementId) {

        if (contractAgreementId.equals(NOT_FOUND_ID)) {
            throw new ContractAgreementNotFoundException("not found");
        }

        return getContractDetailsBE();
    }

    @Override
    public OfferRetrievalResponseBE getOfferDetailsByContractAgreementId(String contractAgreementId) {

        if (contractAgreementId.equals(NOT_FOUND_ID)) {
            throw new ContractAgreementNotFoundException("not found");
        }

        return new OfferRetrievalResponseBE(
            PxExtendedServiceOfferingCredentialSubject.builder().name(NAME).description(DESCRIPTION)
                .id(FAKE_ID_OFFERING).assetId(FAKE_ID_ASSET).build(), getDateAsOffsetDateTime());
    }

    private List<ContractAgreementBE> getContractAgreementBEs() {

        ContractAgreement contractAgreement = ContractAgreement.builder().contractSigningDate(DATE_IN_SECONDS)
            .id(FAKE_ID_CONTRACT_AGREEMENT).assetId(FAKE_ID_ASSET).consumerId(FAKE_ID_CONSUMER)
            .providerId(FAKE_ID_PROVIDER)
            .policy(Policy.builder().target(PolicyTarget.builder().id(FAKE_ID_ASSET).build()).build()).build();

        ContractAgreementBE contractAgreementBE = ContractAgreementBE.builder().contractAgreement(contractAgreement)
            .isProvider(false).isDataOffering(false)
            .offeringDetails(new OfferingDetailsBE(NAME, DESCRIPTION, FAKE_ID_ASSET, FAKE_ID_OFFERING, FAKE_URL_PROVIDER))
            .providerDetails(new ParticipantWithDapsBE()).consumerDetails(new ParticipantWithDapsBE()).build();

        return List.of(contractAgreementBE);
    }

    private ContractDetailsBE getContractDetailsBE() {

        ContractAgreement contractAgreement = ContractAgreement.builder().contractSigningDate(DATE_IN_SECONDS)
            .id(FAKE_ID_CONTRACT_AGREEMENT).assetId(FAKE_ID_ASSET).consumerId(FAKE_ID_CONSUMER)
            .providerId(FAKE_ID_PROVIDER)
            .policy(Policy.builder().target(PolicyTarget.builder().id(FAKE_ID_ASSET).build()).build()).build();

        return ContractDetailsBE.builder().contractAgreement(contractAgreement).offeringDetails(
                new OfferRetrievalResponseBE(
                    PxExtendedServiceOfferingCredentialSubject.builder().name(NAME).description(DESCRIPTION)
                        .id(FAKE_ID_OFFERING).assetId(FAKE_ID_ASSET).build(), getDateAsOffsetDateTime()))
            .providerDetails(new ParticipantWithDapsBE()).consumerDetails(new ParticipantWithDapsBE()).build();
    }
}
