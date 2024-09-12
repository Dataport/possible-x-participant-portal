package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.edc.asset.AssetCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.asset.AssetProperties;
import eu.possiblex.participantportal.business.entity.edc.asset.DataAddress;
import eu.possiblex.participantportal.business.entity.edc.asset.ionoss3extension.IonosS3DataSource;
import eu.possiblex.participantportal.business.entity.edc.common.IdResponse;
import eu.possiblex.participantportal.business.entity.edc.contractdefinition.ContractDefinitionCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.contractdefinition.Criterion;
import eu.possiblex.participantportal.business.entity.edc.policy.PolicyCreateRequest;
import eu.possiblex.participantportal.business.entity.fh.CreateFhOfferBE;
import eu.possiblex.participantportal.business.entity.fh.catalog.DcatDataset;
import eu.possiblex.participantportal.business.entity.fh.catalog.DcatDistribution;

import java.util.List;
import java.util.UUID;

/**
 * The ProviderRequestBuilder class is responsible for constructing various request objects required for creating offers in the
 * FH catalog and EDC.
 */
public class ProviderRequestBuilder {
    
    private final String assetId;
    private final CreateFhOfferBE createFhOfferBE;
    private final CreateEdcOfferBE createEdcOfferBE;
    private final String edcProtocolUrl;
    
    /**
     * Constructor for ProviderRequestBuilder.
     *
     * @param assetId the asset ID
     * @param createFhOfferBE the FH offer business entity
     * @param createEdcOfferBE the EDC offer business entity
     */
    public ProviderRequestBuilder(String assetId, CreateFhOfferBE createFhOfferBE, CreateEdcOfferBE createEdcOfferBE,
        String edcProtocolUrl) {
        this.assetId = assetId;
        this.createFhOfferBE = createFhOfferBE;
        this.createEdcOfferBE = createEdcOfferBE;
        this.edcProtocolUrl = edcProtocolUrl;
    }

    /**
     * Builds a DcatDataset for the FH catalog offer request.
     *
     * @return the DcatDataset
     */
    public DcatDataset buildFhCatalogOfferRequest() {
        return DcatDataset.builder()
            .id(assetId)
            .hasPolicy(createFhOfferBE.getPolicy())
            .title(createFhOfferBE.getOfferName())
            .description(createFhOfferBE.getOfferDescription())
            .assetId(assetId)
            .distribution(List.of(DcatDistribution.builder().id("testId").accessUrl(edcProtocolUrl).build()))
            .build();
    }

    /**
     * Builds an AssetCreateRequest for the EDC offer.
     *
     * @return the AssetCreateRequest
     */
    public AssetCreateRequest buildAssetRequest() {
        DataAddress dataAddress = IonosS3DataSource.builder()
            .bucketName("dev-provider-edc-bucket-possible-31952746")
            .blobName(createEdcOfferBE.getFileName())
            .keyName(createEdcOfferBE.getFileName())
            .storage("s3-eu-central-2.ionoscloud.com")
            .build();

        return AssetCreateRequest.builder()
            .id(assetId)
            .properties(AssetProperties.builder()
                .name(createEdcOfferBE.getAssetName())
                .description(createEdcOfferBE.getAssetDescription())
                .contenttype("application/json")
                .build())
            .dataAddress(dataAddress)
            .build();
    }

    /**
     * Builds a PolicyCreateRequest for the EDC offer.
     *
     * @return the PolicyCreateRequest
     */
    public PolicyCreateRequest buildPolicyRequest() {
        return PolicyCreateRequest.builder()
            .id("policyDefinitionId_" + UUID.randomUUID())
            .policy(createEdcOfferBE.getPolicy())
            .build();
    }

    /**
     * Builds a ContractDefinitionCreateRequest for the EDC offer.
     *
     * @param policyIdResponse the policy ID response
     * @param assetIdResponse the asset ID response
     * @return the ContractDefinitionCreateRequest
     */
    public ContractDefinitionCreateRequest buildContractDefinitionRequest(IdResponse policyIdResponse, IdResponse assetIdResponse) {
        return ContractDefinitionCreateRequest.builder()
            .id("contractDefinitionId_" + UUID.randomUUID())
            .contractPolicyId(policyIdResponse.getId())
            .accessPolicyId(policyIdResponse.getId())
            .assetsSelector(List.of(
                Criterion.builder()
                    .operandLeft("https://w3id.org/edc/v0.0.1/ns/id")
                    .operator("=")
                    .operandRight(assetIdResponse.getId())
                    .build()))
            .build();
    }
}