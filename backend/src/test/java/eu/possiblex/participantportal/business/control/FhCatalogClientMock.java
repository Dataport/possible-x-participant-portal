package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogOffer;
import eu.possiblex.participantportal.business.entity.fh.catalog.DcatDataset;
import eu.possiblex.participantportal.business.entity.selfdescriptions.px.PxExtendedServiceOfferingCredentialSubject;

public class FhCatalogClientMock implements FhCatalogClient {
    @Override
    public FhCatalogIdResponse addDatasetToFhCatalog(DcatDataset datasetToCatalogRequest) {

        return new FhCatalogIdResponse("id");
    }

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
