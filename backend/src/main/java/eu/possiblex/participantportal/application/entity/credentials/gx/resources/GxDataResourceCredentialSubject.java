package eu.possiblex.participantportal.application.entity.credentials.gx.resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.possiblex.participantportal.application.entity.credentials.PojoCredentialSubject;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.business.entity.serialization.BooleanDeserializer;
import eu.possiblex.participantportal.business.entity.serialization.BooleanSerializer;
import eu.possiblex.participantportal.business.entity.serialization.StringDeserializer;
import eu.possiblex.participantportal.business.entity.serialization.StringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true, value = { "type", "@context" }, allowGetters = true)
public class GxDataResourceCredentialSubject extends PojoCredentialSubject {

    @Getter(AccessLevel.NONE)
    public static final String TYPE_NAMESPACE = "gx";

    @Getter(AccessLevel.NONE)
    public static final String TYPE_CLASS = "DataResource";

    @Getter(AccessLevel.NONE)
    public static final String TYPE = TYPE_NAMESPACE + ":" + TYPE_CLASS;

    @Getter(AccessLevel.NONE)
    public static final Map<String, String> CONTEXT = Map.of(TYPE_NAMESPACE,
        "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#", "xsd",
        "http://www.w3.org/2001/XMLSchema#", "schema", "https://schema.org/");

    @Schema(description = "A list of copyright owners either as a free form string or as resolvable link to the participant self-description.", example = "[\"Some Org Ltd.\", \"did:web:example.com:participant:someorgltd\"]")
    @JsonProperty("gx:copyrightOwnedBy")
    @JsonSerialize(contentUsing = StringSerializer.class)
    @JsonDeserialize(contentUsing = StringDeserializer.class)
    @NotEmpty(message = "At least one copyright owner is required")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<@NotBlank(message = "Copyright owner is required") String> copyrightOwnedBy;

    @Schema(description = "Resolvable link to the participant self-description legally enabling the data usage")
    @Valid
    @JsonProperty("gx:producedBy")
    @NotNull
    private NodeKindIRITypeId producedBy;

    @Schema(description = "Resolvable link to the data exchange component that exposes the data resource")
    @JsonProperty("gx:exposedThrough")
    // no input validations as this will be set by the backend
    private NodeKindIRITypeId exposedThrough;

    // aggregationOf not yet mapped as it is optional

    @Schema(description = "List of policies expressed using a DSL (e.g., Rego or ODRL). A simple default is: allow intent.", example = "[\"allow intent\"]")
    @JsonProperty("gx:policy")
    @JsonSerialize(contentUsing = StringSerializer.class)
    @JsonDeserialize(contentUsing = StringDeserializer.class)
    @NotEmpty(message = "At least one policy is required")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<@NotBlank(message = "Policy is required") String> policy;

    @Schema(description = "A list of SPDX identifiers or URL to document. Find SPDX identifiers here: https://spdx.org/licenses/ .", example = "[\"CC-BY-SA-4.0\", \"https://www.apache.org/licenses/LICENSE-2.0\"]")
    @JsonProperty("gx:license")
    @JsonSerialize(contentUsing = StringSerializer.class)
    @JsonDeserialize(contentUsing = StringDeserializer.class)
    @NotEmpty(message = "At least one license is required")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<@NotBlank(message = "License is required") String> license;

    @Schema(description = "Flag whether the data resource contains Personal Identifiable Information (PII) or not", example = "true")
    @JsonProperty("gx:containsPII")
    @JsonSerialize(using = BooleanSerializer.class)
    @JsonDeserialize(using = BooleanDeserializer.class)
    private boolean containsPII;

    @Schema(description = "Name of the data resource", example = "Some Data")
    @NotBlank(message = "Name is required")
    @JsonProperty("schema:name")
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = StringDeserializer.class)
    private String name;

    @Schema(description = "Description of the data resource", example = "Some Data Description")
    @JsonProperty("schema:description")
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = StringDeserializer.class)
    private String description;

    // obsoleteDateTime and expirationDateTime not yet mapped as they are optional

    @Schema(description = "JSON-LD type", example = "gx:DataResource")
    @JsonProperty("type")
    public String getType() {

        return TYPE;
    }

    @Schema(description = "JSON-LD context", example = "{\"gx\": \"https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#\"}")
    @JsonProperty("@context")
    public Map<String, String> getContext() {

        return CONTEXT;
    }
}
