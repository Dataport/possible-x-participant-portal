package eu.possiblex.participantportal.business.entity.fh;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SparqlQueryResponseHead {
    private List<Object> link;
    private List<String> vars;
}
