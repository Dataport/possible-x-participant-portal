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
