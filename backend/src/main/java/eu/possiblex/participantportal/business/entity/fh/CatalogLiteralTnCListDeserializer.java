package eu.possiblex.participantportal.business.entity.fh;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Arrays;
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

    private List<TermsAndConditions> getTermsAndConditionsList(String literalTnCList) {

        return Arrays.stream(literalTnCList.split(", ")).map(literalTnC -> {
            String[] tnc = literalTnC.split("\\|");
            return TermsAndConditions.builder().url(tnc[0]).hash(tnc[1]).build();
        }).toList();
    }
}
