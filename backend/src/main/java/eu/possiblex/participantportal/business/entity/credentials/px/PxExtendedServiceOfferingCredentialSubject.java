package eu.possiblex.participantportal.business.entity.credentials.px;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxDataAccountExport;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxSOTermsAndConditions;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.application.entity.credentials.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.serialization.StringDeserializer;
import eu.possiblex.participantportal.business.entity.serialization.StringSerializer;
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
@JsonIgnoreProperties(ignoreUnknown = true, value = { "@type", "@context" }, allowGetters = true)
public class PxExtendedServiceOfferingCredentialSubject {
    @Getter(AccessLevel.NONE)
    public static final List<String> TYPE = List.of(GxServiceOfferingCredentialSubject.TYPE,
        "px:PossibleXServiceOfferingExtension");

    @Getter(AccessLevel.NONE)
    public static final Map<String, String> CONTEXT = Map.of("gx", "https://w3id.org/gaia-x/development#", "xsd",
        "http://www.w3.org/2001/XMLSchema#", "px", "http://w3id.org/gaia-x/possible-x#", "schema",
        "https://schema.org/");

    @NotNull
    @JsonAlias("@id")
    private String id;

    @JsonProperty("gx:providedBy")
    @NotNull
    private NodeKindIRITypeId providedBy;

    @JsonProperty("gx:aggregationOf")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<PxExtendedDataResourceCredentialSubject> aggregationOf;

    @JsonProperty("gx:termsAndConditions")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @NotNull
    private List<GxSOTermsAndConditions> termsAndConditions;

    @JsonProperty("gx:policy")
    @NotNull
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JsonSerialize(contentUsing = StringSerializer.class)
    @JsonDeserialize(contentUsing = StringDeserializer.class)
    private List<String> policy;

    @JsonProperty("gx:dataProtectionRegime")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JsonSerialize(contentUsing = StringSerializer.class)
    @JsonDeserialize(contentUsing = StringDeserializer.class)
    private List<String> dataProtectionRegime;

    @JsonProperty("gx:dataAccountExport")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @NotNull
    private List<GxDataAccountExport> dataAccountExport;

    @JsonProperty("schema:name")
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = StringDeserializer.class)
    private String name;

    @JsonProperty("schema:description")
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
