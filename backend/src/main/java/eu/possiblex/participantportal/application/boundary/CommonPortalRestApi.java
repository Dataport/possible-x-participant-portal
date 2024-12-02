package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.entity.VersionTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/common")
public interface CommonPortalRestApi {
    @GetMapping(value = "/version", produces = MediaType.APPLICATION_JSON_VALUE)
    /**
     * Get the version of the participant portal.
     * @return the version of the participant portal
     */
    VersionTO getVersion();
}
