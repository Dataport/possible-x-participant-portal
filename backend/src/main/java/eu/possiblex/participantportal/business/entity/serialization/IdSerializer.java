package eu.possiblex.participantportal.business.entity.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class IdSerializer extends StdSerializer<String> {

    public IdSerializer() {

        this(null);
    }

    public IdSerializer(Class<String> t) {

        super(t);
    }

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("@id", s);
        jsonGenerator.writeEndObject();
    }
}
