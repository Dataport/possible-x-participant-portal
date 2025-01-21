package eu.possiblex.participantportal.business.entity.edc.policy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import eu.possiblex.participantportal.business.entity.common.JsonLdConstants;

import java.util.HashMap;
import java.util.Map;

public enum OdrlOperator {
    EQ, GEQ, LEQ, NEQ, IN, IS_ANY_OF, IS_NONE_OF;

    private static final Map<String, OdrlOperator> operatorMap = new HashMap<>();

    static {
        operatorMap.put(JsonLdConstants.ODRL_PREFIX + "eq", EQ);
        operatorMap.put(JsonLdConstants.ODRL_PREFIX + "gteq", GEQ);
        operatorMap.put(JsonLdConstants.ODRL_PREFIX + "lteq", LEQ);
        operatorMap.put(JsonLdConstants.ODRL_PREFIX + "neq", NEQ);
        operatorMap.put(JsonLdConstants.ODRL_PREFIX + "isPartOf", IN);
        operatorMap.put(JsonLdConstants.ODRL_PREFIX + "isAnyOf", IS_ANY_OF);
        operatorMap.put(JsonLdConstants.ODRL_PREFIX + "isNoneOf", IS_ANY_OF);
    }

    @JsonCreator
    public static OdrlOperator forValue(String value) {

        return operatorMap.get(value);
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
