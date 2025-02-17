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

import eu.possiblex.participantportal.application.control.ContractApiMapper;
import eu.possiblex.participantportal.application.entity.*;
import eu.possiblex.participantportal.business.control.ContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing contract-related operations.
 */
@RestController
@Slf4j
public class ContractRestApiImpl implements ContractRestApi {
    private final ContractService contractService;

    private final ContractApiMapper contractApiMapper;

    public ContractRestApiImpl(@Autowired ContractService contractService,
        @Autowired ContractApiMapper contractApiMapper) {

        this.contractService = contractService;
        this.contractApiMapper = contractApiMapper;
    }

    /**
     * GET request for retrieving all contract agreements.
     *
     * @return list of contract agreements
     */
    @Override
    public List<ContractAgreementTO> getContractAgreements() {

        return contractService.getContractAgreements().stream().map(contractApiMapper::contractAgreementBEToTO)
            .toList();
    }

    @Override
    public ContractDetailsTO getContractDetailsByContractAgreementId(String contractAgreementId) {

        return contractApiMapper.contractDetailsBEToTO(
            contractService.getContractDetailsByContractAgreementId(contractAgreementId));

    }

    @Override
    public OfferWithTimestampTO getOfferWithTimestampByContractAgreementId(String contractAgreementId) {

        return contractApiMapper.offerRetrievalResponseBEToOfferWithTimestampTO(
            contractService.getOfferDetailsByContractAgreementId(contractAgreementId));
    }
}
