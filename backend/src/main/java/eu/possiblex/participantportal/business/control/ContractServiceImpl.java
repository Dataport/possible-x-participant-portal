package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.policies.EndAgreementOffsetPolicy;
import eu.possiblex.participantportal.application.entity.policies.EndDatePolicy;
import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import eu.possiblex.participantportal.application.entity.policies.EverythingAllowedPolicy;
import eu.possiblex.participantportal.application.entity.policies.ParticipantRestrictionPolicy;
import eu.possiblex.participantportal.application.entity.policies.StartAgreementOffsetPolicy;
import eu.possiblex.participantportal.application.entity.policies.StartDatePolicy;
import eu.possiblex.participantportal.application.entity.policies.TimeAgreementOffsetPolicy;
import eu.possiblex.participantportal.business.entity.*;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.daps.OmejdnConnectorDetailsBE;
import eu.possiblex.participantportal.business.entity.edc.catalog.QuerySpec;
import eu.possiblex.participantportal.business.entity.edc.catalog.SortOrder;
import eu.possiblex.participantportal.business.entity.edc.contractagreement.ContractAgreement;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.TransferFailedException;
import eu.possiblex.participantportal.business.entity.fh.OfferingDetailsSparqlQueryResult;
import eu.possiblex.participantportal.business.entity.fh.ParticipantDetailsSparqlQueryResult;
import eu.possiblex.participantportal.utilities.PossibleXException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ContractServiceImpl implements ContractService {
    private final EdcClient edcClient;

    private final ConsumerService consumerService;

    private final FhCatalogClient fhCatalogClient;

    private final OmejdnConnectorApiClient omejdnConnectorApiClient;

    private final String participantId;

    public ContractServiceImpl(@Autowired EdcClient edcClient, @Autowired FhCatalogClient fhCatalogClient,
        @Autowired OmejdnConnectorApiClient omejdnConnectorApiClient, @Autowired ConsumerService consumerService,
        @Value("${participant-id}") String participantId) {

        this.edcClient = edcClient;
        this.fhCatalogClient = fhCatalogClient;
        this.omejdnConnectorApiClient = omejdnConnectorApiClient;
        this.consumerService = consumerService;
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
        List<ContractAgreement> contractAgreements = edcClient.queryContractAgreements(QuerySpec.builder().limit(Integer.MAX_VALUE).build());

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
        String unknown = "Unknown";
        ParticipantDetailsSparqlQueryResult unknownParticipant = ParticipantDetailsSparqlQueryResult.builder()
            .name(unknown).build();
        OfferingDetailsSparqlQueryResult unknownOffering = OfferingDetailsSparqlQueryResult.builder().name(unknown)
            .description(unknown).uri(unknown).build();

        // convert contract agreements to contract agreement BEs
        contractAgreements.forEach(c -> contractAgreementBEs.add(ContractAgreementBE.builder().contractAgreement(c)
            .isProvider(participantId.equals(participantDidMap.getOrDefault(c.getProviderId(), "")))
            .isDataOffering(offeringDetails.getOrDefault(c.getAssetId(), unknownOffering).getAggregationOf() != null)
            .enforcementPolicies(getEnforcementPoliciesWithValidity(
                List.of(c.getPolicy()),
                c.getContractSigningDate(),
                participantDidMap.getOrDefault(c.getProviderId(), "")))
            .offeringDetails(OfferingDetailsBE.builder().assetId(c.getAssetId())
                .offeringId(offeringDetails.getOrDefault(c.getAssetId(), unknownOffering).getUri())
                .name(offeringDetails.getOrDefault(c.getAssetId(), unknownOffering).getName())
                .description(offeringDetails.getOrDefault(c.getAssetId(), unknownOffering).getDescription()).build())
            .consumerDetails(
                ParticipantWithDapsBE.builder().dapsId(c.getConsumerId()).did(participantDidMap.getOrDefault(c.getConsumerId(), ""))
                    .name(participantNames.getOrDefault(participantDidMap.getOrDefault(c.getConsumerId(), ""),
                        unknownParticipant).getName()).build()).providerDetails(
                ParticipantWithDapsBE.builder().dapsId(c.getProviderId()).did(participantDidMap.getOrDefault(c.getProviderId(), ""))
                    .name(participantNames.getOrDefault(participantDidMap.getOrDefault(c.getProviderId(), ""),
                        unknownParticipant).getName()).build()).build()));

        return contractAgreementBEs;
    }

    @Override
    public ContractDetailsBE getContractDetailsByContractAgreementId(String contractAgreementId) {
        ContractAgreement contractAgreement = edcClient.getContractAgreementById(contractAgreementId);

        // build a map of consumer and provider daps ids to dids
        Map<String, String> participantDidMap = getParticipantDids(List.of(contractAgreement.getConsumerId(),
            contractAgreement.getProviderId()));

        // build a map of consumer and provider dids to participant names
        Map<String, ParticipantDetailsSparqlQueryResult> participantNames = fhCatalogClient.getParticipantDetailsByIds(
            participantDidMap.values());

        // get the offering details for the asset ID
        OfferRetrievalResponseBE offerRetrievalResponseBE = getOfferRetrievalResponseBE(contractAgreement);

        // prepare for if the did is not found in the map
        String unknown = "Unknown";
        ParticipantDetailsSparqlQueryResult unknownParticipant = ParticipantDetailsSparqlQueryResult.builder()
            .name(unknown).build();

        return ContractDetailsBE.builder().contractAgreement(contractAgreement)
            .isDataOffering(offerRetrievalResponseBE.getCatalogOffering().getAggregationOf() != null)
            .enforcementPolicies(getEnforcementPoliciesWithValidity(
                List.of(contractAgreement.getPolicy()),
                contractAgreement.getContractSigningDate(),
                participantDidMap.getOrDefault(contractAgreement.getProviderId(), "")))
            .offeringDetails(offerRetrievalResponseBE)
            .consumerDetails(
                ParticipantWithDapsBE.builder().dapsId(contractAgreement.getConsumerId()).did(participantDidMap.getOrDefault(contractAgreement.getConsumerId(), ""))
                    .name(participantNames.getOrDefault(participantDidMap.getOrDefault(contractAgreement.getConsumerId(), ""),
                        unknownParticipant).getName()).build())
            .providerDetails(
                ParticipantWithDapsBE.builder().dapsId(contractAgreement.getProviderId()).did(participantDidMap.getOrDefault(contractAgreement.getProviderId(), ""))
                    .name(participantNames.getOrDefault(participantDidMap.getOrDefault(contractAgreement.getProviderId(), ""),
                        unknownParticipant).getName()).build()).build();

    }

    @Override
    public OfferRetrievalResponseBE getOfferDetailsByContractAgreementId(String contractAgreementId) {
        ContractAgreement contractAgreement = edcClient.getContractAgreementById(contractAgreementId);
        return getOfferRetrievalResponseBE(contractAgreement);
    }

    private List<EnforcementPolicy> getEnforcementPoliciesWithValidity(List<Policy> edcPolicies, BigInteger contractSigningDate, String providerDid) {
        List<EnforcementPolicy> enforcementPolicies = consumerService.getEnforcementPoliciesFromEdcPolicies(edcPolicies);
        computePolicyValidities(
            enforcementPolicies,
            OffsetDateTime.ofInstant(Instant.ofEpochMilli(contractSigningDate.longValue()), ZoneId.systemDefault()),
            providerDid);
        return enforcementPolicies;
    }

    private void computePolicyValidities(List<EnforcementPolicy> policies, OffsetDateTime contractAgreementTime, String providerDid) {
        for (EnforcementPolicy policy : policies) {
            boolean isValid = false;
            OffsetDateTime now = OffsetDateTime.now();
            if (policy instanceof ParticipantRestrictionPolicy participantRestrictionPolicy) {
                isValid = providerDid.equals(participantId) || participantRestrictionPolicy.getAllowedParticipants().contains(participantId);
            } else if (policy instanceof StartDatePolicy startDatePolicy) {
                isValid = startDatePolicy.getDate().isBefore(now);
            } else if (policy instanceof EndDatePolicy endDatePolicy) {
                isValid = endDatePolicy.getDate().isAfter(now);
            } else if (policy instanceof StartAgreementOffsetPolicy startAgreementOffsetPolicy) {
                Duration offset = getOffsetFromTimeAgreementOffsetPolicy(startAgreementOffsetPolicy);
                isValid = contractAgreementTime.plus(offset).isBefore(now);
            } else if (policy instanceof EndAgreementOffsetPolicy endAgreementOffsetPolicy) {
                Duration offset = getOffsetFromTimeAgreementOffsetPolicy(endAgreementOffsetPolicy);
                isValid = contractAgreementTime.plus(offset).isAfter(now);
            } else if (policy instanceof EverythingAllowedPolicy) {
                isValid = true;
            } else {
                log.error("Could not compute validity for unknown policy type: {}", policy.getClass().getName());
            }
            policy.setValid(isValid);
        }
    }

    private Duration getOffsetFromTimeAgreementOffsetPolicy(TimeAgreementOffsetPolicy policy) {
        return switch (policy.getOffsetUnit()) {
            case DAYS -> Duration.ofDays(policy.getOffsetNumber());
            case HOURS -> Duration.ofHours(policy.getOffsetNumber());
            case MINUTES -> Duration.ofMinutes(policy.getOffsetNumber());
            case SECONDS -> Duration.ofSeconds(policy.getOffsetNumber());
        };
    }

    private OfferRetrievalResponseBE getOfferRetrievalResponseBE(ContractAgreement contractAgreement) {
        // get the offering details for the asset ID
        Map<String, OfferingDetailsSparqlQueryResult> offeringDetails = fhCatalogClient.getOfferingDetailsByAssetIds(
            List.of(contractAgreement.getAssetId()));

        OfferRetrievalResponseBE offerRetrievalResponseBE;
        PxExtendedServiceOfferingCredentialSubject unknownCatalogOffering = PxExtendedServiceOfferingCredentialSubject
            .builder().id("Unknown").name("Unknown").description("Unknown").build();

        if(!offeringDetails.containsKey(contractAgreement.getAssetId())) {
            log.warn("No offer found in catalog with referenced assetId: {}", contractAgreement.getAssetId());
            offerRetrievalResponseBE = OfferRetrievalResponseBE.builder().offerRetrievalDate(OffsetDateTime.now())
                .catalogOffering(unknownCatalogOffering).build();
        } else {
            String serviceOfferingUriPrefix = "https://piveau.io/set/resource/service-offering/";
            String dataProductUriPrefix = "https://piveau.io/set/resource/data-product/";

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

    private Map<String, String> getParticipantDids(Collection<String> participantDapsIds) {

        Map<String, OmejdnConnectorDetailsBE> connectorDetails = omejdnConnectorApiClient.getConnectorDetails(
            participantDapsIds);
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

    @Override
    public TransferOfferResponseBE transferDataOfferAgain(TransferOfferRequestBE be)
        throws OfferNotFoundException, TransferFailedException {

        Map<String, OfferingDetailsSparqlQueryResult> offeringDetailsMap = fhCatalogClient.getOfferingDetailsByAssetIds(
            List.of(be.getEdcOfferId()));
        if (offeringDetailsMap.size() > 1) {
            throw new OfferNotFoundException(
                "Multiple offers found in Sparql query result for assetId: " + be.getEdcOfferId());
        }
        String providerUrl;
        OfferingDetailsSparqlQueryResult offeringDetails = offeringDetailsMap.get(be.getEdcOfferId());
        if (offeringDetails == null) {
            throw new OfferNotFoundException(
                "No Data Offering found in Sparql query result for assetId: " + be.getEdcOfferId());
        }
        providerUrl = offeringDetails.getProviderUrl();
        if (providerUrl == null) {
            throw new OfferNotFoundException(
                "Provider URL not found in Sparql query result for assetId: " + be.getEdcOfferId());
        }
        be.setCounterPartyAddress(providerUrl);
        TransferOfferResponseBE transferOfferResponseBE;
        try {
            transferOfferResponseBE = consumerService.transferDataOffer(be);
        } catch (OfferNotFoundException e) {
            throw new OfferNotFoundException(
                "Failed to transfer offer again with offerId" + be.getEdcOfferId() + ". OfferNotFoundException: " + e);
        } catch (TransferFailedException e) {
            throw new TransferFailedException(
                "Failed to transfer offer again with offerId" + be.getEdcOfferId() + ". TransferFailedException: " + e);
        } catch (Exception e) {
            throw new PossibleXException(
                "Failed to transfer offer again with offerId" + be.getEdcOfferId() + ". Other Exception: " + e);
        }
        return transferOfferResponseBE;
    }
}
