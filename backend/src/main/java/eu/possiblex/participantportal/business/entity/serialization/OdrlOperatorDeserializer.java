package eu.possiblex.participantportal.business.entity.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import eu.possiblex.participantportal.business.entity.edc.policy.OdrlOperator;

import java.io.IOException;

public class OdrlOperatorDeserializer extends StdDeserializer<OdrlOperator> {

    public OdrlOperatorDeserializer() {

        this(null);
    }

    public OdrlOperatorDeserializer(Class<?> vc) {

        super(vc);
    }

    @Override
    public OdrlOperator deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        if (node.get("@id") != null) {
            return OdrlOperator.forValue(node.get("@id").textValue());
        }
        return null;
    }
}
