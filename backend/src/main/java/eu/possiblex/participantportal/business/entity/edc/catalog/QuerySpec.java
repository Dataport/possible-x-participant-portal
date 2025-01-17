package eu.possiblex.participantportal.business.entity.edc.catalog;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuerySpec {
    private static final Map<String, String> CONTEXT = Map.of("@vocab", "https://w3id.org/edc/v0.0.1/ns/");

    private List<FilterExpression> filterExpression;

    @Builder.Default
    private int limit = 50;

    private int offset;

    private String sortField;

    private String sortOrder;

    @JsonProperty("@context")
    public Map<String, String> getContext() {

        return CONTEXT;
    }
}
