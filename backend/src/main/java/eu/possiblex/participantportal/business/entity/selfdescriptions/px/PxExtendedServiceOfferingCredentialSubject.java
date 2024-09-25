package eu.possiblex.participantportal.business.entity.selfdescriptions.px;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.datatypes.GxDataAccountExport;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.datatypes.GxSOTermsAndConditions;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.resources.GxDataResourceCredentialSubject;
import eu.possiblex.participantportal.business.entity.serialization.StringDeserializer;
import eu.possiblex.participantportal.business.entity.serialization.StringSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

/**
 * This is the class which will be sent to the FH catalog.
 */
@Getter
@Setter
@SuperBuilder
@ToString
public class PxExtendedServiceOfferingCredentialSubject {
    @Getter(AccessLevel.NONE)
    public static final List<String> TYPE = List.of("gx:ServiceOffering", "px:PossibleXServiceOfferingExtension");

    @Getter(AccessLevel.NONE)
    public static final Map<String, String> CONTEXT = Map.of("gx", "https://w3id.org/gaia-x/development#", "xsd",
        "http://www.w3.org/2001/XMLSchema#", "px", "http://w3id.org/gaia-x/possible-x#");

    @NotNull
    private String id;

    @JsonProperty("gx:providedBy")
    @NotNull
    private NodeKindIRITypeId providedBy;

    @JsonProperty("gx:aggregationOf")
    private List<GxDataResourceCredentialSubject> aggregationOf;

    @JsonProperty("gx:termsAndConditions")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @NotNull
    private List<GxSOTermsAndConditions> termsAndConditions;

    @JsonProperty("gx:policy")
    @JsonSerialize(contentUsing = StringSerializer.class)
    @JsonDeserialize(contentUsing = StringDeserializer.class)
    @NotNull
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> policy;

    @JsonProperty("gx:dataProtectionRegime")
    @JsonSerialize(contentUsing = StringSerializer.class)
    @JsonDeserialize(contentUsing = StringDeserializer.class)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> dataProtectionRegime;

    @JsonProperty("gx:dataAccountExport")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @NotNull
    private List<GxDataAccountExport> dataAccountExport;

    @JsonProperty("gx:name")
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = StringDeserializer.class)
    private String name;

    @JsonProperty("gx:description")
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = StringDeserializer.class)
    private String description;

    @JsonProperty("px:assetId")
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = StringDeserializer.class)
    private String assetId;

    @JsonProperty("px:providerUrl")
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = StringDeserializer.class)
    private String providerUrl;

    @JsonProperty("@type")
    public List<String> getType() {

        return TYPE;
    }

    @JsonProperty("@context")
    public Map<String, String> getContext() {

        return CONTEXT;
    }
}
