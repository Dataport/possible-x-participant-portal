package eu.possiblex.participantportal.business.entity.credentials.px;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxDataResourceCredentialSubject;
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
public class PxExtendedDataResourceCredentialSubject {
    @Getter(AccessLevel.NONE)
    public static final List<String> TYPE = List.of(GxDataResourceCredentialSubject.TYPE,
        "px:PossibleXDataResourceExtension");

    @Getter(AccessLevel.NONE)
    public static final Map<String, String> CONTEXT = Map.of("gx", "https://w3id.org/gaia-x/development#", "xsd",
        "http://www.w3.org/2001/XMLSchema#", "px", "http://w3id.org/gaia-x/possible-x#", "schema",
        "https://schema.org/");

    @NotNull
    private String id;

    @JsonProperty("gx:copyrightOwnedBy")
    @NotNull
    private NodeKindIRITypeId copyrightOwnedBy;

    @JsonProperty("gx:producedBy")
    @NotNull
    private NodeKindIRITypeId producedBy;

    @JsonProperty("gx:exposedThrough")
    @NotNull
    private NodeKindIRITypeId exposedThrough;

    // aggregationOf not yet mapped as it is optional

    @JsonProperty("gx:policy")
    @NotNull
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> policy;

    @JsonProperty("gx:license")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> license;

    @JsonProperty("gx:containsPII")
    @NotNull
    private boolean containsPII;

    @JsonProperty("gx:name")
    private String name;

    @JsonProperty("gx:description")
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
