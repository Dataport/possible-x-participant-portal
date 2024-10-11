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

package eu.possible_x.edc_orchestrator.entities.edc.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.possible_x.edc_orchestrator.entities.edc.EdcConstants;
import eu.possible_x.edc_orchestrator.entities.edc.asset.DataAddress;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
@Builder
public class TransferRequest {
    private static final Map<String, String> CONTEXT = EdcConstants.EDC_CONTEXT;

    private static final String TYPE = "TransferRequestDto";

    private static final boolean MANAGED_RESOURCES = true;

    private static final String PROTOCOL = "dataspace-protocol-http";

    private String connectorId;
    private String counterPartyAddress;
    private String contractId;
    private String assetId;
    private DataAddress dataDestination;

    @JsonProperty("@type")
    public String getType() {
        return TYPE;
    }

    @JsonProperty("protocol")
    public String getProtocol() {
        return PROTOCOL;
    }

    @JsonProperty("@context")
    public Map<String, String> getContext() {
        return CONTEXT;
    }

    @JsonProperty("managedResources")
    public boolean isManagedResources() {
        return MANAGED_RESOURCES;
    }
}
