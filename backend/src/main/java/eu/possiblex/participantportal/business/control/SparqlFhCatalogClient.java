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
