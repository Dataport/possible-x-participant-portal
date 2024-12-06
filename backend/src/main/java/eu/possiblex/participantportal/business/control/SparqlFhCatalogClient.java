package eu.possiblex.participantportal.business.control;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface SparqlFhCatalogClient {
    /*
     * GET Request to the sparql endpoint which makes a sparql query.
     * @param query the sparql query. It defines which information will be retrieved.
     * @param format the format of the response. Default is application/sparql-results+json.
     */
    @GetExchange("/")
    String queryCatalog(@RequestParam String query,
        @RequestParam(required = false, defaultValue = "application/sparql-results+json") String format);
}
