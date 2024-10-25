package eu.possiblex.participantportal.business.entity.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import eu.possiblex.participantportal.business.entity.edc.policy.OdrlOperator;

import java.io.IOException;

public class OdrlOperatorSerializer extends StdSerializer<OdrlOperator> {

    public OdrlOperatorSerializer() {

        this(null);
    }

    public OdrlOperatorSerializer(Class<OdrlOperator> t) {

        super(t);
    }

    @Override
    public void serialize(OdrlOperator s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {

        jsonGenerator.writeStartObject();
        // access JsonProperty of enum field and write as string
        jsonGenerator.writeStringField("@id", s.toValue());
        jsonGenerator.writeEndObject();
    }
}
