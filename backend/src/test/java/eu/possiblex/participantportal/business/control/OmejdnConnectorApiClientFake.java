package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.daps.OmejdnConnectorDetailsBE;

import java.util.Collection;
import java.util.Map;

public class OmejdnConnectorApiClientFake implements OmejdnConnectorApiClient {

    @Override
    public Map<String, OmejdnConnectorDetailsBE> getConnectorDetails(Collection<String> clientIds) {

        return Map.of();
    }
}
