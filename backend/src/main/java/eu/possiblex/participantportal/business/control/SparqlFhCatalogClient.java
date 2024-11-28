package eu.possiblex.participantportal.business.control;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface SparqlFhCatalogClient {
    @GetExchange()
    String queryCatalog(@RequestParam String query,
        @RequestParam(required = false, defaultValue = "application/sparql-results+json") String format);
}
