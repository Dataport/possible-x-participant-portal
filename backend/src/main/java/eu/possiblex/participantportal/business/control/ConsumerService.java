package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import eu.possiblex.participantportal.business.entity.*;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;

import java.util.List;

public interface ConsumerService {
    /**
     * Given a request for an offer, select it and return the details of this offer from the data transfer component.
     *
     * @param request request for selecting the offer
     * @return details of the offer
     */
    SelectOfferResponseBE selectContractOffer(SelectOfferRequestBE request);

    /**
     * Given a request for an offer, accept the offer on the data transfer component and perform the transfer.
     *
     * @param request request for accepting the offer
     * @return final result of the transfer
     */
    AcceptOfferResponseBE acceptContractOffer(ConsumeOfferRequestBE request);

    /**
     * Given a request for a transfer, transfer the data using the data transfer component.
     *
     * @param request request for transferring the data
     * @return final result of the transfer
     */
    TransferOfferResponseBE transferDataOffer(TransferOfferRequestBE request);

    /**
     * Get the enforcement policies from the EDC policies.
     *
     * @param policies edc policies
     * @return enforcement policies
     */
    List<EnforcementPolicy> getEnforcementPoliciesFromEdcPolicies(List<Policy> policies);
}
