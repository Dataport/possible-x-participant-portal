package eu.possiblex.participantportal.business.control;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SdCreationWizardApiServiceImpl implements SdCreationWizardApiService {

    private final SdCreationWizardApiClient sdCreationWizardApiClient;

    public SdCreationWizardApiServiceImpl(@Autowired SdCreationWizardApiClient sdCreationWizardApiClient) {

        this.sdCreationWizardApiClient = sdCreationWizardApiClient;
    }

    /**
     * Return the map of shape-type:filename for a given ecosystem.
     *
     * @param ecosystem ecosystem to filter for
     * @return map of filenames as described above
     */
    @Override
    public Map<String, List<String>> getShapesByEcosystem(String ecosystem) {

        return sdCreationWizardApiClient.getAvailableShapesCategorized(ecosystem);
    }

    /**
     * Return a list of service offering shape files for the given ecosystem.
     *
     * @param ecosystem ecosystem to filter for
     * @return list of service offering shape JSON files
     */
    @Override
    public List<String> getServiceOfferingShapesByEcosystem(String ecosystem) {

        return sdCreationWizardApiClient.getAvailableShapesCategorized(ecosystem).get("Service");
    }

    /**
     * Return a list of participant shape files for the given ecosystem.
     *
     * @param ecosystem ecosystem to filter for
     * @return list of resource shape JSON files
     */
    @Override
    public List<String> getResourceShapesByEcosystem(String ecosystem) {

        return sdCreationWizardApiClient.getAvailableShapesCategorized(ecosystem).get("Resource");
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

        return sdCreationWizardApiClient.getJSON(ecosystem, jsonName);
    }
}
