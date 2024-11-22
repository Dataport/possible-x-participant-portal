package eu.possiblex.participantportal.business.entity.credentials.px;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PxExtendedLegalParticipantCredentialSubjectSubset {

    @Getter(AccessLevel.NONE)
    public static final List<String> TYPE = List.of("gx:LegalParticipant",
        "px:PossibleXLegalParticipantExtension");

    @Getter(AccessLevel.NONE)
    public static final Map<String, String> CONTEXT = Map.of("gx",
        "https://w3id.org/gaia-x/development#", "vcard", "http://www.w3.org/2006/vcard/ns#", "xsd",
        "http://www.w3.org/2001/XMLSchema#", "px", "http://w3id.org/gaia-x/possible-x#", "schema",
        "https://schema.org/");

    @JsonProperty("@id")
    private String id;

    @JsonProperty("schema:name")
    private String name;

    @JsonProperty("schema:description")
    private String description;

    @JsonProperty("px:mailAddress")
    private String mailAddress;

}
