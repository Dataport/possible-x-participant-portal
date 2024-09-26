package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.business.entity.edc.negotiation.NegotiationState;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcessState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcceptOfferResponseTO {

    private TransferProcessState transferProcessState;

    private NegotiationState negotiationState;
    /**
     * Does this offer contain Data Resources.
     */
    private boolean dataOffering;
}
