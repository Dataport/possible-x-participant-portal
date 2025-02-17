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
import eu.possiblex.participantportal.business.entity.daps.OmejdnConnectorDetailsBE;
import eu.possiblex.participantportal.business.entity.edc.catalog.QuerySpec;
import eu.possiblex.participantportal.business.entity.edc.contractagreement.ContractAgreement;
import eu.possiblex.participantportal.business.entity.exception.ContractAgreementNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.fh.OfferingDetailsSparqlQueryResult;
import eu.possiblex.participantportal.business.entity.fh.ParticipantDetailsSparqlQueryResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ContractServiceImpl implements ContractService {

    public static final String UNKNOWN = "Unknown";

    private final EdcClient edcClient;

    private final EnforcementPolicyParserService enforcementPolicyParserService;

    private final FhCatalogClient fhCatalogClient;

    private final OmejdnConnectorApiClient omejdnConnectorApiClient;

    private final String participantId;

    @Value("${fh.catalog.uri-resource-base}")
    private String fhCatalogUriResourceBase;

    public ContractServiceImpl(@Autowired EdcClient edcClient, @Autowired FhCatalogClient fhCatalogClient,
        @Autowired OmejdnConnectorApiClient omejdnConnectorApiClient,
        @Autowired EnforcementPolicyParserService enforcementPolicyParserService,
        @Value("${participant-id}") String participantId) {

        this.edcClient = edcClient;
        this.fhCatalogClient = fhCatalogClient;
        this.omejdnConnectorApiClient = omejdnConnectorApiClient;
        this.enforcementPolicyParserService = enforcementPolicyParserService;
        this.participantId = participantId;
    }

    /**
     * Get all contract agreements.
     *
     * @return List of contract agreements.
     */
    @Override
    public List<ContractAgreementBE> getContractAgreements() {

        List<ContractAgreementBE> contractAgreementBEs = new ArrayList<>();
        List<ContractAgreement> contractAgreements = edcClient.queryContractAgreements(
            QuerySpec.builder().limit(Integer.MAX_VALUE).build());

        if (contractAgreements.isEmpty()) {
            return Collections.emptyList();
        }

        // Get all referenced assetIds from the contracts
        Set<String> referencedAssetIds = contractAgreements.stream().map(ContractAgreement::getAssetId)
            .collect(Collectors.toSet());
        // Get all provider and consumer daps ids from the contracts
        Set<String> refrencedDapsIds = contractAgreements.stream().map(ContractAgreement::getConsumerId)
            .collect(Collectors.toSet());
        refrencedDapsIds.addAll(
            contractAgreements.stream().map(ContractAgreement::getProviderId).collect(Collectors.toSet()));

        // build a map of participant daps ids to dids
        Map<String, String> participantDidMap = getParticipantDids(refrencedDapsIds);

        // build a map of participant dids to participant names
        Map<String, ParticipantDetailsSparqlQueryResult> participantNames = fhCatalogClient.getParticipantDetailsByIds(
            participantDidMap.values());

        // build a map of assetIds to offering details
        Map<String, OfferingDetailsSparqlQueryResult> offeringDetails = fhCatalogClient.getOfferingDetailsByAssetIds(
            referencedAssetIds);
        // prepare for if the did or asset ID is not found
        ParticipantDetailsSparqlQueryResult unknownParticipant = ParticipantDetailsSparqlQueryResult.builder()
            .name(UNKNOWN).build();
        OfferingDetailsSparqlQueryResult unknownOffering = OfferingDetailsSparqlQueryResult.builder().name(UNKNOWN)
            .description(UNKNOWN).uri(UNKNOWN).providerUrl(UNKNOWN).build();

        // convert contract agreements to contract agreement BEs
        contractAgreements.forEach(c -> contractAgreementBEs.add(ContractAgreementBE.builder().contractAgreement(c)
            .isProvider(participantId.equals(participantDidMap.getOrDefault(c.getProviderId(), ""))).isDataOffering(
                StringUtils.hasText(offeringDetails.getOrDefault(c.getAssetId(), unknownOffering).getAggregationOf()))
            .enforcementPolicies(
                enforcementPolicyParserService.getEnforcementPoliciesWithValidity(List.of(c.getPolicy()),
                    c.getContractSigningDate(), participantDidMap.getOrDefault(c.getProviderId(), ""))).offeringDetails(
                OfferingDetailsBE.builder().assetId(c.getAssetId())
                    .offeringId(offeringDetails.getOrDefault(c.getAssetId(), unknownOffering).getUri())
                    .name(offeringDetails.getOrDefault(c.getAssetId(), unknownOffering).getName())
                    .description(offeringDetails.getOrDefault(c.getAssetId(), unknownOffering).getDescription())
                    .providerUrl(offeringDetails.getOrDefault(c.getAssetId(), unknownOffering).getProviderUrl())
                    .build()).consumerDetails(ParticipantWithDapsBE.builder().dapsId(c.getConsumerId())
                .did(participantDidMap.getOrDefault(c.getConsumerId(), "")).name(
                    participantNames.getOrDefault(participantDidMap.getOrDefault(c.getConsumerId(), ""),
                        unknownParticipant).getName()).build()).providerDetails(
                ParticipantWithDapsBE.builder().dapsId(c.getProviderId())
                    .did(participantDidMap.getOrDefault(c.getProviderId(), "")).name(
                        participantNames.getOrDefault(participantDidMap.getOrDefault(c.getProviderId(), ""),
                            unknownParticipant).getName()).build()).build()));

        return contractAgreementBEs;
    }

    @Override
    public ContractDetailsBE getContractDetailsByContractAgreementId(String contractAgreementId) {

        ContractAgreement contractAgreement = getContractAgreementById(contractAgreementId);

        // build a map of consumer and provider daps ids to dids
        Map<String, String> participantDidMap = getParticipantDids(
            List.of(contractAgreement.getConsumerId(), contractAgreement.getProviderId()));

        // build a map of consumer and provider dids to participant names
        Map<String, ParticipantDetailsSparqlQueryResult> participantNames = fhCatalogClient.getParticipantDetailsByIds(
            participantDidMap.values());

        // get the offering details for the asset ID
        OfferRetrievalResponseBE offerRetrievalResponseBE = getOfferRetrievalResponseBE(contractAgreement);

        // prepare for if the did is not found in the map
        ParticipantDetailsSparqlQueryResult unknownParticipant = ParticipantDetailsSparqlQueryResult.builder()
            .name(UNKNOWN).build();

        return ContractDetailsBE.builder().contractAgreement(contractAgreement)
            .isDataOffering(!CollectionUtils.isEmpty(offerRetrievalResponseBE.getCatalogOffering().getAggregationOf()))
            .enforcementPolicies(enforcementPolicyParserService.getEnforcementPoliciesWithValidity(
                List.of(contractAgreement.getPolicy()), contractAgreement.getContractSigningDate(),
                participantDidMap.getOrDefault(contractAgreement.getProviderId(), "")))
            .offeringDetails(offerRetrievalResponseBE).consumerDetails(
                ParticipantWithDapsBE.builder().dapsId(contractAgreement.getConsumerId())
                    .did(participantDidMap.getOrDefault(contractAgreement.getConsumerId(), "")).name(
                        participantNames.getOrDefault(participantDidMap.getOrDefault(contractAgreement.getConsumerId(), ""),
                            unknownParticipant).getName()).build()).providerDetails(
                ParticipantWithDapsBE.builder().dapsId(contractAgreement.getProviderId())
                    .did(participantDidMap.getOrDefault(contractAgreement.getProviderId(), "")).name(
                        participantNames.getOrDefault(participantDidMap.getOrDefault(contractAgreement.getProviderId(), ""),
                            unknownParticipant).getName()).build()).build();

    }

    @Override
    public OfferRetrievalResponseBE getOfferDetailsByContractAgreementId(String contractAgreementId) {

        ContractAgreement contractAgreement = getContractAgreementById(contractAgreementId);
        return getOfferRetrievalResponseBE(contractAgreement);
    }

    private OfferRetrievalResponseBE getOfferRetrievalResponseBE(ContractAgreement contractAgreement) {
        // get the offering details for the asset ID
        Map<String, OfferingDetailsSparqlQueryResult> offeringDetails = fhCatalogClient.getOfferingDetailsByAssetIds(
            List.of(contractAgreement.getAssetId()));

        OfferRetrievalResponseBE offerRetrievalResponseBE;
        PxExtendedServiceOfferingCredentialSubject unknownCatalogOffering = PxExtendedServiceOfferingCredentialSubject.builder()
            .id(UNKNOWN).name(UNKNOWN).description(UNKNOWN).build();

        if (!offeringDetails.containsKey(contractAgreement.getAssetId())) {
            log.warn("No offer found in catalog with referenced assetId: {}", contractAgreement.getAssetId());
            offerRetrievalResponseBE = OfferRetrievalResponseBE.builder().offerRetrievalDate(OffsetDateTime.now())
                .catalogOffering(unknownCatalogOffering).build();
        } else {
            String serviceOfferingUriPrefix = fhCatalogUriResourceBase + "service-offering/";
            String dataProductUriPrefix = fhCatalogUriResourceBase + "data-product/";

            String offeringIdWithoutPrefix = offeringDetails.get(contractAgreement.getAssetId()).getUri()
                .replace(serviceOfferingUriPrefix, "").replace(dataProductUriPrefix, "");

            try {
                offerRetrievalResponseBE = fhCatalogClient.getFhCatalogOffer(offeringIdWithoutPrefix);
            } catch (OfferNotFoundException e) {
                log.warn("No offer found in catalog with id: {}", offeringIdWithoutPrefix);
                offerRetrievalResponseBE = OfferRetrievalResponseBE.builder().offerRetrievalDate(OffsetDateTime.now())
                    .catalogOffering(unknownCatalogOffering).build();
            }
        }
        return offerRetrievalResponseBE;
    }

    private ContractAgreement getContractAgreementById(String contractAgreementId) {

        ContractAgreement contractAgreement;
        try {
            contractAgreement = edcClient.getContractAgreementById(contractAgreementId);
        } catch (WebClientResponseException.NotFound e) {
            log.error("Contract agreement with ID {} not found", contractAgreementId);
            throw new ContractAgreementNotFoundException(
                "Contract agreement with ID " + contractAgreementId + " not found");
        } catch (Exception exception) {
            log.error("Error while fetching contract agreement with ID {}", contractAgreementId, exception);
            throw exception;
        }
        return contractAgreement;
    }

    private Map<String, String> getParticipantDids(Collection<String> participantDapsIds) {

        Map<String, OmejdnConnectorDetailsBE> connectorDetails = Collections.emptyMap();
        if (!participantDapsIds.isEmpty()) {
            connectorDetails = omejdnConnectorApiClient.getConnectorDetails(participantDapsIds);
        }
        Map<String, String> participantDids = new HashMap<>();

        for (String participantDapsId : participantDapsIds) {
            if (!connectorDetails.containsKey(participantDapsId)) {
                log.error("No connector details found for participant with DAPS ID: {}", participantDapsId);
                continue;
            }
            participantDids.put(participantDapsId, connectorDetails.get(participantDapsId).getAttributes().get("did"));
        }

        return participantDids;
    }
}
