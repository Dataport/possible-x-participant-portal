package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.ContractAgreementBE;
import eu.possiblex.participantportal.business.entity.TransferOfferRequestBE;
import eu.possiblex.participantportal.business.entity.TransferOfferResponseBE;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAsset;
import eu.possiblex.participantportal.business.entity.edc.contractagreement.ContractAgreement;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.TransferFailedException;
import eu.possiblex.participantportal.business.entity.fh.OfferingDetailsSparqlQueryResult;
import eu.possiblex.participantportal.utilities.PossibleXException;
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
    private final ConsumerService consumerService;
    private final FhCatalogClient fhCatalogClient;

    public ContractServiceImpl(@Autowired EdcClient edcClient, @Autowired ConsumerService consumerService, @Autowired FhCatalogClient fhCatalogClient) {

        this.edcClient = edcClient;
        this.consumerService = consumerService;
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

    @Override
    public TransferOfferResponseBE transferDataOfferAgain(TransferOfferRequestBE be) throws OfferNotFoundException, TransferFailedException {
        Map<String, OfferingDetailsSparqlQueryResult> offeringDetailsMap = fhCatalogClient.getDataOfferingDetails(List.of(be.getEdcOfferId()));
        if (offeringDetailsMap.size() > 1) {
            throw new OfferNotFoundException("Multiple offers found in Sparql query result for assetId: " + be.getEdcOfferId());
        }
        String providerUrl;
        OfferingDetailsSparqlQueryResult offeringDetails = offeringDetailsMap.get(be.getEdcOfferId());
        if (offeringDetails == null) {
            throw new OfferNotFoundException("No Data Offering found in Sparql query result for assetId: " + be.getEdcOfferId());
        }
        providerUrl = offeringDetails.getProviderUrl();
        if (providerUrl == null) {
            throw new OfferNotFoundException("Provider URL not found in Sparql query result for assetId: " + be.getEdcOfferId());
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
