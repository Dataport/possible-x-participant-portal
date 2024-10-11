package eu.possible_x.edc_orchestrator.service;

import eu.possible_x.edc_orchestrator.entities.ConsumeOfferRequest;
import eu.possible_x.edc_orchestrator.entities.edc.asset.DataAddress;
import eu.possible_x.edc_orchestrator.entities.edc.asset.ionoss3extension.IonosS3DataAddress;
import eu.possible_x.edc_orchestrator.entities.edc.asset.ionoss3extension.IonosS3DataDestination;
import eu.possible_x.edc_orchestrator.entities.edc.catalog.CatalogRequest;
import eu.possible_x.edc_orchestrator.entities.edc.catalog.DcatCatalog;
import eu.possible_x.edc_orchestrator.entities.edc.catalog.DcatDataset;
import eu.possible_x.edc_orchestrator.entities.edc.common.IdResponse;
import eu.possible_x.edc_orchestrator.entities.edc.negotiation.ContractNegotiation;
import eu.possible_x.edc_orchestrator.entities.edc.negotiation.ContractOffer;
import eu.possible_x.edc_orchestrator.entities.edc.negotiation.NegotiationInitiateRequest;
import eu.possible_x.edc_orchestrator.entities.edc.transfer.IonosS3TransferProcess;
import eu.possible_x.edc_orchestrator.entities.edc.transfer.TransferRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ConsumerService {

  private final EdcClient edcClient;

  public ConsumerService(@Autowired EdcClient edcClient) {
    this.edcClient = edcClient;
  }

  /**
   * Given a request for consuming an offer, perform the necessary steps on the EDC to transfer the data.
   *
   * @param request request for consuming an offer
   * @return data address in the transfer response (TBR)
   */
  public IonosS3DataAddress acceptContractOffer(ConsumeOfferRequest request) {

    // query catalog (take first entry)
    CatalogRequest catalogRequest = CatalogRequest.builder()
            .counterPartyAddress(request.getCounterPartyAddress())
            .build();
    log.info("Query Catalog with Request {}", catalogRequest);
    DcatCatalog catalog = edcClient.queryCatalog(catalogRequest);
    DcatDataset dataset = catalog.getDataset().get(0);

    // initiate negotiation
    NegotiationInitiateRequest negotiationInitiateRequest = NegotiationInitiateRequest.builder()
            .counterPartyAddress(request.getCounterPartyAddress())
            .providerId(catalog.getParticipantId())
            .offer(ContractOffer.builder()
                    .offerId(dataset.getHasPolicy().get(0).getId())
                    .assetId(dataset.getAssetId())
                    .policy(dataset.getHasPolicy().get(0))
                    .build())
            .build();
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
    } while (!contractNegotiation.getState().equals("FINALIZED"));

    // initiate transfer
    DataAddress dataAddress = IonosS3DataDestination.builder()
        .storage("s3-eu-central-2.ionoscloud.com")
        .bucketName("dev-consumer-edc-bucket-possible-31952746")
        .path("s3HatGeklappt/")
        .keyName("myKey")
        .build();
    TransferRequest transferRequest = TransferRequest.builder()
        .connectorId(catalog.getParticipantId())
        .counterPartyAddress(request.getCounterPartyAddress())
        .assetId(dataset.getAssetId())
        .contractId(contractNegotiation.getContractAgreementId())
        .dataDestination(dataAddress)
        .build();
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
    } while (!transferProcess.getState().equals("COMPLETED"));

    edcClient.deprovisionTransfer(transferProcess.getId());

    return transferProcess.getDataDestination();
  }

  private void delayOneSecond() {
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
    }
  }
}
