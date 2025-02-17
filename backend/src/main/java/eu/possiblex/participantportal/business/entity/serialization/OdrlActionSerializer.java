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

package eu.possiblex.participantportal.business.entity.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import eu.possiblex.participantportal.business.entity.edc.policy.OdrlAction;

import java.io.IOException;

public class OdrlActionSerializer extends StdSerializer<OdrlAction> {

    public OdrlActionSerializer() {

        this(null);
    }

    public OdrlActionSerializer(Class<OdrlAction> t) {

        super(t);
    }

    @Override
    public void serialize(OdrlAction s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {

        jsonGenerator.writeStartObject();
        // access JsonProperty of enum field and write as string
        jsonGenerator.writeStringField("odrl:type", s.toValue());
        jsonGenerator.writeEndObject();
    }
}
