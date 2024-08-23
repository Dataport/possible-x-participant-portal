package eu.possible_x.backend.business.entity.fh.catalog;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class DctTitle {
    @JsonProperty("@language")
    private String language;

    @JsonProperty("@value")
    private String value;
}
