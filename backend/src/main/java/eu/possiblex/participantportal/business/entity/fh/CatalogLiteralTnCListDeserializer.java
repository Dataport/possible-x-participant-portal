package eu.possiblex.participantportal.business.entity.fh;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.List;

public class CatalogLiteralTnCListDeserializer extends StdDeserializer<List<TermsAndConditions>> {

    public CatalogLiteralTnCListDeserializer() {

        this(null);
    }

    public CatalogLiteralTnCListDeserializer(Class<?> vc) {

        super(vc);
    }

    @Override
    public List<TermsAndConditions> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        if (node.get("type") != null && node.get("type").textValue().equals("literal")) {
            return getTermsAndConditionsList(node.get("value").textValue());
        }

        return getTermsAndConditionsList(node.textValue());
    }

    private List<TermsAndConditions> getTermsAndConditionsList(String literalTnCList) throws JsonProcessingException {
        // Add enclosing brackets to the JSON string
        String jsonArrayString = "[" + literalTnCList + "]";

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonArrayString, new TypeReference<List<TermsAndConditions>>() {});
    }
}
