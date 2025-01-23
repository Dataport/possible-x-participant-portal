package eu.possiblex.participantportal.business.control;

import java.util.List;
import java.util.Map;

public interface SdCreationWizardApiService {
    /**
     * Return the map of shape-type:filename for a given ecosystem.
     *
     * @param ecosystem ecosystem to filter for
     * @return map of filenames as described above
     */
    Map<String, List<String>> getShapesByEcosystem(String ecosystem);

    /**
     * Return a list of service offering shape files for the given ecosystem.
     *
     * @param ecosystem ecosystem to filter for
     * @return list of service offering shape JSON files
     */
    List<String> getServiceOfferingShapesByEcosystem(String ecosystem);

    /**
     * Return a list of participant shape files for the given ecosystem.
     *
     * @param ecosystem ecosystem to filter for
     * @return list of resource shape JSON files
     */
    List<String> getResourceShapesByEcosystem(String ecosystem);

    /**
     * Given a JSON file name, return the corresponding JSON shape file
     *
     * @param ecosystem ecosystem of shape
     * @param jsonName JSON file name
     * @return JSON file
     */
    String getShapeByName(String ecosystem, String jsonName);
}
