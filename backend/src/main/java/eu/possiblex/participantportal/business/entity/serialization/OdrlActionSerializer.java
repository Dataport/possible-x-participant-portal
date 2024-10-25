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
