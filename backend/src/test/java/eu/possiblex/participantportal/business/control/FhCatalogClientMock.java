package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogOffer;

public class FhCatalogClientMock implements FhCatalogClient {
    @Override
    public FhCatalogIdResponse addServiceOfferingToFhCatalog(
        PxExtendedServiceOfferingCredentialSubject serviceOfferingCredentialSubject) {

        return new FhCatalogIdResponse("id");
    }

    @Override
    public FhCatalogOffer getFhCatalogOffer(String datasetId) {

        return null;
    }
}
