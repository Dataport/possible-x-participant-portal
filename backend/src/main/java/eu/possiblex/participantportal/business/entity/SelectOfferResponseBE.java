package eu.possiblex.participantportal.business.entity;

import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDataset;
import lombok.AllArgsConstructor;
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
     * The content of the offering as retrieved from the catalog.
     */
    private PxExtendedServiceOfferingCredentialSubject catalogOffering;

    /**
     * Does this offer contain Data Resources.
     */
    private boolean dataOffering;
}
