package eu.possiblex.participantportal.business.entity.edc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class DataspaceErrorMessage {

    @JsonProperty("@context")
    private Map<String, String> context;

    @JsonProperty("@type")
    private String type;

    @JsonProperty("dspace:code")
    private String code;

    @JsonProperty("dspace:reason")
    private String reason;
}
