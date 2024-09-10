package eu.possiblex.participantportal.application.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferDetailsTO {
    /**
     * The ID of the offer in the EDC Catalog, which corresponds to the offer that the user has chosen in the FH Catalog.
     */
    private String edcOfferId;
    private String offerType;
    private OffsetDateTime creationDate;
    private String name;
    private String description;
    private String contentType;
}
