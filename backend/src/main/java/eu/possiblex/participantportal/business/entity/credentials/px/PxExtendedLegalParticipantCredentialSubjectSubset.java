package eu.possiblex.participantportal.business.entity.credentials.px;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PxExtendedLegalParticipantCredentialSubjectSubset {

    private String id;

    @JsonProperty("schema:name")
    private String name;

    @JsonProperty("schema:description")
    private String description;

    @JsonProperty("px:mailAddress")
    private String mailAddress;

}
