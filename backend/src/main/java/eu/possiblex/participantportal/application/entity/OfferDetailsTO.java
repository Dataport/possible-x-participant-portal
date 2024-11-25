package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferDetailsTO {
    /**
     * The ID which is used to identify the offer in the EDC Catalog. Currently this is the asset-ID, because an asset
     * will only be used in one offer.
     */
    private String edcOfferId;

    /**
     * The content of the offering as retrieved from the catalog.
     */
    private PxExtendedServiceOfferingCredentialSubject catalogOffering;

    /**
     * Does this offer contain Data Resources.
     */
    private boolean dataOffering;

    /**
     * The enforcement policies for this offer.
     */
    private List<EnforcementPolicy> enforcementPolicies;
}
