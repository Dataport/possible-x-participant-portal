package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogOffer;
import eu.possiblex.participantportal.business.entity.selfdescriptions.px.PxExtendedServiceOfferingCredentialSubject;

public interface FhCatalogClient {
    FhCatalogIdResponse addServiceOfferingToFhCatalog(
        PxExtendedServiceOfferingCredentialSubject serviceOfferingCredentialSubject);

    FhCatalogOffer getFhCatalogOffer(String datasetId) throws OfferNotFoundException;
}
