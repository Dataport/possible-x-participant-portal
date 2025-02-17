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

package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.entity.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/contract")
public interface ContractRestApi {
    /**
     * GET request for retrieving all contract agreements.
     *
     * @return list of contract agreements
     */
    @GetMapping(value = "/agreement", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ContractAgreementTO> getContractAgreements();

    /**
     * GET request for retrieving contract details by contract agreement id.
     *
     * @return contract details
     */
    @GetMapping(value = "/details/{contractAgreementId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ContractDetailsTO getContractDetailsByContractAgreementId(@PathVariable String contractAgreementId);

    /**
     * GET request for retrieving the referenced offer with timestamp by contract agreement id.
     *
     * @return Referenced offer with timestamp
     */
    @GetMapping(value = "/details/{contractAgreementId}/offer", produces = MediaType.APPLICATION_JSON_VALUE)
    OfferWithTimestampTO getOfferWithTimestampByContractAgreementId(@PathVariable String contractAgreementId);
}
