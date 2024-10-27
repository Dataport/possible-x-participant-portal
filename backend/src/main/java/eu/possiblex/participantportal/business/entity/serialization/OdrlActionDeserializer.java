package eu.possiblex.participantportal.business.entity.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import eu.possiblex.participantportal.business.entity.edc.policy.OdrlAction;

import java.io.IOException;

public class OdrlActionDeserializer extends StdDeserializer<OdrlAction> {

    public OdrlActionDeserializer() {

        this(null);
    }

    public OdrlActionDeserializer(Class<?> vc) {

        super(vc);
    }

    @Override
    public OdrlAction deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        if (node.get("odrl:type") != null) {
            return OdrlAction.forValue(node.get("odrl:type").textValue());
        }
        return null;
    }
}
