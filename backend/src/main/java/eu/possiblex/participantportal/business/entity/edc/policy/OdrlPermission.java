package eu.possiblex.participantportal.business.entity.edc.policy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.possiblex.participantportal.business.entity.common.JsonLdConstants;
import eu.possiblex.participantportal.business.entity.serialization.OdrlActionDeserializer;
import eu.possiblex.participantportal.business.entity.serialization.OdrlActionSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OdrlPermission {

    @JsonProperty(JsonLdConstants.ODRL_PREFIX + "target")
    private String target;

    @JsonProperty(JsonLdConstants.ODRL_PREFIX + "action")
    @JsonSerialize(using = OdrlActionSerializer.class)
    @JsonDeserialize(using = OdrlActionDeserializer.class)
    private OdrlAction action;

    @Builder.Default
    @JsonProperty(JsonLdConstants.ODRL_PREFIX + "constraint")
    @JsonFormat(with = { JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
        JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED })
    private List<OdrlConstraint> constraint = new ArrayList<>();
}
