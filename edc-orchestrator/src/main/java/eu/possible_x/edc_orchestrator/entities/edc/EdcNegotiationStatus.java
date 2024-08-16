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

package eu.possible_x.edc_orchestrator.entities.edc;

import eu.possible_x.edc_orchestrator.entities.edc.negotiation.ContractNegotiation;
import lombok.Getter;

@Getter
public class EdcNegotiationStatus {

    private final String id;

    private final String state;

    private final String contractAgreementId;

    public EdcNegotiationStatus(ContractNegotiation negotiation) {
        this.id = negotiation.getId();
        this.state = negotiation.getState();
        this.contractAgreementId = negotiation.getContractAgreementId();
    }
}
