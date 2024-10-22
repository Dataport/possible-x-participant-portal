package eu.possiblex.participantportal.business.entity.edc.policy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import eu.possiblex.participantportal.business.entity.common.JsonLdConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public enum OdrlOperator {
    EQ, IN;

    private static final Map<String, OdrlOperator> operatorMap = new HashMap<>();

    static {
        operatorMap.put(JsonLdConstants.ODRL_PREFIX + "eq", EQ);
        operatorMap.put(JsonLdConstants.ODRL_PREFIX + "in", IN);
    }

    @JsonCreator
    public static OdrlOperator forValue(String value) {

        return operatorMap.get(StringUtils.lowerCase(value));
    }

    @JsonValue
    public String toValue() {

        for (Map.Entry<String, OdrlOperator> entry : operatorMap.entrySet()) {
            if (entry.getValue() == this)
                return entry.getKey();
        }

        return null; // or fail
    }
}
