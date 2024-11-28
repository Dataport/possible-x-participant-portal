package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.daps.OmejdnConnectorDetailsBE;
import eu.possiblex.participantportal.business.entity.daps.OmejdnConnectorDetailsRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Map;

public interface OmejdnConnectorApiClient {

    @PostExchange("/details")
    Map<String, OmejdnConnectorDetailsBE> getConnectorDetails(@RequestBody OmejdnConnectorDetailsRequest request);
}
