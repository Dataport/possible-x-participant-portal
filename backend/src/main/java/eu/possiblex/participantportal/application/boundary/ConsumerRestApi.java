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
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/consumer")
public interface ConsumerRestApi {
    @Operation(summary = "Select an offer", tags = {
        "ConsumeOffer" }, description = "Select an offer and retrieve the offering details for the given catalog offer ID.")
    @PostMapping(value = "/offer/select", produces = MediaType.APPLICATION_JSON_VALUE)
    OfferDetailsTO selectContractOffer(@Valid @RequestBody SelectOfferRequestTO request);

    @Operation(summary = "Accept an offer", tags = {
        "ConsumeOffer" }, description = "Accept an offer and establish a contract agreement with the given counter party and EDC offer ID.")
    @PostMapping(value = "/offer/accept", produces = MediaType.APPLICATION_JSON_VALUE)
    AcceptOfferResponseTO acceptContractOffer(@Valid @RequestBody ConsumeOfferRequestTO request);

    @Operation(summary = "Initiate a data transfer", tags = {
        "ConsumeOffer" }, description = "Initiate a data transfer for an offering with the given counter party, contract agreement and EDC offer ID.")
    @PostMapping(value = "/offer/transfer", produces = MediaType.APPLICATION_JSON_VALUE)
    TransferOfferResponseTO transferDataOffer(@Valid @RequestBody TransferOfferRequestTO request);
}