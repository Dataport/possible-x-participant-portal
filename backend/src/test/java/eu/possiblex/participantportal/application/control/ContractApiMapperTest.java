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
import eu.possiblex.participantportal.application.entity.policies.EverythingAllowedPolicy;
import eu.possiblex.participantportal.business.entity.*;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.edc.contractagreement.ContractAgreement;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigInteger;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@SpringBootTest
@ContextConfiguration(classes = { ContractApiMapperTest.TestConfig.class })
class ContractApiMapperTest {

    @Autowired
    private ContractApiMapper contractApiMapper;

    @Test
    void mapContractAgreementBEToTO() {

        ContractAgreementBE be = ContractAgreementBE.builder().contractAgreement(
                ContractAgreement.builder().id("contractAgreementId").assetId("assetId")
                    .contractSigningDate(BigInteger.valueOf(Instant.now().toEpochMilli() / 1000)).consumerId("consumerId")
                    .providerId("providerId").policy(Policy.builder().build()).build()).offeringDetails(
                OfferingDetailsBE.builder().name("offeringName").description("offeringDescription").offeringId("offeringId")
                    .assetId("assetId").build())
            .providerDetails(ParticipantWithDapsBE.builder().name("providerName").dapsId("dapsId").did("did").build())
            .consumerDetails(ParticipantWithDapsBE.builder().name("consumer").dapsId("dapsId2").did("did2").build())
            .enforcementPolicies(List.of(new EverythingAllowedPolicy())).isDataOffering(true).isProvider(true).build();

        ContractAgreementTO to = contractApiMapper.contractAgreementBEToTO(be);

        assertEquals(be.getContractAgreement().getId(), to.getId());
        assertEquals(be.getContractAgreement().getPolicy(), to.getPolicy());
        assertEquals(ZonedDateTime.ofInstant(
            Instant.ofEpochSecond(be.getContractAgreement().getContractSigningDate().longValueExact()),
            ZoneId.systemDefault()).toOffsetDateTime(), to.getContractSigningDate());

        assertEquals(be.getOfferingDetails().getOfferingId(), to.getAssetDetails().getOfferingId());
        assertEquals(be.getOfferingDetails().getName(), to.getAssetDetails().getName());
        assertEquals(be.getOfferingDetails().getDescription(), to.getAssetDetails().getDescription());
        assertEquals(be.getOfferingDetails().getAssetId(), to.getAssetDetails().getAssetId());

        assertEquals(be.getConsumerDetails().getDid(), to.getConsumerDetails().getDid());
        assertEquals(be.getConsumerDetails().getDapsId(), to.getConsumerDetails().getDapsId());
        assertEquals(be.getConsumerDetails().getName(), to.getConsumerDetails().getName());

        assertEquals(be.getProviderDetails().getDid(), to.getProviderDetails().getDid());
        assertEquals(be.getProviderDetails().getDapsId(), to.getProviderDetails().getDapsId());
        assertEquals(be.getProviderDetails().getName(), to.getProviderDetails().getName());

        assertEquals(be.isDataOffering(), to.isDataOffering());
        assertEquals(be.isProvider(), to.isProvider());
        assertIterableEquals(be.getEnforcementPolicies(), to.getEnforcementPolicies());
    }

    @Test
    void mapContractDetailsBEToTO() {

        ContractDetailsBE be = ContractDetailsBE.builder().contractAgreement(
                ContractAgreement.builder().id("contractAgreementId").assetId("assetId")
                    .contractSigningDate(BigInteger.valueOf(Instant.now().toEpochMilli() / 1000)).consumerId("consumerId")
                    .providerId("providerId").policy(Policy.builder().build()).build()).offeringDetails(
                OfferRetrievalResponseBE.builder().offerRetrievalDate(OffsetDateTime.now())
                    .catalogOffering(PxExtendedServiceOfferingCredentialSubject.builder().id("id").name("name").build())
                    .build())
            .providerDetails(ParticipantWithDapsBE.builder().name("providerName").dapsId("dapsId").did("did").build())
            .consumerDetails(ParticipantWithDapsBE.builder().name("consumer").dapsId("dapsId2").did("did2").build())
            .enforcementPolicies(List.of(new EverythingAllowedPolicy())).isDataOffering(true).build();

        ContractDetailsTO to = contractApiMapper.contractDetailsBEToTO(be);

        assertEquals(be.getContractAgreement().getId(), to.getId());
        assertEquals(be.getContractAgreement().getAssetId(), to.getAssetId());
        assertThat(be.getOfferingDetails().getCatalogOffering()).usingRecursiveComparison()
            .isEqualTo(to.getCatalogOffering());
        assertEquals(be.getOfferingDetails().getOfferRetrievalDate(), to.getOfferRetrievalDate());
        assertEquals(be.getContractAgreement().getPolicy(), to.getPolicy());
        assertEquals(ZonedDateTime.ofInstant(
            Instant.ofEpochSecond(be.getContractAgreement().getContractSigningDate().longValueExact()),
            ZoneId.systemDefault()).toOffsetDateTime(), to.getContractSigningDate());

        assertEquals(be.getConsumerDetails().getDid(), to.getConsumerDetails().getDid());
        assertEquals(be.getConsumerDetails().getDapsId(), to.getConsumerDetails().getDapsId());
        assertEquals(be.getConsumerDetails().getName(), to.getConsumerDetails().getName());

        assertEquals(be.getProviderDetails().getDid(), to.getProviderDetails().getDid());
        assertEquals(be.getProviderDetails().getDapsId(), to.getProviderDetails().getDapsId());
        assertEquals(be.getProviderDetails().getName(), to.getProviderDetails().getName());

        assertEquals(be.isDataOffering(), to.isDataOffering());
        assertIterableEquals(be.getEnforcementPolicies(), to.getEnforcementPolicies());

    }

    @Test
    void mapOfferRetrievalResponseBEToOfferWithTimestampTO() {

        OfferRetrievalResponseBE be = OfferRetrievalResponseBE.builder()
            .catalogOffering(PxExtendedServiceOfferingCredentialSubject.builder().id("id").name("name").build())
            .offerRetrievalDate(OffsetDateTime.now()).build();

        OfferWithTimestampTO to = contractApiMapper.offerRetrievalResponseBEToOfferWithTimestampTO(be);

        assertThat(be.getCatalogOffering()).usingRecursiveComparison().isEqualTo(to.getCatalogOffering());
        assertEquals(be.getOfferRetrievalDate(), to.getOfferRetrievalDate());
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ContractApiMapper contractApiMapper() {

            return Mappers.getMapper(ContractApiMapper.class);
        }

    }
}
