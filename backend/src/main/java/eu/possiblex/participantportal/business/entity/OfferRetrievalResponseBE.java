package eu.possiblex.participantportal.business.entity;

import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferRetrievalResponseBE {
    private PxExtendedServiceOfferingCredentialSubject catalogOffering;

    private OffsetDateTime offerRetrievalDate;
}
