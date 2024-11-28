package eu.possiblex.participantportal.business.entity.fh;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipantNameQueryResult {
    @JsonDeserialize(using = CatalogLiteralDeserializer.class)
    private String uri;

    @JsonDeserialize(using = CatalogLiteralDeserializer.class)
    private String name;
}
