package eu.possiblex.participantportal.business.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import eu.possiblex.participantportal.application.entity.policies.EverythingAllowedPolicy;
import eu.possiblex.participantportal.application.entity.policies.ParticipantRestrictionPolicy;
import eu.possiblex.participantportal.business.entity.*;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.edc.DataspaceErrorMessage;
import eu.possiblex.participantportal.business.entity.edc.asset.DataAddress;
import eu.possiblex.participantportal.business.entity.edc.asset.ionoss3extension.IonosS3DataDestination;
import eu.possiblex.participantportal.business.entity.edc.catalog.*;
import eu.possiblex.participantportal.business.entity.edc.common.IdResponse;
import eu.possiblex.participantportal.business.entity.edc.negotiation.ContractNegotiation;
import eu.possiblex.participantportal.business.entity.edc.negotiation.ContractOffer;
import eu.possiblex.participantportal.business.entity.edc.negotiation.NegotiationInitiateRequest;
import eu.possiblex.participantportal.business.entity.edc.negotiation.NegotiationState;
import eu.possiblex.participantportal.business.entity.edc.policy.OdrlConstraint;
import eu.possiblex.participantportal.business.entity.edc.policy.OdrlPermission;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.edc.transfer.IonosS3TransferProcess;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcess;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcessState;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferRequest;
import eu.possiblex.participantportal.business.entity.exception.NegotiationFailedException;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.ParticipantNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.TransferFailedException;
import eu.possiblex.participantportal.business.entity.fh.ParticipantDetailsSparqlQueryResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ConsumerServiceImpl implements ConsumerService {

    private static final int MAX_NEGOTIATION_CHECK_ATTEMPTS = 15;

    private static final int MAX_TRANSFER_CHECK_ATTEMPTS = 30;

    private static final String UNKNOWN_ERROR = "Unknown Error.";

    private final ObjectMapper objectMapper;

    private final EdcClient edcClient;

    private final FhCatalogClient fhCatalogClient;

    private final TaskScheduler taskScheduler;

    private final String bucketStorageRegion;

    private final String bucketName;

    private final String bucketTopLevelFolder;

    private final ConsumerServiceMapper consumerServiceMapper;

    public ConsumerServiceImpl(@Autowired ObjectMapper objectMapper, @Autowired EdcClient edcClient,
        @Autowired FhCatalogClient fhCatalogClient, @Autowired TaskScheduler taskScheduler,
        @Value("${s3.bucket-storage-region}") String bucketStorageRegion, @Value("${s3.bucket-name}") String bucketName,
        @Value("${s3.bucket-top-level-folder}") String bucketTopLevelFolder,
        @Autowired ConsumerServiceMapper consumerServiceMapper) {

        this.objectMapper = objectMapper;
        this.edcClient = edcClient;
        this.fhCatalogClient = fhCatalogClient;
        this.taskScheduler = taskScheduler;
        this.bucketStorageRegion = bucketStorageRegion;
        this.bucketName = bucketName;
        this.bucketTopLevelFolder = bucketTopLevelFolder;
        this.consumerServiceMapper = consumerServiceMapper;
    }

    @Override
    public SelectOfferResponseBE selectContractOffer(SelectOfferRequestBE request) throws OfferNotFoundException,
        ParticipantNotFoundException {
        // get offer from FH Catalog and parse the attributes needed to get the offer from EDC Catalog
        OfferRetrievalResponseBE offerRetrievalResponseBE = fhCatalogClient.getFhCatalogOffer(
            request.getFhCatalogOfferId());
        PxExtendedServiceOfferingCredentialSubject fhCatalogOffer = offerRetrievalResponseBE.getCatalogOffering();
        boolean isDataOffering = !(fhCatalogOffer.getAggregationOf() == null || fhCatalogOffer.getAggregationOf()
            .isEmpty());
        log.info("got fh catalog offer {}", fhCatalogOffer);

        // get offer from EDC Catalog
        DcatCatalog edcCatalog = queryEdcCatalog(CatalogRequest.builder()
            .counterPartyAddress(fhCatalogOffer.getProviderUrl())
            .querySpec(QuerySpec.builder()
                .filterExpression(List.of(FilterExpression.builder()
                    .operandLeft("id")
                    .operator("=")
                    .operandRight(fhCatalogOffer.getAssetId())
                    .build()))
                .build())
            .build());
        log.info("got edc catalog: {}", edcCatalog);
        DcatDataset edcCatalogOffer = getDatasetById(edcCatalog, fhCatalogOffer.getAssetId());

        List<EnforcementPolicy> enforcementPolicies = getEnforcementPoliciesFromEdcPolicies(
            edcCatalogOffer.getHasPolicy());

        Map<String, ParticipantDetailsSparqlQueryResult> participantDetailsMap = getParticipantDetailsInOffer(
            fhCatalogOffer, isDataOffering, enforcementPolicies);

        ParticipantDetailsSparqlQueryResult providerDetails = participantDetailsMap.get(fhCatalogOffer.getProvidedBy().getId());

        if (providerDetails == null) {
            throw new ParticipantNotFoundException("Provider of offer with ID " + fhCatalogOffer.getId() + " not found in catalog.");
        }

        Map<String, ParticipantNameBE> participantNamesMap = new HashMap<>();

        participantDetailsMap.forEach((k, v) -> participantNamesMap.put(k, consumerServiceMapper
            .mapToParticipantNameBE(v)));

        SelectOfferResponseBE response = new SelectOfferResponseBE();
        response.setEdcOffer(edcCatalogOffer);
        response.setCatalogOffering(fhCatalogOffer);
        response.setDataOffering(isDataOffering);
        response.setEnforcementPolicies(enforcementPolicies);
        response.setProviderDetails(consumerServiceMapper.mapToParticipantWithMailBE(providerDetails));
        response.setParticipantNames(participantNamesMap);
        response.setOfferRetrievalDate(offerRetrievalResponseBE.getOfferRetrievalDate());

        return response;
    }

    private Map<String, ParticipantDetailsSparqlQueryResult> getParticipantDetailsInOffer(
        PxExtendedServiceOfferingCredentialSubject fhCatalogOffer, boolean isDataOffering,
        List<EnforcementPolicy> enforcementPolicies) {

        Set<String> participantIds = new HashSet<>();
        participantIds.add(fhCatalogOffer.getProvidedBy().getId());

        if (isDataOffering) {
            participantIds.add(fhCatalogOffer.getAggregationOf().get(0).getCopyrightOwnedBy().getId());
            participantIds.add(fhCatalogOffer.getAggregationOf().get(0).getProducedBy().getId());
        }

        for (EnforcementPolicy enforcementPolicy : enforcementPolicies) {
            if (enforcementPolicy instanceof ParticipantRestrictionPolicy participantrestrictionpolicy) {
                participantIds.addAll(participantrestrictionpolicy.getAllowedParticipants());
            }
        }

        return fhCatalogClient.getParticipantDetailsByIds(participantIds);
    }

    @Override
    public AcceptOfferResponseBE acceptContractOffer(ConsumeOfferRequestBE request)
        throws OfferNotFoundException, NegotiationFailedException {

        // query edcOffer
        DcatCatalog edcOffer = queryEdcCatalog(CatalogRequest.builder()
            .counterPartyAddress(request.getCounterPartyAddress())
            .querySpec(QuerySpec.builder()
                .filterExpression(List.of(FilterExpression.builder()
                    .operandLeft("id")
                    .operator("=")
                    .operandRight(request.getEdcOfferId())
                    .build()))
                .build())
            .build());
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
            CatalogRequest.builder()
                .counterPartyAddress(request.getCounterPartyAddress())
                .querySpec(QuerySpec.builder()
                    .filterExpression(List.of(FilterExpression.builder()
                            .operandLeft("id")
                            .operator("=")
                            .operandRight(request.getEdcOfferId())
                        .build()))
                    .build())
                .build());
        DcatDataset dataset = getDatasetById(edcOffer, request.getEdcOfferId());

        // initiate transfer
        String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String bucketTargetPath = bucketTopLevelFolder + "/" + timestamp + "_" + request.getContractAgreementId() + "/";
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

        List<DcatDataset> datasets = catalog.getDataset();

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
            if (negotiationCheckAttempts >= MAX_NEGOTIATION_CHECK_ATTEMPTS) {
                throw new NegotiationFailedException("Negotiation never reached FINALIZED state and timed out.");
            } else if (contractNegotiation.getState().equals(NegotiationState.TERMINATED)) {
                String errorReason;
                try {
                    errorReason = contractNegotiation.getErrorDetail() == null
                        ? UNKNOWN_ERROR
                        : objectMapper.readValue(contractNegotiation.getErrorDetail(), DataspaceErrorMessage.class)
                            .getReason();
                } catch (JsonProcessingException e) {
                    log.warn("Failed to read error message from payload", e);
                    errorReason = UNKNOWN_ERROR;
                }

                throw new NegotiationFailedException("Negotiation was terminated. " + errorReason);
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
            if (transferCheckAttempts >= MAX_TRANSFER_CHECK_ATTEMPTS) {
                deprovisionTransfer(transferProcess.getId());
                throw new TransferFailedException("Transfer never reached COMPLETED state and timed out.");
            } else if (transferProcess.getState().equals(TransferProcessState.TERMINATED)) {
                deprovisionTransfer(transferProcess.getId());

                String errorReason;
                try {
                    errorReason = transferProcess.getErrorDetail() == null
                        ? UNKNOWN_ERROR
                        : objectMapper.readValue(transferProcess.getErrorDetail(), DataspaceErrorMessage.class)
                            .getReason();
                } catch (JsonProcessingException e) {
                    log.warn("Failed to read error message from payload", e);
                    errorReason = UNKNOWN_ERROR;
                }

                throw new TransferFailedException("Transfer was terminated. " + errorReason);
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

    /**
     * Given the ODRL Policy stored in the EDC, build the corresponding list of enforcement policies.
     *
     * @param policies ODRL Policies
     * @return enforcement policies
     */
    @Override
    public List<EnforcementPolicy> getEnforcementPoliciesFromEdcPolicies(List<Policy> policies) {

        List<OdrlConstraint> constraints = new ArrayList<>();
        for (Policy policy : policies) {
            for (OdrlPermission permission : policy.getPermission()) {
                constraints.addAll(permission.getConstraint());
            }
        }

        Set<EnforcementPolicy> enforcementPolicies = new HashSet<>();
        for (OdrlConstraint constraint : constraints) {
            if (constraint.getLeftOperand().equals("did")) {
                enforcementPolicies.add(
                    new ParticipantRestrictionPolicy(List.of(constraint.getRightOperand().split(","))));
            } else {
                log.warn("Encountered unknown constraint: {}", constraint);
            }
        }

        if (enforcementPolicies.isEmpty()) {
            enforcementPolicies.add(new EverythingAllowedPolicy());
        }

        return enforcementPolicies.stream().toList();
    }
}