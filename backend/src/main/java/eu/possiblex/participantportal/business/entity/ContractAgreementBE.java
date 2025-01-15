package eu.possiblex.participantportal.business.entity;

import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import eu.possiblex.participantportal.business.entity.edc.contractagreement.ContractAgreement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ContractAgreementBE {
    private ContractAgreement contractAgreement;

    private OfferingDetailsBE offeringDetails;

    private ParticipantWithDapsBE providerDetails;

    private ParticipantWithDapsBE consumerDetails;

    private List<EnforcementPolicy> enforcementPolicies;

    private boolean isDataOffering;

    private boolean isConsumer;
}
