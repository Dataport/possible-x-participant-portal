package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.ContractAgreementBE;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAsset;
import eu.possiblex.participantportal.business.entity.edc.contractagreement.ContractAgreement;
import eu.possiblex.participantportal.utilities.PossibleXException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            PossibleAsset asset = edcClient.queryPossibleAsset(c.getAssetId());

            if (!asset.getId().equals(c.getAssetId())) {
                throw new PossibleXException(
                    "Failed to retrieve contracts. Asset ID " + asset.getId() + " does not match asset ID "
                        + c.getAssetId() + " in contract agreement with ID " + c.getId() + ".");
            }

            contractAgreementBEs.add(ContractAgreementBE.builder().contractAgreement(c).asset(asset).build());
        });

        return contractAgreementBEs;
    }
}
