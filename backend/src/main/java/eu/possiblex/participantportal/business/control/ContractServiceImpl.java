package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.ContractAgreementBE;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAsset;
import eu.possiblex.participantportal.business.entity.edc.contractagreement.ContractAgreement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ContractServiceImpl implements ContractService {
    private final EdcClient edcClient;

    public ContractServiceImpl(@Autowired EdcClient edcClient) {

        this.edcClient = edcClient;
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

        contractAgreements.forEach(c -> {
            PossibleAsset asset = getPossibleAsset(c);
            contractAgreementBEs.add(ContractAgreementBE.builder().contractAgreement(c).asset(asset).build());
        });

        return contractAgreementBEs;
    }

    private PossibleAsset getPossibleAsset(ContractAgreement c) {

        PossibleAsset asset = null;
        try {
            asset = edcClient.queryPossibleAsset(c.getAssetId());
        } catch (WebClientResponseException.NotFound notFound) {
            // For Consumer, this is not a problem, as the asset is not kept in the EDC
            // For Provider, this is a problem, as the asset should be available in the EDC
            // We cannot differentiate between the two at the moment
            log.warn("Failed to retrieve asset with ID {} for contract agreement with ID {}.", c.getAssetId(),
                c.getId(), notFound);
        }
        return asset;
    }
}
