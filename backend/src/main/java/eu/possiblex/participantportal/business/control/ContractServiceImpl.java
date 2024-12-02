package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.ContractAgreementBE;
import eu.possiblex.participantportal.business.entity.OfferingDetailsBE;
import eu.possiblex.participantportal.business.entity.ParticipantDetailsBE;
import eu.possiblex.participantportal.business.entity.daps.OmejdnConnectorDetailsBE;
import eu.possiblex.participantportal.business.entity.edc.contractagreement.ContractAgreement;
import eu.possiblex.participantportal.business.entity.fh.OfferingDetailsQueryResult;
import eu.possiblex.participantportal.business.entity.fh.ParticipantNameQueryResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ContractServiceImpl implements ContractService {
    private final EdcClient edcClient;

    private final FhCatalogClient fhCatalogClient;

    private final OmejdnConnectorApiClient omejdnConnectorApiClient;

    public ContractServiceImpl(@Autowired EdcClient edcClient, @Autowired FhCatalogClient fhCatalogClient,
        @Autowired OmejdnConnectorApiClient omejdnConnectorApiClient) {

        this.edcClient = edcClient;
        this.fhCatalogClient = fhCatalogClient;
        this.omejdnConnectorApiClient = omejdnConnectorApiClient;
    }

    /**
     * Get all contract agreements.
     *
     * @return List of contract agreements.
     */
    @Override
    public List<ContractAgreementBE> getContractAgreements() {

        List<ContractAgreementBE> contractAgreementBEs = new ArrayList<>();
        List<ContractAgreement> contractAgreements = edcClient.queryContractAgreements();

        Set<String> referencedAssetIds = contractAgreements.stream().map(ContractAgreement::getAssetId)
            .collect(Collectors.toSet());
        Set<String> referencedConsumerDapsIds = contractAgreements.stream().map(ContractAgreement::getConsumerId)
            .collect(Collectors.toSet());
        Set<String> referencedProviderDapsIds = contractAgreements.stream().map(ContractAgreement::getProviderId)
            .collect(Collectors.toSet());
        Set<String> refrencedDapsIds = new HashSet<>(referencedConsumerDapsIds);
        refrencedDapsIds.addAll(referencedProviderDapsIds);
        Map<String, String> participantDidMap = getParticipantDids(refrencedDapsIds);

        Map<String, ParticipantNameQueryResult> participantNames = fhCatalogClient.getParticipantNames(
            participantDidMap.values());
        Map<String, OfferingDetailsQueryResult> offeringDetails = fhCatalogClient.getOfferingDetails(
            referencedAssetIds);

        ParticipantNameQueryResult unknownParticipant = ParticipantNameQueryResult.builder().name("Unknown").build();
        OfferingDetailsQueryResult unknownOffering = OfferingDetailsQueryResult.builder().name("Unknown")
            .description("Unknown").build();

        contractAgreements.forEach(c -> contractAgreementBEs.add(ContractAgreementBE.builder().contractAgreement(c)
            .offeringDetails(OfferingDetailsBE.builder()
                .name(offeringDetails.getOrDefault(c.getAssetId(), unknownOffering).getName())
                .description(offeringDetails.getOrDefault(c.getAssetId(), unknownOffering).getDescription()).build())
            .consumerDetails(ParticipantDetailsBE.builder().name(
                    participantNames.getOrDefault(participantDidMap.get(c.getConsumerId()), unknownParticipant).getName())
                .build()).providerDetails(ParticipantDetailsBE.builder().name(
                    participantNames.getOrDefault(participantDidMap.get(c.getProviderId()), unknownParticipant).getName())
                .build()).build()));

        return contractAgreementBEs;
    }

    private Map<String, String> getParticipantDids(Collection<String> participantDapsIds) {

        Map<String, OmejdnConnectorDetailsBE> connectorDetails = omejdnConnectorApiClient.getConnectorDetails(
            participantDapsIds);
        Map<String, String> participantDids = new HashMap<>();

        for (String participantDapsId : participantDapsIds) {
            if (!connectorDetails.containsKey(participantDapsId)) {
                log.error("No connector details found for participant with DAPS ID: {}", participantDapsId);
            }
            participantDids.put(participantDapsId, connectorDetails.get(participantDapsId).getAttributes().get("did"));
        }

        return participantDids;
    }
}
