package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.*;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
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
import eu.possiblex.participantportal.business.entity.exception.NegotiationFailedException;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.TransferFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ConsumerServiceImpl implements ConsumerService {

    private static final int MAX_NEGOTIATION_CHECK_ATTEMPTS = 15;

    private static final int MAX_TRANSFER_CHECK_ATTEMPTS = 30;

    private final EdcClient edcClient;

    private final FhCatalogClient fhCatalogClient;

    private final TaskScheduler taskScheduler;

    private final String bucketStorageRegion;

    private final String bucketName;

    public ConsumerServiceImpl(@Autowired EdcClient edcClient, @Autowired FhCatalogClient fhCatalogClient,
        @Autowired TaskScheduler taskScheduler, @Value("${s3.bucket-storage-region}") String bucketStorageRegion,
        @Value("${s3.bucket-name}") String bucketName) {

        this.edcClient = edcClient;
        this.fhCatalogClient = fhCatalogClient;
        this.taskScheduler = taskScheduler;
        this.bucketStorageRegion = bucketStorageRegion;
        this.bucketName = bucketName;
    }

    @Override
    public SelectOfferResponseBE selectContractOffer(SelectOfferRequestBE request) throws OfferNotFoundException {
        // get offer from FH Catalog and parse the attributes needed to get the offer from EDC Catalog
        PxExtendedServiceOfferingCredentialSubject fhCatalogOffer = fhCatalogClient.getFhCatalogOffer(
            request.getFhCatalogOfferId());
        boolean isDataOffering = !(fhCatalogOffer.getAggregationOf() == null || fhCatalogOffer.getAggregationOf()
            .isEmpty());
        log.info("got fh catalog offer " + fhCatalogOffer);

        // get offer from EDC Catalog
        DcatCatalog edcCatalog = queryEdcCatalog(
            CatalogRequest.builder().counterPartyAddress(fhCatalogOffer.getProviderUrl()).build());
        log.info("got edc catalog: " + edcCatalog);
        DcatDataset edcCatalogOffer = getDatasetById(edcCatalog, fhCatalogOffer.getAssetId());

        SelectOfferResponseBE response = new SelectOfferResponseBE();
        response.setEdcOffer(edcCatalogOffer);
        response.setCatalogOffering(fhCatalogOffer);
        response.setDataOffering(isDataOffering);

        return response;
    }

    @Override
    public AcceptOfferResponseBE acceptContractOffer(ConsumeOfferRequestBE request)
        throws OfferNotFoundException, NegotiationFailedException {

        // query edcOffer
        DcatCatalog edcOffer = queryEdcCatalog(
            CatalogRequest.builder().counterPartyAddress(request.getCounterPartyAddress()).build());
        DcatDataset dataset = getDatasetById(edcOffer, request.getEdcOfferId());

        // initiate negotiation
        NegotiationInitiateRequest negotiationInitiateRequest = NegotiationInitiateRequest.builder()
            .counterPartyAddress(request.getCounterPartyAddress()).providerId(edcOffer.getParticipantId()).offer(
                ContractOffer.builder().offerId(dataset.getHasPolicy().get(0).getId()).assetId(dataset.getAssetId())
                    .policy(dataset.getHasPolicy().get(0)).build()).build();

        ContractNegotiation contractNegotiation = negotiateOffer(negotiationInitiateRequest);

        return new AcceptOfferResponseBE(contractNegotiation.getState(), contractNegotiation.getContractAgreementId(),
            request.isDataOffering());
    }

    @Override
    public TransferOfferResponseBE transferDataOffer(TransferOfferRequestBE request)
        throws OfferNotFoundException, TransferFailedException {

        // query edcOffer
        DcatCatalog edcOffer = queryEdcCatalog(
            CatalogRequest.builder().counterPartyAddress(request.getCounterPartyAddress()).build());
        DcatDataset dataset = getDatasetById(edcOffer, request.getEdcOfferId());

        // initiate transfer
        String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMDD_HHmmss"));
        String bucketTargetPath = timestamp + "/" + request.getContractAgreementId() + "/";
        DataAddress dataAddress = IonosS3DataDestination.builder().region(bucketStorageRegion).bucketName(bucketName)
            .path(bucketTargetPath).keyName("myKey").build();
        TransferRequest transferRequest = TransferRequest.builder().connectorId(edcOffer.getParticipantId())
            .counterPartyAddress(request.getCounterPartyAddress()).assetId(dataset.getAssetId())
            .contractId(request.getContractAgreementId()).dataDestination(dataAddress).build();
        TransferProcessState transferProcessState = performTransfer(transferRequest).getState();
        return new TransferOfferResponseBE(transferProcessState);
    }

    private DcatCatalog queryEdcCatalog(CatalogRequest catalogRequest) {

        log.info("Query Catalog with Request {}", catalogRequest);
        return edcClient.queryCatalog(catalogRequest);
    }

    private DcatDataset getDatasetById(DcatCatalog catalog, String assetId) throws OfferNotFoundException {

        List<DcatDataset> datasets = catalog.getDataset().stream().filter(d -> d.getAssetId().equals(assetId)).toList();

        if (datasets.size() == 1) {
            return datasets.get(0);
        } else {
            throw new OfferNotFoundException(
                "Offer with given ID " + assetId + " not found or ambiguous. Nr of offers: " + datasets.size());
        }
    }

    private ContractNegotiation negotiateOffer(NegotiationInitiateRequest negotiationInitiateRequest)
        throws NegotiationFailedException {

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
            if (negotiationCheckAttempts >= MAX_NEGOTIATION_CHECK_ATTEMPTS || contractNegotiation.getState()
                .equals(NegotiationState.TERMINATED)) {
                throw new NegotiationFailedException("Negotiation never reached FINALIZED state.");
            }
        } while (!contractNegotiation.getState().equals(NegotiationState.FINALIZED));
        return contractNegotiation;
    }

    private TransferProcess performTransfer(TransferRequest transferRequest) throws TransferFailedException {

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
            if (transferCheckAttempts >= MAX_TRANSFER_CHECK_ATTEMPTS || transferProcess.getState()
                .equals(TransferProcessState.TERMINATED)) {

                deprovisionTransfer(transferProcess.getId());
                throw new TransferFailedException("Transfer never reached COMPLETED state.");
            }
        } while (!transferProcess.getState().equals(TransferProcessState.COMPLETED));

        deprovisionTransfer(transferProcess.getId());

        return transferProcess;
    }

    private void deprovisionTransfer(String transferId) {

        taskScheduler.schedule(new EdcTransferDeprovisionTask(edcClient, transferId), Instant.now().plusSeconds(5));
    }

    private void delayOneSecond() {

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
