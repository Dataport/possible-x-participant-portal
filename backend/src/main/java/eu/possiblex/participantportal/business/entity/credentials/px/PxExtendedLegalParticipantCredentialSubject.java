package eu.possiblex.participantportal.business.entity.credentials.px;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.possiblex.participantportal.business.entity.serialization.StringDeserializer;
import eu.possiblex.participantportal.business.entity.serialization.StringSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PxExtendedLegalParticipantCredentialSubject {

    @JsonProperty("@type")
    private List<String> type;

    @JsonProperty("@context")
    private  Map<String, String> context;

    @NotNull
    @JsonAlias("@id")
    private String id;

    @NotNull
    @JsonProperty("gx:legalRegistrationNumber")
    // Add GxLegalRegistrationNumberCredentialSubject when needed
    private JsonNode legalRegistrationNumber;

    @NotNull
    @JsonProperty("gx:legalAddress")
    // Add GxVcard when needed
    private JsonNode legalAddress;

    @NotNull
    @JsonProperty("gx:headquarterAddress")
    // Add GxVcard when needed
    private JsonNode headquarterAddress;

    @JsonProperty("schema:name")
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = StringDeserializer.class)
    private String name;

    @JsonProperty("schema:description")
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = StringDeserializer.class)
    private String description;

    @JsonProperty("px:mailAddress")
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = StringDeserializer.class)
    private String mailAddress;
}

