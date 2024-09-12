package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogOffer;
import eu.possiblex.participantportal.business.entity.fh.catalog.DcatDataset;

public interface FHCatalogClient {
    FhCatalogIdResponse addDatasetToFhCatalog(DcatDataset datasetToCatalogRequest);

    FhCatalogOffer getFhCatalogOffer(String datasetId);
}
