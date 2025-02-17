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

import eu.possiblex.participantportal.application.control.ConsumerApiMapper;
import eu.possiblex.participantportal.application.entity.*;
import eu.possiblex.participantportal.business.control.ConsumerService;
import eu.possiblex.participantportal.business.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ConsumerRestApiImpl implements ConsumerRestApi {

    private final ConsumerService consumerService;

    private final ConsumerApiMapper consumerApiMapper;

    public ConsumerRestApiImpl(@Autowired ConsumerService consumerService,
        @Autowired ConsumerApiMapper consumerApiMapper) {

        this.consumerService = consumerService;
        this.consumerApiMapper = consumerApiMapper;
    }

    @Override
    public OfferDetailsTO selectContractOffer(@RequestBody SelectOfferRequestTO request) {

        log.info("selecting contract with {}", request);
        SelectOfferRequestBE be = consumerApiMapper.selectOfferRequestTOToBE(request);
        SelectOfferResponseBE response = consumerService.selectContractOffer(be);

        log.info("returning for selecting contract: {}", response);
        return consumerApiMapper.selectOfferResponseBEToOfferDetailsTO(response);
    }

    @Override
    public AcceptOfferResponseTO acceptContractOffer(@RequestBody ConsumeOfferRequestTO request) {

        log.info("accepting contract with {}", request);
        ConsumeOfferRequestBE be = consumerApiMapper.consumeOfferRequestTOToBE(request);

        AcceptOfferResponseBE acceptOffer = consumerService.acceptContractOffer(be);
        AcceptOfferResponseTO response = consumerApiMapper.acceptOfferResponseBEToAcceptOfferResponseTO(acceptOffer);
        log.info("Returning for accepting contract: {}", response);
        return response;
    }

    @Override
    public TransferOfferResponseTO transferDataOffer(@RequestBody TransferOfferRequestTO request) {

        log.info("transferring data from contract with {}", request);
        TransferOfferRequestBE be = consumerApiMapper.transferOfferRequestTOToBE(request);

        TransferOfferResponseBE transferOfferResponseBE = consumerService.transferDataOffer(be);

        TransferOfferResponseTO response = consumerApiMapper.transferOfferResponseBEToTransferOfferResponseTO(
            transferOfferResponseBE);
        log.info("Returning for transferring data of contract: {}", response);
        return response;
    }
}