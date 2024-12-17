package eu.possiblex.participantportal.application.entity;

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
public class OfferWithTimestampTO {
    private PxExtendedServiceOfferingCredentialSubject catalogOffering;

    private OffsetDateTime offerRetrievalDate;
}
