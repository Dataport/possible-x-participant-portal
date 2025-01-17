package eu.possiblex.participantportal.application.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractAgreementsResponseTO {
    private List<ContractAgreementTO> contractAgreements;

    private int totalNumberOfContractAgreements;
}
