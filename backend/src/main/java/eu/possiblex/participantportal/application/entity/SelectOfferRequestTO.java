package eu.possiblex.participantportal.application.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectOfferRequestTO {

    /**
     * The offer ID from the offer in the FH Catalog.
     */
    private String fhCatalogOfferId;
}

