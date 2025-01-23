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
 */

package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.edc.transfer.TerminateTransferRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class EdcTransferDeprovisionTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(EdcTransferDeprovisionTask.class);

    private final EdcClient edcClient;

    private final String transferId;

    public EdcTransferDeprovisionTask(EdcClient edcClient, String transferId) {

        this.edcClient = edcClient;
        this.transferId = transferId;
    }

    @Override
    public void run() {

        logger.info("Deprovisioning transfer with id {}", transferId);

        try {
            this.edcClient.terminateTransfer(this.transferId,
                TerminateTransferRequest.builder().reason("Transfer timed out.").build());
        } catch (WebClientResponseException.Conflict e) {
            logger.info("Did not terminate transfer with id {} as it is already in some terminated state", transferId);
        } catch (Exception e) {
            logger.error("Failed to terminate transfer with id {}", transferId, e);
        }

        try {
            this.edcClient.deprovisionTransfer(this.transferId);
        } catch (Exception e) {
            logger.error("Failed to deprovision transfer with id {}", transferId, e);
        }
    }
}