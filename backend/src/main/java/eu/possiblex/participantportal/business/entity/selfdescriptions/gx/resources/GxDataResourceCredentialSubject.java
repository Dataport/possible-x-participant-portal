package eu.possiblex.participantportal.business.entity.selfdescriptions.gx.resources;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.possiblex.participantportal.business.entity.selfdescriptions.PojoCredentialSubject;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.business.entity.serialization.StringDeserializer;
import eu.possiblex.participantportal.business.entity.serialization.StringSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
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
        "http://www.w3.org/2001/XMLSchema#");

    @JsonProperty("gx:copyrightOwnedBy")
    @NotNull
    private NodeKindIRITypeId copyrightOwnedBy;

    @JsonProperty("gx:producedBy")
    @NotNull
    private NodeKindIRITypeId producedBy;

    @JsonProperty("gx:exposedThrough")
    @NotNull
    private NodeKindIRITypeId exposedThrough;

    // aggregationOf are not yet mapped as they are optional

    @JsonProperty("gx:policy")
    @JsonSerialize(contentUsing = StringSerializer.class)
    @JsonDeserialize(contentUsing = StringDeserializer.class)
    @NotNull
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> policy;

    @JsonProperty("gx:license")
    @JsonSerialize(contentUsing = StringSerializer.class)
    @JsonDeserialize(contentUsing = StringDeserializer.class)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> license;

    @JsonProperty("gx:containsPII")
    @NotNull
    private boolean containsPII;

    @JsonProperty("gx:name")
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = StringDeserializer.class)
    private String name;

    @JsonProperty("gx:description")
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = StringDeserializer.class)
    private String description;

    @NotNull
    @JsonProperty("gx:obsoleteDateTime")
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = StringDeserializer.class)
    private String obsoleteDateTime;

    @NotNull
    @JsonProperty("gx:expirationDateTime")
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = StringDeserializer.class)
    private String expirationDateTime;

    @JsonProperty("type")
    public String getType() {

        return TYPE;
    }

    @JsonProperty("@context")
    public Map<String, String> getContext() {

        return CONTEXT;
    }
}
