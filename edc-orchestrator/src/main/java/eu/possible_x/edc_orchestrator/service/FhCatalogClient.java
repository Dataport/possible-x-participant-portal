/*
 *  Copyright 2024 Dataport. All rights reserved. Developed as part of the MERLOT project.
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
 *
 * Modifications:
 * - Dataport (part of the POSSIBLE project) - 14 August, 2024 - Adjust package names and imports
 */

package eu.possible_x.edc_orchestrator.service;

import eu.possible_x.edc_orchestrator.entities.fh.FhIdResponse;
import eu.possible_x.edc_orchestrator.entities.fh.catalog.DatasetToCatalogRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Map;

public interface FhCatalogClient {
    @PostExchange("/catalogues/{cat_name}/datasets")
    FhIdResponse addDatasetToFhCatalog(@RequestHeader Map<String, String> auth, @RequestBody DatasetToCatalogRequest datasetToCatalogRequest, @PathVariable String cat_name, @RequestParam String value_type);

}

