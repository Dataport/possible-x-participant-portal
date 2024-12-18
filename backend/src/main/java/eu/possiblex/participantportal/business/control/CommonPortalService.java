package eu.possiblex.participantportal.business.control;

import java.util.Map;

public interface CommonPortalService {

    /**
     * Get the id to name mapping for all participants that are published in the catalogue.
     * @return the id to name mapping for all participants.
     */
    Map<String, String> getNameMapping();
}
