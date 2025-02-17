/*
 *  Copyright 2024-2025 Dataport. All rights reserved. Developed as part of the POSSIBLE project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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