package eu.possiblex.participantportal.application.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumeOfferRequestTO {
    /**
     * @see eu.possiblex.participantportal.application.entity.OfferDetailsTO#counterPartyAddress
     */
    private String counterPartyAddress;

    /**
     * @see eu.possiblex.participantportal.application.entity.OfferDetailsTO#edcOfferId
     */
    private String edcOfferId;

    /**
     * Does this offer contain Data Resources.
     */
    private boolean dataOffering;

    /**
     * @see eu.possiblex.participantportal.application.entity.OfferDetailsTO#catalogOffering.providedBy
     */
    private String providedBy;
}
