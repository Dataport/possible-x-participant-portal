package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractDetailsTO {

    @Schema(description = "ID of the contract agreement", example = "db4c8c3a-32c7-4887-b3af-0393721345db")
    private String id;

    @Schema(description = "The ID which is used to identify the offering in the EDC catalog. Currently, this is the asset ID, because an asset will only be used in one offering.", example = "8d3c927a-9bb7-4bc8-a3e7-4f9c9a57d571")
    private String assetId;

    @Schema(description = "Offering credential subject as retrieved from the catalog")
    private PxExtendedServiceOfferingCredentialSubject catalogOffering;

    @Schema(description = "Date when the offering was retrieved")
    private OffsetDateTime offerRetrievalDate;

    @Schema(description = "Policy of the contract agreement as retrieved from the EDC")
    private Policy policy;

    @Schema(description = "List of enforcement policies deduced from the contract agreement policies")
    private List<EnforcementPolicy> enforcementPolicies;

    @Schema(description = "Date when the contract was signed", example = "2021-09-01T12:00:00Z")
    private OffsetDateTime contractSigningDate;

    @Schema(description = "Details of the offering consumer")
    private ContractParticipantDetailsTO consumerDetails;

    @Schema(description = "Details of the offering provider")
    private ContractParticipantDetailsTO providerDetails;

    @Schema(description = "Flag whether the contract agreement is related to an offering containing data resources", example = "true")
    private boolean isDataOffering;
}
