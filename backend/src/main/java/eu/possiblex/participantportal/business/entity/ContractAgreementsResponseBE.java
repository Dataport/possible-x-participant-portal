package eu.possiblex.participantportal.business.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ContractAgreementsResponseBE {
    private List<ContractAgreementBE> contractAgreements;

    private int totalNumberOfContractAgreements;
}
