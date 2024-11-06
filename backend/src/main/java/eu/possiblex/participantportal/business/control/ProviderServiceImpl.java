package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.CreateOfferResponseTO;
import eu.possiblex.participantportal.application.entity.ParticipantIdTO;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import eu.possiblex.participantportal.application.entity.policies.ParticipantRestrictionPolicy;
import eu.possiblex.participantportal.business.entity.CreateDataOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.edc.asset.AssetCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.common.IdResponse;
import eu.possiblex.participantportal.business.entity.edc.contractdefinition.ContractDefinitionCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.policy.*;
import eu.possiblex.participantportal.business.entity.exception.EdcOfferCreationException;
import eu.possiblex.participantportal.business.entity.exception.FhOfferCreationException;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.utilities.PossibleXException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service implementation for managing provider-related operations.
 */
@Service
@Slf4j
public class ProviderServiceImpl implements ProviderService {

    private final EdcClient edcClient;

    private final FhCatalogClient fhCatalogClient;

    private final ProviderServiceMapper providerServiceMapper;

    private final String bucketStorageRegion;

    private final String bucketName;

    private final String edcProtocolUrl;

    private final String participantId;

    /**
     * Constructor for ProviderServiceImpl.
     *
     * @param edcClient the EDC client
     * @param fhCatalogClient the FH catalog client
     */
    @Autowired
    public ProviderServiceImpl(@Autowired EdcClient edcClient, @Autowired FhCatalogClient fhCatalogClient,
        @Autowired ProviderServiceMapper providerServiceMapper,
        @Value("${edc.protocol-base-url}") String edcProtocolUrl, @Value("${participant-id}") String participantId,
        @Value("${s3.bucket-storage-region}") String bucketStorageRegion,
        @Value("${s3.bucket-name}") String bucketName) {

        this.edcClient = edcClient;
        this.fhCatalogClient = fhCatalogClient;
        this.providerServiceMapper = providerServiceMapper;
        this.edcProtocolUrl = edcProtocolUrl;
        this.participantId = participantId;
        this.bucketStorageRegion = bucketStorageRegion;
        this.bucketName = bucketName;
    }

    /**
     * Creates an offering by interacting with both FH catalog and EDC.
     *
     * @param request the service offering creation request
     * @return the response transfer object containing offer IDs
     */
    @Override
    public CreateOfferResponseTO createOffering(CreateServiceOfferingRequestBE request) {

        Policy policy = createEdcPolicyFromEnforcementPolicies(request.getEnforcementPolicies());

        PxExtendedServiceOfferingCredentialSubject pxExtendedServiceOfferingCs = createCombinedCsFromRequest(request,
            policy);
        CreateEdcOfferBE createEdcOfferBE = createEdcBEFromRequest(request, pxExtendedServiceOfferingCs.getId(),
            pxExtendedServiceOfferingCs.getAssetId(), policy);

        try {
            FhCatalogIdResponse fhResponseId = createFhCatalogOffer(pxExtendedServiceOfferingCs);
            IdResponse edcResponseId = createEdcOffer(createEdcOfferBE);
            return new CreateOfferResponseTO(edcResponseId.getId(), fhResponseId.getId());
        } catch (EdcOfferCreationException e) {
            throw new PossibleXException("Failed to create offer. EdcOfferCreationException: " + e,
                HttpStatus.BAD_REQUEST);
        } catch (FhOfferCreationException e) {
            throw new PossibleXException("Failed to create offer. FhOfferCreationException: " + e,
                HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new PossibleXException("Failed to create offer. Other Exception: " + e);
        }
    }

    /**
     * Return the participant's id.
     *
     * @return participant id
     */
    @Override
    public ParticipantIdTO getParticipantId() {

        return new ParticipantIdTO(participantId);
    }

    /**
     * Creates an EDC offer by building and sending the necessary requests.
     *
     * @param createEdcOfferBE the EDC offer business entity
     * @return the ID response from EDC
     * @throws EdcOfferCreationException if EDC offer creation fails
     */
    private IdResponse createEdcOffer(CreateEdcOfferBE createEdcOfferBE) throws EdcOfferCreationException {

        ProviderRequestBuilder requestBuilder = new ProviderRequestBuilder(createEdcOfferBE);

        try {
            AssetCreateRequest assetCreateRequest = requestBuilder.buildAssetRequest(bucketName, bucketStorageRegion);
            log.info("Creating Asset {}", assetCreateRequest);
            IdResponse assetIdResponse = edcClient.createAsset(assetCreateRequest);

            PolicyCreateRequest policyCreateRequest = requestBuilder.buildPolicyRequest();
            log.info("Creating Policy {}", policyCreateRequest);
            IdResponse policyIdResponse = edcClient.createPolicy(policyCreateRequest);

            ContractDefinitionCreateRequest contractDefinitionCreateRequest = requestBuilder.buildContractDefinitionRequest(
                policyIdResponse, assetIdResponse);
            log.info("Creating Contract Definition {}", contractDefinitionCreateRequest);

            return edcClient.createContractDefinition(contractDefinitionCreateRequest);
        } catch (Exception e) {
            throw new EdcOfferCreationException("An error occurred during Edc offer creation: " + e.getMessage());
        }
    }

    /**
     * Creates an FH catalog offer by sending the service offering creation requests.
     *
     * @param serviceOfferingCredentialSubject the service offering credential subject
     * @return the ID response from FH catalog
     * @throws FhOfferCreationException if FH offer creation fails
     */
    private FhCatalogIdResponse createFhCatalogOffer(
        PxExtendedServiceOfferingCredentialSubject serviceOfferingCredentialSubject) throws FhOfferCreationException {

        try {
            log.info("Adding Service Offering to Fraunhofer Catalog {}", serviceOfferingCredentialSubject);

            return fhCatalogClient.addServiceOfferingToFhCatalog(serviceOfferingCredentialSubject);
        } catch (Exception e) {
            throw new FhOfferCreationException("An error occurred during Fh offer creation: " + e.getMessage());
        }
    }

    /**
     * Creates a combined credential subject for the offering from the request.
     *
     * @param request the offering creation request
     * @param policy policy for the offering
     * @return the combined credential subject
     */
    private PxExtendedServiceOfferingCredentialSubject createCombinedCsFromRequest(
        CreateServiceOfferingRequestBE request, Policy policy) {

        String assetId = UUID.randomUUID().toString();
        String serviceOfferingId = "urn:uuid:" + UUID.randomUUID();
        String dataResourceId = "urn:uuid:" + UUID.randomUUID();

        if (request instanceof CreateDataOfferingRequestBE dataOfferingRequest) { // data offering
            dataOfferingRequest.getDataResource().setId(dataResourceId);
            dataOfferingRequest.getDataResource().setExposedThrough(new NodeKindIRITypeId(serviceOfferingId));

            return providerServiceMapper.getPxExtendedServiceOfferingCredentialSubject(dataOfferingRequest,
                serviceOfferingId, assetId, edcProtocolUrl, policy);
        } else { // base service offering
            return providerServiceMapper.getPxExtendedServiceOfferingCredentialSubject(request, serviceOfferingId,
                assetId, edcProtocolUrl, policy);
        }

    }

    /**
     * Creates the payload for the EDC offer from the request.
     *
     * @param request the offering creation request
     * @param offerId the id of the created offer
     * @param assetId the id of the created asset
     * @param policy the policy for the EDC
     * @return the EDC offer business entity
     */
    private CreateEdcOfferBE createEdcBEFromRequest(CreateServiceOfferingRequestBE request, String offerId,
        String assetId, Policy policy) {

        CreateEdcOfferBE createEdcOfferBE = null;
        if (request instanceof CreateDataOfferingRequestBE dataOfferingRequest) { // data offering
            createEdcOfferBE = providerServiceMapper.getCreateEdcOfferBE(dataOfferingRequest, offerId, assetId, policy);
            createEdcOfferBE.getProperties()
                .setOfferingPolicy(providerServiceMapper.combineSOPolicyAndPolicy(dataOfferingRequest, policy));

        } else { // base service offering
            createEdcOfferBE = providerServiceMapper.getCreateEdcOfferBE(request, offerId, assetId, policy);
            createEdcOfferBE.getProperties()
                .setOfferingPolicy(providerServiceMapper.combineSOPolicyAndPolicy(request, policy));
        }
        return createEdcOfferBE;
    }

    /**
     * Given a list of enforcement policies, convert them to a single policy that can be given to the EDC for
     * evaluation.
     *
     * @param enforcementPolicies list of enforcement policies and their constraints
     * @return edc policy
     */
    private Policy createEdcPolicyFromEnforcementPolicies(List<EnforcementPolicy> enforcementPolicies) {

        List<OdrlConstraint> constraints = new ArrayList<>();

        // iterate over all enforcement policies and add a constraint per entry
        for (EnforcementPolicy enforcementPolicy : enforcementPolicies) {
            if (enforcementPolicy instanceof ParticipantRestrictionPolicy participantRestrictionPolicy) { // restrict to participants

                // create constraint
                OdrlConstraint participantConstraint = OdrlConstraint.builder().leftOperand("connectorId")
                    .operator(OdrlOperator.IN)
                    .rightOperand(String.join(",", participantRestrictionPolicy.getAllowedParticipants())).build();
                constraints.add(participantConstraint);
            } // else unknown or everything allowed => no constraint
        }

        // apply constraints to both use and transfer permission
        OdrlPermission usePermission = OdrlPermission.builder().action(OdrlAction.USE).constraint(constraints).build();
        OdrlPermission transferPermission = OdrlPermission.builder().action(OdrlAction.TRANSFER).constraint(constraints)
            .build();

        // add permissions to ODRL policy
        Policy policy = new Policy();
        policy.getPermission().add(usePermission);
        policy.getPermission().add(transferPermission);

        return policy;
    }

}
