package eu.possiblex.participantportal.business.entity;

import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDataset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectOfferResponseBE {
    /**
     * The offer from the EDC Catalog which was selected.
     */
    private DcatDataset edcOffer;

    /**
     * The URL of the EDC Connector of the provider.
     */
    private String counterPartyAddress;
}
