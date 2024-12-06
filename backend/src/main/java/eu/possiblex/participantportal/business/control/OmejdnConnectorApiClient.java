package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.daps.OmejdnConnectorDetailsBE;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.util.Collection;
import java.util.Map;

public interface OmejdnConnectorApiClient {

    @GetExchange()
    Map<String, OmejdnConnectorDetailsBE> getConnectorDetails(@RequestParam("client_id") Collection<String> clientIds);
}
