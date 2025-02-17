/*
 *  Copyright 2024-2025 Dataport. All rights reserved. Developed as part of the POSSIBLE project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
