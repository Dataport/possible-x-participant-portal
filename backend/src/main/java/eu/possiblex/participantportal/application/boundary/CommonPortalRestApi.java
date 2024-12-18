package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.entity.VersionTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/common")
public interface CommonPortalRestApi {
    /**
     * Get the version of the participant portal.
     * @return the version of the participant portal
     */
    @GetMapping(value = "/version", produces = MediaType.APPLICATION_JSON_VALUE)
    VersionTO getVersion();

    /**
     * Get the id to name mapping for all participants that are published in the catalogue.
     * @return the id to name mapping for all participants.
     */
    @GetMapping(value = "/participant/name-mapping", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, String> getNameMapping();
}
