package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogOffer;
import eu.possiblex.participantportal.business.entity.fh.catalog.DcatDataset;

import java.util.Map;

public class FhCatalogClientFake implements FHCatalogClient {
    @Override
    public FhCatalogIdResponse addDatasetToFhCatalog(DcatDataset datasetToCatalogRequest) {
        return new FhCatalogIdResponse("id");
    }

    @Override
    public FhCatalogOffer getFhCatalogOffer(String datasetId) {
        return null;
    }
}
