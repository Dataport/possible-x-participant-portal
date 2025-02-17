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

import eu.possiblex.participantportal.business.entity.daps.OmejdnConnectorDetailsBE;

import java.util.Collection;
import java.util.Map;

public class OmejdnConnectorApiClientFake implements OmejdnConnectorApiClient {

    public static final String PARTICIPANT_ID = "participantId";

    public static final String PARTICIPANT_NAME = "Some Participant";

    public static final String OTHER_PARTICIPANT_ID = "otherParticipantId";

    public static final String OTHER_PARTICIPANT_NAME = "Other Participant";

    @Override
    public Map<String, OmejdnConnectorDetailsBE> getConnectorDetails(Collection<String> clientIds) {

        return Map.of(PARTICIPANT_ID,
            OmejdnConnectorDetailsBE.builder().clientId(PARTICIPANT_ID).clientName(PARTICIPANT_NAME)
                .attributes(Map.of("did", "did:web:123")).build(), OTHER_PARTICIPANT_ID,
            OmejdnConnectorDetailsBE.builder().clientId(OTHER_PARTICIPANT_ID).clientName(OTHER_PARTICIPANT_NAME)
                .attributes(Map.of("did", "did:web:456")).build());

    }
}
