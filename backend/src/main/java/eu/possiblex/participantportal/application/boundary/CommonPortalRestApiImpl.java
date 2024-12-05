package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.entity.VersionTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CommonPortalRestApiImpl implements CommonPortalRestApi {

    @Value("${version.no}")
    private String version;

    @Value("${version.date}")
    private String versionDate;

    @Override
    public VersionTO getVersion() {
        return new VersionTO(version, versionDate);
    }
}
