package eu.possiblex.participantportal.application.entity.policies;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AgreementOffsetUnit {
    SECONDS, MINUTES, HOURS, DAYS;

    private static final Map<String, AgreementOffsetUnit> unitMap = new HashMap<>();

    static {
        unitMap.put("s", SECONDS);
        unitMap.put("m", MINUTES);
        unitMap.put("h", HOURS);
        unitMap.put("d", DAYS);
    }

    @JsonCreator
    public static AgreementOffsetUnit forValue(String value) {

        return unitMap.get(value);
    }

    @JsonValue
    public String toValue() {

        for (Map.Entry<String, AgreementOffsetUnit> entry : unitMap.entrySet()) {
            if (entry.getValue() == this)
                return entry.getKey();
        }

        return null; // or fail
    }
}