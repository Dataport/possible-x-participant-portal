package eu.possiblex.participantportal.business.entity.edc.policy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.possiblex.participantportal.business.entity.common.JsonLdConstants;
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
public class OdrlPermission {
    @JsonProperty(JsonLdConstants.ODRL_PREFIX + "action")
    private OdrlAction action;

    @JsonProperty(JsonLdConstants.ODRL_PREFIX + "constraint")
    private OdrlConstraint constraint;
}
