package eu.possiblex.participantportal.business.control;

import java.util.Map;

public class CommonPortalServiceFake implements CommonPortalService{
    public static final String PARTICIPANT_DID = "participantDid";

    public static final String PARTICIPANT_NAME = "participantName";

    @Override
    public Map<String, String> getNameMapping() {

        return Map.of(PARTICIPANT_DID, PARTICIPANT_NAME);
    }
}
