package eu.possiblex.participantportal.business.entity.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public abstract class JsonLdBase {

    @JsonProperty("@id")
    private String id;
}
