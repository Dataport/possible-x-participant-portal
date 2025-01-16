package eu.possiblex.participantportal.business.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.application.entity.policies.*;
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
import eu.possiblex.participantportal.business.entity.edc.policy.OdrlOperator;
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
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

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
    public SelectOfferResponseBE selectContractOffer(SelectOfferRequestBE request) {
        // get offer from FH Catalog and parse the attributes needed to get the offer from EDC Catalog
        OfferRetrievalResponseBE offerRetrievalResponseBE = fhCatalogClient.getFhCatalogOffer(
            request.getFhCatalogOfferId());
        PxExtendedServiceOfferingCredentialSubject fhCatalogOffer = offerRetrievalResponseBE.getCatalogOffering();
        boolean isDataOffering = !(fhCatalogOffer.getAggregationOf() == null || fhCatalogOffer.getAggregationOf()
            .isEmpty());
        log.info("got fh catalog offer {}", fhCatalogOffer);

        // get offer from EDC Catalog
        DcatCatalog edcCatalog = queryEdcCatalog(
            CatalogRequest.builder().counterPartyAddress(fhCatalogOffer.getProviderUrl()).querySpec(QuerySpec.builder()
                .filterExpression(List.of(
                    FilterExpression.builder().operandLeft("id").operator("=").operandRight(fhCatalogOffer.getAssetId())
                        .build())).build()).build());
        log.info("got edc catalog: {}", edcCatalog);
        DcatDataset edcCatalogOffer = getDatasetById(edcCatalog, fhCatalogOffer.getAssetId());

        List<EnforcementPolicy> enforcementPolicies = getEnforcementPoliciesFromEdcPolicies(
            edcCatalogOffer.getHasPolicy());

        Map<String, ParticipantDetailsSparqlQueryResult> participantDetailsMap = fhCatalogClient.getParticipantDetailsByIds(
            List.of(fhCatalogOffer.getProvidedBy().getId()));

        ParticipantDetailsSparqlQueryResult providerDetails = participantDetailsMap.get(
            fhCatalogOffer.getProvidedBy().getId());

        if (providerDetails == null) {
            throw new ParticipantNotFoundException(
                "Provider of offer with ID " + fhCatalogOffer.getId() + " not found in catalog.");
        }

        SelectOfferResponseBE response = new SelectOfferResponseBE();
        response.setEdcOffer(edcCatalogOffer);
        response.setCatalogOffering(fhCatalogOffer);
        response.setDataOffering(isDataOffering);
        response.setEnforcementPolicies(enforcementPolicies);
        response.setProviderDetails(consumerServiceMapper.mapToParticipantWithMailBE(providerDetails));
        response.setOfferRetrievalDate(offerRetrievalResponseBE.getOfferRetrievalDate());

        return response;
    }

    @Override
    public AcceptOfferResponseBE acceptContractOffer(ConsumeOfferRequestBE request) {

        // query edcOffer
        DcatCatalog edcOffer = queryEdcCatalog(
            CatalogRequest.builder().counterPartyAddress(request.getCounterPartyAddress()).querySpec(QuerySpec.builder()
                .filterExpression(List.of(
                    FilterExpression.builder().operandLeft("id").operator("=").operandRight(request.getEdcOfferId())
                        .build())).build()).build());
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
    public TransferOfferResponseBE transferDataOffer(TransferOfferRequestBE request) {

        // query edcOffer
        DcatCatalog edcOffer = queryEdcCatalog(
            CatalogRequest.builder().counterPartyAddress(request.getCounterPartyAddress()).querySpec(QuerySpec.builder()
                .filterExpression(List.of(
                    FilterExpression.builder().operandLeft("id").operator("=").operandRight(request.getEdcOfferId())
                        .build())).build()).build());
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

    private DcatDataset getDatasetById(DcatCatalog catalog, String assetId) {

        List<DcatDataset> datasets = catalog.getDataset();

        if (datasets.size() == 1) {
            return datasets.get(0);
        } else {
            throw new OfferNotFoundException(
                "Offer with given ID " + assetId + " not found or ambiguous. Nr of offers: " + datasets.size());
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
            EnforcementPolicy policy = switch (constraint.getLeftOperand()) {
                case ParticipantRestrictionPolicy.EDC_OPERAND -> parseParticipantRestrictionPolicy(constraint);
                case
                    TimeAgreementOffsetPolicy.EDC_OPERAND  // currently time agreement offset and time date have the same name in the edc, hence we need to handle both here
                    -> parseTimedEnforcementPolicy(constraint);
                default -> null;
            };

            if (policy != null) {
                enforcementPolicies.add(policy);
            } else {
                log.warn("Unknown enforcement policy: {}", constraint);
            }
        }

        if (enforcementPolicies.isEmpty()) {
            enforcementPolicies.add(new EverythingAllowedPolicy());
        }

        return enforcementPolicies.stream().toList();
    }

    ParticipantRestrictionPolicy parseParticipantRestrictionPolicy(OdrlConstraint constraint) {

        return new ParticipantRestrictionPolicy(List.of(constraint.getRightOperand().split(",")));
    }

    EnforcementPolicy parseTimedEnforcementPolicy(OdrlConstraint constraint) {
        // check whether we have a start or end date policy
        boolean endDate;
        if (constraint.getOperator().equals(OdrlOperator.LEQ)) {
            endDate = true;
        } else if (constraint.getOperator().equals(OdrlOperator.GEQ)) {
            endDate = false;
        } else {
            log.error("Failed to parse operator in timed policy {}", constraint);
            return null;  // unknown type of time policy
        }

        // try to parse as time agreement offset policy
        TimeAgreementOffsetPolicy policy = parseTimeAgreementOffsetPolicy(constraint, endDate);
        if (policy != null) {
            return policy;
        }

        // try to parse as time date policy
        return parseTimeDatePolicy(constraint, endDate);
    }

    TimeAgreementOffsetPolicy parseTimeAgreementOffsetPolicy(OdrlConstraint constraint, boolean endDate) {

        var matcher = Pattern.compile("(contract[A,a]greement)\\+(-?[0-9]+)(s|m|h|d)")
            .matcher(constraint.getRightOperand());
        if (matcher.matches()) {
            int number = Integer.parseInt(matcher.group(2));
            AgreementOffsetUnit unit = AgreementOffsetUnit.forValue(matcher.group(3));
            return endDate
                ? EndAgreementOffsetPolicy.builder().offsetNumber(number).offsetUnit(unit).build()
                : StartAgreementOffsetPolicy.builder().offsetNumber(number).offsetUnit(unit).build();
        }
        return null;
    }

    TimeDatePolicy parseTimeDatePolicy(OdrlConstraint constraint, boolean endDate) {

        try {
            OffsetDateTime date = OffsetDateTime.parse(constraint.getRightOperand());
            return endDate ? EndDatePolicy.builder().date(date).build() : StartDatePolicy.builder().date(date).build();
        } catch (Exception e) {
            log.error("Failed to parse timestamp in policy {}", constraint, e);
            return null;
        }
    }
}