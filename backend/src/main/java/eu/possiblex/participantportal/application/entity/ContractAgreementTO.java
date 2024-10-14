package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractAgreementTO {
    private String id;

    private String assetId;

    private AssetDetailsTO assetDetails;

    private Policy policy;

    private OffsetDateTime contractSigningDate;

    private String consumerId;

    private String providerId;
}
