package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.fh.FhIdResponse;
import eu.possiblex.participantportal.business.entity.fh.catalog.DatasetToCatalogRequest;

import java.util.Map;

public class FhCatalogClientFake implements FhCatalogClient {
    @Override
    public FhIdResponse addDatasetToFhCatalog(Map<String, String> auth, DatasetToCatalogRequest datasetToCatalogRequest,
        String cat_name, String value_type) {

        return new FhIdResponse("id");
    }
}
