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

import eu.possiblex.participantportal.business.entity.*;

public interface ConsumerService {
    /**
     * Given a request for an offer, select it and return the details of this offer from the data transfer component.
     *
     * @param request request for selecting the offer
     * @return details of the offer
     */
    SelectOfferResponseBE selectContractOffer(SelectOfferRequestBE request);

    /**
     * Given a request for an offer, accept the offer on the data transfer component and perform the transfer.
     *
     * @param request request for accepting the offer
     * @return final result of the transfer
     */
    AcceptOfferResponseBE acceptContractOffer(ConsumeOfferRequestBE request);

    /**
     * Given a request for a transfer, transfer the data using the data transfer component.
     *
     * @param request request for transferring the data
     * @return final result of the transfer
     */
    TransferOfferResponseBE transferDataOffer(TransferOfferRequestBE request);
}
