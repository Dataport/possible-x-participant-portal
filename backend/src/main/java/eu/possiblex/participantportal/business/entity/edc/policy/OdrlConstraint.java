package eu.possiblex.participantportal.business.entity.edc.policy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.possiblex.participantportal.business.entity.common.JsonLdConstants;
import eu.possiblex.participantportal.business.entity.serialization.OdrlOperatorDeserializer;
import eu.possiblex.participantportal.business.entity.serialization.OdrlOperatorSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OdrlConstraint {

    private static final String TYPE = "AtomicConstraint";

    @JsonProperty(JsonLdConstants.ODRL_PREFIX + "leftOperand")
    private String leftOperand;

    @JsonSerialize(using = OdrlOperatorSerializer.class)
    @JsonDeserialize(using = OdrlOperatorDeserializer.class)
    @JsonProperty(JsonLdConstants.ODRL_PREFIX + "operator")
    private OdrlOperator operator;

    @JsonProperty(JsonLdConstants.ODRL_PREFIX + "rightOperand")
    private String rightOperand; // technically this can be any object but the EDC only supports strings

    @JsonProperty("@type")
    public String getType() {

        return TYPE;
    }
}
