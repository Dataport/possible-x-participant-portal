package eu.possiblex.participantportal.business.control;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;
import java.util.Map;

public interface SdCreationWizardApiClient {
    @GetExchange("/getAvailableShapesCategorized")
    Map<String, List<String>> getAvailableShapesCategorized(@RequestParam(name = "ecosystem") String ecosystem);

    @GetExchange("/getJSON")
    String getJSON(@RequestParam(name = "ecosystem") String ecosystem, @RequestParam(name = "name") String name);

    // not implemented: GET "/getAvailableShapes"
    // not implemented: POST "/convertFile"
    // not implemented: GET "/getSearchQuery/{ecoSystem}/{query}"
}
