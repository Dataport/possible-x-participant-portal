package eu.possiblex.participantportal.business.control;

import java.util.List;
import java.util.Map;

public class SdCreationWizardApiServiceFake implements SdCreationWizardApiService {
    /**
     * Return the map of shape-type:filename for a given ecosystem.
     *
     * @param ecosystem ecosystem to filter for
     * @return map of filenames as described above
     */
    @Override
    public Map<String, List<String>> getShapesByEcosystem(String ecosystem) {

        return Map.of("Resource", List.of("Resource1.json"), "Service", List.of("Offering1.json", "Offering2.json"));
    }

    /**
     * Return a list of service offering shape files for the given ecosystem.
     *
     * @param ecosystem ecosystem to filter for
     * @return list of service offering shape JSON files
     */
    @Override
    public List<String> getServiceOfferingShapesByEcosystem(String ecosystem) {

        return List.of("Offering1.json");
    }

    /**
     * Return a list of participant shape files for the given ecosystem.
     *
     * @param ecosystem ecosystem to filter for
     * @return list of resource shape JSON files
     */
    @Override
    public List<String> getResourceShapesByEcosystem(String ecosystem) {

        return List.of("Resource1.json");
    }

    /**
     * Given a JSON file name, return the corresponding JSON shape file
     *
     * @param ecosystem ecosystem of shape
     * @param jsonName JSON file name
     * @return JSON file
     */
    @Override
    public String getShapeByName(String ecosystem, String jsonName) {

        return """
            {
                "someKey": "someValue"
            }
            """;
    }
}
