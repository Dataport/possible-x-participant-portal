/*
 *  Copyright 2024 Dataport. All rights reserved. Developed as part of the MERLOT project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * Modifications:
 * - Dataport (part of the POSSIBLE project) - 14 August, 2024 - Adjust package names and imports
 */

package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.business.entity.edc.asset.AssetCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.asset.ionoss3extension.IonosS3DataDestination;
import eu.possiblex.participantportal.business.entity.edc.asset.ionoss3extension.IonosS3DataSource;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAsset;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAssetDataAccountExport;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAssetProperties;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAssetTnC;
import eu.possiblex.participantportal.business.entity.edc.catalog.CatalogRequest;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatCatalog;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDataset;
import eu.possiblex.participantportal.business.entity.edc.catalog.QuerySpec;
import eu.possiblex.participantportal.business.entity.edc.common.IdResponse;
import eu.possiblex.participantportal.business.entity.edc.contractagreement.ContractAgreement;
import eu.possiblex.participantportal.business.entity.edc.contractdefinition.ContractDefinitionCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.negotiation.ContractNegotiation;
import eu.possiblex.participantportal.business.entity.edc.negotiation.NegotiationInitiateRequest;
import eu.possiblex.participantportal.business.entity.edc.negotiation.NegotiationState;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.edc.policy.PolicyCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.policy.PolicyTarget;
import eu.possiblex.participantportal.business.entity.edc.transfer.DataRequest;
import eu.possiblex.participantportal.business.entity.edc.transfer.IonosS3TransferProcess;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcessState;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EdcClientFake implements EdcClient {

    public static final String FAKE_ID = "myId";

    public static final String BAD_NEGOTIATION_ID = "badNegotiation";

    public static final String VALID_AGREEMENT_ID = "validAgreement";

    public static final String VALID_COUNTER_PARTY_ADDRESS = "validCounterPartyAddress";

    public static final String VALID_CONTRACT_AGREEEMENT_ID = "validContractAgreementId";

    public static final String BAD_TRANSFER_ID = "badTransfer";

    public static final String BAD_GATEWAY_ASSET_ID = "edcerror";

    public static final long FAKE_TIMESTAMP = 1234L;

    private IdResponse generateFakeIdResponse(String id) {

        IdResponse response = new IdResponse();
        response.setId(id);
        response.setCreatedAt(FAKE_TIMESTAMP);
        response.setType("edc:IdResponseDto");
        return response;
    }

    @Override
    public IdResponse createAsset(AssetCreateRequest assetCreateRequest) {

        if (assetCreateRequest.getProperties().getName().equals(BAD_GATEWAY_ASSET_ID)) {
            throw new WebClientResponseException(502, "error", null, null, null);
        }

        return generateFakeIdResponse(assetCreateRequest.getId());
    }

    @Override
    public IdResponse createPolicy(PolicyCreateRequest policyCreateRequest) {

        return generateFakeIdResponse(policyCreateRequest.getId());
    }

    @Override
    public IdResponse createContractDefinition(ContractDefinitionCreateRequest contractDefinitionCreateRequest) {

        return generateFakeIdResponse(contractDefinitionCreateRequest.getId());
    }

    @Override
    public DcatCatalog queryCatalog(CatalogRequest catalogRequest) {

        DcatCatalog catalog = new DcatCatalog();
        List<DcatDataset> datasets = new ArrayList<>();
        for (String id : List.of(FAKE_ID, BAD_NEGOTIATION_ID, BAD_TRANSFER_ID)) {
            DcatDataset dataset = new DcatDataset();
            dataset.setAssetId(id);
            dataset.setHasPolicy(List.of(Policy.builder().id(id).build()));
            datasets.add(dataset);
        }
        catalog.setDataset(datasets);
        return catalog;
    }

    @Override
    public IdResponse negotiateOffer(NegotiationInitiateRequest negotiationInitiateRequest) {

        return generateFakeIdResponse(negotiationInitiateRequest.getOffer().getAssetId());
    }

    @Override
    public ContractNegotiation checkOfferStatus(String negotiationId) {

        ContractNegotiation negotiation = new ContractNegotiation();
        negotiation.setType("edc:ContractNegotiationDto");
        negotiation.setId(FAKE_ID);
        negotiation.setContractAgreementId(FAKE_ID + ":" + FAKE_ID + ":" + FAKE_ID);
        if (negotiationId.equals(BAD_NEGOTIATION_ID)) {
            negotiation.setState(NegotiationState.TERMINATED);
        } else {
            negotiation.setState(NegotiationState.FINALIZED);
        }
        return negotiation;
    }

    @Override
    public IdResponse initiateTransfer(TransferRequest transferRequest) {

        return generateFakeIdResponse(transferRequest.getAssetId());
    }

    @Override
    public IonosS3TransferProcess checkTransferStatus(String transferId) {

        DataRequest request = new DataRequest();
        request.setAssetId(FAKE_ID);
        request.setType("edc:DataRequestDto");
        IonosS3TransferProcess process = new IonosS3TransferProcess();
        process.setId(FAKE_ID);
        process.setType("edc:TransferProcessDto");
        process.setDataRequest(request);
        if (transferId.equals(BAD_TRANSFER_ID)) {
            process.setState(TransferProcessState.TERMINATED);
        } else {
            process.setState(TransferProcessState.COMPLETED);
        }
        process.setDataDestination(new IonosS3DataDestination());
        return process;
    }

    @Override
    public void deprovisionTransfer(String transferId) {

    }

    @Override
    public void revokeContractDefinition(String contractDefinitionId) {

    }

    @Override
    public List<ContractAgreement> queryContractAgreements(QuerySpec querySpec) {

        Policy policy = Policy.builder().target(PolicyTarget.builder().id(FAKE_ID).build()).build();

        ContractAgreement contractAgreement = ContractAgreement.builder()
            .contractSigningDate(BigInteger.valueOf(1728549145)).id(FAKE_ID).assetId(FAKE_ID)
            .consumerId(OmejdnConnectorApiClientFake.PARTICIPANT_ID)
            .providerId(OmejdnConnectorApiClientFake.PARTICIPANT_ID).policy(policy).build();

        return List.of(contractAgreement);
    }

    @Override
    public ContractAgreement getContractAgreementById(String contractAgreementId) {

        return queryContractAgreements(QuerySpec.builder().build()).get(0);
    }

    @Override
    public List<PossibleAsset> queryPossibleAssets() {

        PossibleAssetTnC assetTnC = PossibleAssetTnC.builder().url("https://example.com").hash("hash1234").build();

        PossibleAssetDataAccountExport dataAccountExport = PossibleAssetDataAccountExport.builder()
            .accessType("digital").requestType("API").formatType("application/json").build();

        PossibleAssetProperties properties = PossibleAssetProperties.builder().termsAndConditions(List.of(assetTnC))
            .producedBy(new NodeKindIRITypeId(FAKE_ID)).providedBy(new NodeKindIRITypeId(FAKE_ID))
            .license(List.of("MIT")).copyrightOwnedBy(new NodeKindIRITypeId(FAKE_ID))
            .exposedThrough(new NodeKindIRITypeId(FAKE_ID)).offerId(FAKE_ID).name("name").description("description")
            .dataAccountExport(List.of(dataAccountExport)).build();

        Map<String, String> context = Map.of("edc", "https://w3id.org/edc/v0.0.1/ns/", "odrl",
            "http://www.w3.org/ns/odrl/2/", "@vocab", "https://w3id.org/edc/v0.0.1/ns/");

        IonosS3DataSource dataAddress = IonosS3DataSource.builder().bucketName("bucket").blobName("name")
            .keyName("name").region("storage").build();

        return List.of(PossibleAsset.builder().id(FAKE_ID).type("Asset").properties(properties).context(context)
            .dataAddress(dataAddress).build());
    }

}
