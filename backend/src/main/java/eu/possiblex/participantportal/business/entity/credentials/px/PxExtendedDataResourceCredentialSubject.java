package eu.possiblex.participantportal.business.entity.credentials.px;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxLegitimateInterest;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxDataResourceCredentialSubject;
import eu.possiblex.participantportal.business.entity.serialization.BooleanDeserializer;
import eu.possiblex.participantportal.business.entity.serialization.BooleanSerializer;
import eu.possiblex.participantportal.business.entity.serialization.StringDeserializer;
import eu.possiblex.participantportal.business.entity.serialization.StringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true, value = { "@type", "@context" }, allowGetters = true)
public class PxExtendedDataResourceCredentialSubject {
    @Getter(AccessLevel.NONE)
    public static final List<String> TYPE = List.of(GxDataResourceCredentialSubject.TYPE/*,
        "px:PossibleXDataResourceExtension"*/); // TODO re-add this once the catalog properly parses this
    // currently this has no implications as we don't have custom fields on the resource, but if we did, this would
    // be bad

    @Getter(AccessLevel.NONE)
    public static final Map<String, String> CONTEXT = Map.of("gx", "https://w3id.org/gaia-x/development#", "xsd",
        "http://www.w3.org/2001/XMLSchema#", "px", "http://w3id.org/gaia-x/possible-x#", "schema",
        "https://schema.org/");

    @Schema(description = "ID of the data resource in the catalog", example = "urn:uuid:0107cfac-ba6c-4d1b-9b90-899aa4b56da1")
    @NotNull
    @JsonAlias("@id")
    private String id;

    @Schema(description = "A list of copyright owners either as a free form string or as resolvable link to the participant self-description.", example = "[\"Some Org Ltd.\", \"did:web:example.com:participant:someorgltd\"]")
    @JsonProperty("gx:copyrightOwnedBy")
    @NotNull
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JsonSerialize(contentUsing = StringSerializer.class)
    @JsonDeserialize(contentUsing = StringDeserializer.class)
    private List<String> copyrightOwnedBy;

    @Schema(description = "Resolvable link to the participant self-description legally enabling the data usage")
    @JsonProperty("gx:producedBy")
    @NotNull
    private NodeKindIRITypeId producedBy;

    @Schema(description = "Resolvable link to the data exchange component that exposes the data resource")
    @JsonProperty("gx:exposedThrough")
    @NotNull
    private NodeKindIRITypeId exposedThrough;

    // aggregationOf not yet mapped as it is optional

    @Schema(description = "List of policies expressed using a DSL (e.g., Rego or ODRL). A simple default is: allow intent.", example = "[\"allow intent\"]")
    @JsonProperty("gx:policy")
    @NotNull
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JsonSerialize(contentUsing = StringSerializer.class)
    @JsonDeserialize(contentUsing = StringDeserializer.class)
    private List<String> policy;

    @Schema(description = "A list of SPDX identifiers or URL to document. Find SPDX identifiers here: https://spdx.org/licenses/ .", example = "[\"CC-BY-SA-4.0\", \"https://www.apache.org/licenses/LICENSE-2.0\"]")
    @JsonProperty("gx:license")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JsonSerialize(contentUsing = StringSerializer.class)
    @JsonDeserialize(contentUsing = StringDeserializer.class)
    private List<String> license;

    @Schema(description = "Flag whether the data resource contains Personal Identifiable Information (PII) or not")
    @JsonProperty("gx:containsPII")
    @NotNull
    @JsonSerialize(using = BooleanSerializer.class)
    @JsonDeserialize(using = BooleanDeserializer.class)
    private boolean containsPII;

    @Schema(description = "Legitimate interest for processing data containing PII")
    @JsonProperty("gx:legitimateInterest")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private GxLegitimateInterest legitimateInterest;

    @Schema(description = "Human readable name of the data resource", example = "Some Data")
    @JsonProperty("schema:name")
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = StringDeserializer.class)
    private String name;

    @Schema(description = "Human readable description of the data resource", example = "Some Data Description")
    @JsonProperty("schema:description")
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = StringDeserializer.class)
    private String description;

    // obsoleteDateTime and expirationDateTime not yet mapped as they are optional

    @JsonProperty("@type")
    public List<String> getType() {

        return TYPE;
    }

    @JsonProperty("@context")
    public Map<String, String> getContext() {

        return CONTEXT;
    }
}
