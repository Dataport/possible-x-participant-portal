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
     * The ID which is used to identify the offer in the EDC Catalog. Currently this is the asset-ID, because an asset will only be used in one offer.
     */
    private String edcOfferId;
    /**
     * The URL of the EDC Connector of the provider.
     */
    private String counterPartyAddress;
    private String offerType;
    private OffsetDateTime creationDate;
    private String name;
    private String description;
    private String contentType;
}
