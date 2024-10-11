package eu.possible_x.edc_orchestrator.service;

import eu.possible_x.edc_orchestrator.entities.fh.FhIdResponse;
import eu.possible_x.edc_orchestrator.entities.fh.catalog.DatasetToCatalogRequest;

import java.util.Map;

public class FhCatalogClientFake implements FhCatalogClient {
  @Override
  public FhIdResponse addDatasetToFhCatalog(Map<String, String> auth, DatasetToCatalogRequest datasetToCatalogRequest,
      String cat_name, String value_type) {
    return new FhIdResponse("id");
  }
}
