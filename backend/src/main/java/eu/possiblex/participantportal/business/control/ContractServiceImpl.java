package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.ContractAgreementBE;
import eu.possiblex.participantportal.business.entity.OfferingDetailsBE;
import eu.possiblex.participantportal.business.entity.ParticipantDetailsBE;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAsset;
import eu.possiblex.participantportal.business.entity.edc.contractagreement.ContractAgreement;
import eu.possiblex.participantportal.business.entity.fh.OfferingDetailsQueryResult;
import eu.possiblex.participantportal.business.entity.fh.ParticipantNameQueryResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ContractServiceImpl implements ContractService {
    private final EdcClient edcClient;
    private final FhCatalogClient fhCatalogClient;

    public ContractServiceImpl(@Autowired EdcClient edcClient, @Autowired FhCatalogClient fhCatalogClient) {

        this.edcClient = edcClient;
        this.fhCatalogClient = fhCatalogClient;
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

        Set<String> referencedAssetIds = contractAgreements.stream().map(ContractAgreement::getAssetId).collect(Collectors.toSet());
        Set<String> referencedConsumerDapsIds = contractAgreements.stream().map(ContractAgreement::getConsumerId).collect(Collectors.toSet());
        Set<String> referencedProviderDapsIds = contractAgreements.stream().map(ContractAgreement::getProviderId).collect(Collectors.toSet());
        Set<String> refrencedDapsIds = new HashSet<>(referencedConsumerDapsIds);
        refrencedDapsIds.addAll(referencedProviderDapsIds);

        Map<String, ParticipantNameQueryResult> participantNames = fhCatalogClient.getParticipantNames(refrencedDapsIds);
        Map<String, OfferingDetailsQueryResult> offeringDetails = fhCatalogClient.getOfferingDetails(referencedAssetIds);

        contractAgreements.forEach(c ->
            contractAgreementBEs.add(ContractAgreementBE.builder()
            .contractAgreement(c)
            .offeringDetails(OfferingDetailsBE.builder()
                    .name(offeringDetails.get(c.getAssetId()).getName())
                    .description(offeringDetails.get(c.getAssetId()).getDescription())
                .build())
            .consumerDetails(ParticipantDetailsBE.builder()
                    .name(participantNames.get(c.getConsumerId()).getName())
                .build())
            .providerDetails(ParticipantDetailsBE.builder()
                    .name(participantNames.get(c.getProviderId()).getName())
                .build())
            .build()));

        return contractAgreementBEs;
    }
}
