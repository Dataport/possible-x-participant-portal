package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Offering credential subject as retrieved from the catalog")
    private PxExtendedServiceOfferingCredentialSubject catalogOffering;

    @Schema(description = "Date when the offering was retrieved")
    private OffsetDateTime offerRetrievalDate;
}
