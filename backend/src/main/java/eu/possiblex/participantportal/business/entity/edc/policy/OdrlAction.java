package eu.possiblex.participantportal.business.entity.edc.policy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import eu.possiblex.participantportal.business.entity.common.JsonLdConstants;

import java.util.HashMap;
import java.util.Map;

public enum OdrlAction {
    USE, TRANSFER;

    private static final Map<String, OdrlAction> actionMap = new HashMap<>();

    static {
        actionMap.put(JsonLdConstants.ODRL_PREFIX + "use", USE);
        actionMap.put(JsonLdConstants.ODRL_PREFIX + "transfer", TRANSFER);
    }

    @JsonCreator
    public static OdrlAction forValue(String value) {

        return actionMap.get(value);
    }

    @JsonValue
    public String toValue() {

        for (Map.Entry<String, OdrlAction> entry : actionMap.entrySet()) {
            if (entry.getValue() == this)
                return entry.getKey();
        }

        return null; // or fail
    }
}
