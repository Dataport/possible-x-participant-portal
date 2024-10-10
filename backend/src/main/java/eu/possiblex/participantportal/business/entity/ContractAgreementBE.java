package eu.possiblex.participantportal.business.entity;

import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAsset;
import eu.possiblex.participantportal.business.entity.edc.contractagreement.ContractAgreement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ContractAgreementBE {
    private ContractAgreement contractAgreement;

    private PossibleAsset asset;
}
