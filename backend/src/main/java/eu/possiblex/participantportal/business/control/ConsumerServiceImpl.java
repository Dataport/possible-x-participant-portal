package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.ConsumeOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferRequestBE;
import eu.possiblex.participantportal.business.entity.edc.asset.DataAddress;
import eu.possiblex.participantportal.business.entity.edc.asset.ionoss3extension.IonosS3DataDestination;
import eu.possiblex.participantportal.business.entity.edc.catalog.CatalogRequest;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatCatalog;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDataset;
import eu.possiblex.participantportal.business.entity.edc.common.IdResponse;
import eu.possiblex.participantportal.business.entity.edc.negotiation.ContractNegotiation;
import eu.possiblex.participantportal.business.entity.edc.negotiation.ContractOffer;
import eu.possiblex.participantportal.business.entity.edc.negotiation.NegotiationInitiateRequest;
import eu.possiblex.participantportal.business.entity.edc.negotiation.NegotiationState;
import eu.possiblex.participantportal.business.entity.edc.transfer.IonosS3TransferProcess;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcess;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcessState;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ConsumerServiceImpl implements ConsumerService {

    private final EdcClient edcClient;

    public ConsumerServiceImpl(@Autowired EdcClient edcClient) {

        this.edcClient = edcClient;
    }

    /**
     * Given a request for an offer, select it and return the details of this offer from the EDC catalog.
     *
     * @param request request for selecting the offer
     * @return details of the offer
     */
    @Override
    public DcatDataset selectContractOffer(SelectOfferRequestBE request) {
        DcatCatalog catalog = queryEdcCatalog(CatalogRequest
            .builder()
            .counterPartyAddress(request.getCounterPartyAddress())
            .build());
        return catalog.getDataset().get(0);
    }

    /**
     * Given a request for consuming an offer, perform the necessary steps on the EDC to transfer the data.
     *
     * @param request request for consuming an offer
     * @return data address in the transfer response (TBR)
     */
    @Override
    public TransferProcess acceptContractOffer(ConsumeOfferRequestBE request) {

        // query catalog
        DcatCatalog catalog = queryEdcCatalog(CatalogRequest
            .builder()
            .counterPartyAddress(request.getCounterPartyAddress())
            .build());
        DcatDataset dataset = getDatasetById(catalog, request.getOfferId());

        // initiate negotiation
        NegotiationInitiateRequest negotiationInitiateRequest = NegotiationInitiateRequest
            .builder()
            .counterPartyAddress(request.getCounterPartyAddress())
            .providerId(catalog.getParticipantId())
            .offer(ContractOffer
                .builder()
                .offerId(dataset.getHasPolicy().get(0).getId())
                .assetId(dataset.getAssetId())
                .policy(dataset.getHasPolicy().get(0))
                .build())
            .build();
        ContractNegotiation contractNegotiation = negotiateOffer(negotiationInitiateRequest);

        // initiate transfer
        DataAddress dataAddress = IonosS3DataDestination
            .builder()
            .storage("s3-eu-central-2.ionoscloud.com")
            .bucketName("dev-consumer-edc-bucket-possible-31952746")
            .path("s3HatGeklappt/")
            .keyName("myKey")
            .build();
        TransferRequest transferRequest = TransferRequest
            .builder()
            .connectorId(catalog.getParticipantId())
            .counterPartyAddress(request.getCounterPartyAddress())
            .assetId(dataset.getAssetId())
            .contractId(contractNegotiation.getContractAgreementId())
            .dataDestination(dataAddress)
            .build();
        return performTransfer(transferRequest);
    }

    private DcatCatalog queryEdcCatalog(CatalogRequest catalogRequest) {
        // query catalog
        log.info("Query Catalog with Request {}", catalogRequest);
        return edcClient.queryCatalog(catalogRequest);
    }

    private DcatDataset getDatasetById(DcatCatalog catalog, String offerId) {
        List<DcatDataset> datasets = catalog.getDataset()
            .stream()
            .filter(d -> d.getAssetId().equals(offerId))
            .toList();

        if (datasets.size() == 1) {
            return datasets.get(0);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Offer with given ID not found or ambiguous.");
        }
    }

    private ContractNegotiation negotiateOffer(NegotiationInitiateRequest negotiationInitiateRequest) {
        log.info("Initiate Negotiation with Request {}", negotiationInitiateRequest);
        IdResponse negotiation = edcClient.negotiateOffer(negotiationInitiateRequest);

        // wait until FINALIZED
        ContractNegotiation contractNegotiation;
        int negotiationCheckAttempts = 0;
        do {
            delayOneSecond();
            contractNegotiation = edcClient.checkOfferStatus(negotiation.getId());
            log.info("Negotiation {}", contractNegotiation);
            negotiationCheckAttempts += 1;
            if (negotiationCheckAttempts >= 15) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "failed to negotiate");
            }
        } while (!contractNegotiation.getState().equals(NegotiationState.FINALIZED));
        return contractNegotiation;
    }

    private TransferProcess performTransfer(TransferRequest transferRequest) {
        log.info("Initiate Transfer {}", transferRequest);
        IdResponse transfer = edcClient.initiateTransfer(transferRequest);

        // wait until COMPLETED
        IonosS3TransferProcess transferProcess;
        int transferCheckAttempts = 0;
        do {
            delayOneSecond();
            transferProcess = edcClient.checkTransferStatus(transfer.getId());
            log.info("Transfer Process {}", transferProcess);
            transferCheckAttempts += 1;
            if (transferCheckAttempts >= 15) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "failed to transfer");
            }
        } while (!transferProcess.getState().equals(TransferProcessState.COMPLETED));

        edcClient.deprovisionTransfer(transferProcess.getId());

        return transferProcess;
    }

    private void delayOneSecond() {

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
