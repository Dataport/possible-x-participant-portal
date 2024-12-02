package eu.possiblex.participantportal.business.entity.fh;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipantNameSparqlQueryResult {
    @JsonDeserialize(using = CatalogLiteralDeserializer.class)
    private String uri;

    @JsonDeserialize(using = CatalogLiteralDeserializer.class)
    private String dapsId;

    @JsonDeserialize(using = CatalogLiteralDeserializer.class)
    private String name;
}
