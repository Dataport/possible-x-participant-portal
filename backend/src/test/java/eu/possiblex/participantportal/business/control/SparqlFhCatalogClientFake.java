package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.utils.TestUtils;

public class SparqlFhCatalogClientFake implements SparqlFhCatalogClient {

    private final String sparqlQueryResultString;

    public SparqlFhCatalogClientFake() {

        this.sparqlQueryResultString = TestUtils.loadTextFile(
            "unit_tests/ConsumerModuleTest/validSparqlResultParticipant.json");
    }

    @Override
    public String queryCatalog(String query, String format) {

        return sparqlQueryResultString;
    }
}
