package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.entity.VersionTO;
import eu.possiblex.participantportal.business.control.CommonPortalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class CommonPortalRestApiImpl implements CommonPortalRestApi {

    @Value("${version.no}")
    private String version;

    @Value("${version.date}")
    private String versionDate;

    private final CommonPortalService commonPortalService;

    public CommonPortalRestApiImpl(@Autowired CommonPortalService commonPortalService) {

        this.commonPortalService = commonPortalService;
    }

    @Override
    public VersionTO getVersion() {
        return new VersionTO(version, versionDate);
    }

    @Override
    public Map<String, String> getNameMapping() {

        return commonPortalService.getNameMapping();
    }
}
