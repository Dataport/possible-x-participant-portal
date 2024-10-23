package eu.possiblex.participantportal.business.entity.edc.policy;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.possiblex.participantportal.business.entity.common.JsonLdConstants;

public enum OdrlAction {
    @JsonProperty(JsonLdConstants.ODRL_PREFIX + "use") USE, @JsonProperty(
        JsonLdConstants.ODRL_PREFIX + "transfer") TRANSFER;
}
