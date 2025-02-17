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
