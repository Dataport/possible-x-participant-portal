package eu.possiblex.participantportal.application.entity;

import com.fasterxml.jackson.databind.JsonNode;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import lombok.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CreateOfferRequestTO {

    private List<JsonNode> credentialSubjectList; //TODO replace JsonNode with real classes

    private String fileName;

    private Policy policy;

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CreateOfferRequestTO{");
        sb.append("credentialSubjectList=");
        if (credentialSubjectList != null) {
            sb.append("[");
            for (JsonNode node : credentialSubjectList) {
                sb.append(jsonNodeToString(node)).append(",");
            }
            if (sb.charAt(sb.length() - 1) == ',') {
                sb.deleteCharAt(sb.length() - 1); // Remove trailing comma
            }
            sb.append("]");
        } else {
            sb.append("null");
        }
        sb.append(", fileName='").append(fileName).append('\'');
        sb.append(", policy=").append(policy);
        sb.append('}');
        return sb.toString();
    }

    private String jsonNodeToString(JsonNode node) {

        StringBuilder sb = new StringBuilder();
        if (node.isObject()) {
            sb.append("{");
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                sb.append("\"").append(field.getKey()).append("\":").append(jsonNodeToString(field.getValue()));
                if (fields.hasNext()) {
                    sb.append(",");
                }
            }
            sb.append("}");
        } else if (node.isArray()) {
            sb.append("[");
            for (JsonNode element : node) {
                sb.append(jsonNodeToString(element)).append(",");
            }
            if (sb.charAt(sb.length() - 1) == ',') {
                sb.deleteCharAt(sb.length() - 1); // Remove trailing comma
            }
            sb.append("]");
        } else if (node.isTextual()) {
            sb.append("\"").append(node.textValue()).append("\"");
        } else if (node.isNumber()) {
            sb.append(node.numberValue());
        } else if (node.isBoolean()) {
            sb.append(node.booleanValue());
        } else {
            sb.append(node.toString());
        }
        return sb.toString();
    }
}
