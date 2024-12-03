package eu.possiblex.participantportal.business.entity.fh;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SparqlQueryResponse<T> {
    private SparqlQueryResponseHead head;
    private SparqlQueryResponseResults<T> results;
}
