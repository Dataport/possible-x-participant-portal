package eu.possiblex.participantportal.business.entity.fh;

import lombok.*;

/**
 * The (relevant) contents of an offer of the FH Catalog.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FhCatalogOffer {
    /**
     * The ID of the asset which is offered.
     */
    private String assetId;
    /**
     * The URL for the provider EDC Connector.
     */
    private String counterPartyAddress;

    /**
     * The number of data resources in the offer.
     */
    private int dataResourceCount;
}
