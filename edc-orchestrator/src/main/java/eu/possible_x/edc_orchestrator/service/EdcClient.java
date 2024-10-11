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

import eu.possible_x.edc_orchestrator.entities.edc.asset.AssetCreateRequest;
import eu.possible_x.edc_orchestrator.entities.edc.catalog.CatalogRequest;
import eu.possible_x.edc_orchestrator.entities.edc.catalog.DcatCatalog;
import eu.possible_x.edc_orchestrator.entities.edc.common.IdResponse;
import eu.possible_x.edc_orchestrator.entities.edc.contractdefinition.ContractDefinitionCreateRequest;
import eu.possible_x.edc_orchestrator.entities.edc.negotiation.ContractNegotiation;
import eu.possible_x.edc_orchestrator.entities.edc.negotiation.NegotiationInitiateRequest;
import eu.possible_x.edc_orchestrator.entities.edc.policy.PolicyCreateRequest;
import eu.possible_x.edc_orchestrator.entities.edc.transfer.IonosS3TransferProcess;
import eu.possible_x.edc_orchestrator.entities.edc.transfer.TransferRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface EdcClient {
    @PostExchange("/v3/assets")
    IdResponse createAsset(@RequestBody AssetCreateRequest assetCreateRequest);

    @PostExchange("/v2/policydefinitions")
    IdResponse createPolicy(@RequestBody PolicyCreateRequest policyCreateRequest);

    @PostExchange("/v2/contractdefinitions")
    IdResponse createContractDefinition(@RequestBody ContractDefinitionCreateRequest contractDefinitionCreateRequest);

    @PostExchange("/v2/catalog/request")
    DcatCatalog queryCatalog(@RequestBody CatalogRequest catalogRequest);

    @PostExchange("/v2/contractnegotiations")
    IdResponse negotiateOffer(@RequestBody NegotiationInitiateRequest negotiationInitiateRequest);

    @GetExchange("/v2/contractnegotiations/{negotiationId}")
    ContractNegotiation checkOfferStatus(@PathVariable String negotiationId);

    @PostExchange("/v2/transferprocesses")
    IdResponse initiateTransfer(@RequestBody TransferRequest transferRequest);

    @GetExchange("/v2/transferprocesses/{transferId}")
    IonosS3TransferProcess checkTransferStatus(@PathVariable String transferId);

    @PostExchange("/v2/transferprocesses/{transferId}/deprovision")
    void deprovisionTransfer(@PathVariable String transferId);

    @DeleteExchange("/v2/contractdefinitions/{contractDefinitionId}")
    void revokeContractDefinition(@PathVariable String contractDefinitionId);
}

