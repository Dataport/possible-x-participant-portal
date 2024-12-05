package eu.possiblex.participantportal.business.entity.fh;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SparqlQueryResponseResults<T> {
    private boolean distinct;
    private boolean ordered;
    private List<T> bindings;
}
