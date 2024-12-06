package eu.possiblex.participantportal.business.entity.fh;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class CatalogLiteralDeserializer extends StdDeserializer<String> {

    public CatalogLiteralDeserializer() {

        this(null);
    }

    public CatalogLiteralDeserializer(Class<?> vc) {

        super(vc);
    }

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        if (node.get("type") != null && (node.get("type").textValue().equals("literal")
            || node.get("type").textValue().equals("uri"))) {
            return node.get("value").textValue();
        }
        return node.textValue();
    }
}
