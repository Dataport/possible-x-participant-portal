package eu.possiblex.participantportal.business.entity.credentials.px;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxDataAccountExport;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxSOTermsAndConditions;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.application.entity.credentials.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * This is the class which will be sent to the FH catalog.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PxExtendedServiceOfferingCredentialSubject {
    @Getter(AccessLevel.NONE)
    public static final List<String> TYPE = List.of(GxServiceOfferingCredentialSubject.TYPE,
        "px:PossibleXServiceOfferingExtension");

    @Getter(AccessLevel.NONE)
    public static final Map<String, String> CONTEXT = Map.of("gx", "https://w3id.org/gaia-x/development#", "xsd",
        "http://www.w3.org/2001/XMLSchema#", "px", "http://w3id.org/gaia-x/possible-x#", "schema",
        "https://schema.org/");

    @NotNull
    private String id;

    @JsonProperty("gx:providedBy")
    @NotNull
    private NodeKindIRITypeId providedBy;

    @JsonProperty("gx:aggregationOf")
    private List<PxExtendedDataResourceCredentialSubject> aggregationOf;

    @JsonProperty("gx:termsAndConditions")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @NotNull
    private List<GxSOTermsAndConditions> termsAndConditions;

    @JsonProperty("gx:policy")
    @NotNull
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> policy;

    @JsonProperty("gx:dataProtectionRegime")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> dataProtectionRegime;

    @JsonProperty("gx:dataAccountExport")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @NotNull
    private List<GxDataAccountExport> dataAccountExport;

    @JsonProperty("gx:name")
    private String name;

    @JsonProperty("gx:description")
    private String description;

    @JsonProperty("px:assetId")
    private String assetId;

    @JsonProperty("px:providerUrl")
    private String providerUrl;

    // TODO: Remove this when FH catalog UI is adjusted, currently needed to show the name
    @JsonProperty("schema:name")
    private String schemaName;

    // TODO: Remove this when FH catalog UI is adjusted, currently needed to show the description
    @JsonProperty("schema:description")
    private String schemaDescription;

    @JsonProperty("@type")
    public List<String> getType() {

        return TYPE;
    }

    @JsonProperty("@context")
    public Map<String, String> getContext() {

        return CONTEXT;
    }
}
