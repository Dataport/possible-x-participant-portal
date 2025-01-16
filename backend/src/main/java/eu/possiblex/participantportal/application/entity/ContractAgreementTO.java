package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
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
public class ContractAgreementTO {
    private String id;

    private String assetId;

    private AssetDetailsTO assetDetails;

    private Policy policy;

    private List<EnforcementPolicy> enforcementPolicies;

    private OffsetDateTime contractSigningDate;

    private ContractParticipantDetailsTO consumerDetails;

    private ContractParticipantDetailsTO providerDetails;

    private boolean isDataOffering;

    private boolean isProvider;
}
