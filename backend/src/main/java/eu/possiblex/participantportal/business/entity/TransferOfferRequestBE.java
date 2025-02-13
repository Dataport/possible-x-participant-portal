package eu.possiblex.participantportal.business.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferOfferRequestBE {
    private String counterPartyAddress;

    private String edcOfferId;

    private String contractAgreementId;

}
