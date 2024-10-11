package eu.possible_x.edc_orchestrator.entities.fh.catalog;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class GraphSecondElement {
    @JsonProperty("@id")
    private String id;

    @JsonProperty("@type")
    private String type;

    @JsonProperty("dct:identifier")
    private String identifier;

    @JsonProperty("dct:title")
    private String title;

    @JsonProperty("dcat:accessURL")
    private AccessURL accessURL;
}
