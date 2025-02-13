package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.utils.TestUtils;

public class SparqlFhCatalogClientFake implements SparqlFhCatalogClient {

    private final String sparqlQueryParticipantResultString;

    private final String sparqlQueryOfferResultString;

    public SparqlFhCatalogClientFake() {

        this.sparqlQueryParticipantResultString = TestUtils.loadTextFile(
            "unit_tests/ConsumerModuleTest/validSparqlResultParticipant.json");

        this.sparqlQueryOfferResultString = TestUtils.loadTextFile(
            "unit_tests/ContractModuleTest/validSparqlResultOffer.json");
    }

    @Override
    public String queryCatalog(String query, String format) {

        if (query.contains("LegalParticipant")) {
            return sparqlQueryParticipantResultString;
        } else if (query.contains("Offer")) {
            return sparqlQueryOfferResultString;
        }

        return """
            {
                "results": {
                    "bindings": []
                }
            }
            """;
    }
}
