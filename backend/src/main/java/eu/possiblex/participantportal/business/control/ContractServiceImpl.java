package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.ContractAgreementBE;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAsset;
import eu.possiblex.participantportal.business.entity.edc.contractagreement.ContractAgreement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        Map<String, PossibleAsset> assetMap = getPossibleAssetsMap();

        contractAgreements.forEach(c -> {
            // The asset might be null, if it is not available in the EDC
            // As the consumer, this is not a problem
            // As the provider, this is a problem, because the asset should be available in the EDC
            // We cannot differentiate between the two cases at the moment
            PossibleAsset asset = assetMap.get(c.getAssetId());
            contractAgreementBEs.add(ContractAgreementBE.builder().contractAgreement(c).asset(asset).build());
        });

        return contractAgreementBEs;
    }

    private Map<String, PossibleAsset> getPossibleAssetsMap() {

        List<PossibleAsset> assets = edcClient.queryPossibleAssets();

        return assets.stream().collect(Collectors.toMap(PossibleAsset::getId, Function.identity()));
    }
}
