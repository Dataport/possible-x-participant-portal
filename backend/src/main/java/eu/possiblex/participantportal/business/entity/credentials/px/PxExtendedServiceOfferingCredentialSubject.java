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
public class PxExtendedServiceOfferingCredentialSubject {
    @Getter(AccessLevel.NONE)
    public static final List<String> TYPE = List.of(GxServiceOfferingCredentialSubject.TYPE,
        "px:PossibleXServiceOfferingExtension");

    @Getter(AccessLevel.NONE)
    public static final Map<String, String> CONTEXT = Map.of("gx", "https://w3id.org/gaia-x/development#", "xsd",
        "http://www.w3.org/2001/XMLSchema#", "px", "http://w3id.org/gaia-x/possible-x#", "schema",
        "https://schema.org/");

    @Schema(description = "ID of the offering in the catalog", example = "urn:uuid:0107cfac-ba6c-4d1b-9b90-899aa4b56da1")
    @NotNull
    @JsonAlias("@id")
    private String id;

    @Schema(description = "Resolvable link to the participant self-description providing the service")
    @JsonProperty("gx:providedBy")
    @NotNull
    private NodeKindIRITypeId providedBy;

    @Schema(description = "List of data resources related to the offering")
    @JsonProperty("gx:aggregationOf")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<PxExtendedDataResourceCredentialSubject> aggregationOf;

    @Schema(description = "List of terms and conditions")
    @JsonProperty("gx:termsAndConditions")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @NotNull
    private List<GxSOTermsAndConditions> termsAndConditions;

    @Schema(description = "List of policies expressed using a DSL (e.g., Rego or ODRL). A simple default is: allow intent.", example = "[\"allow intent\"]")
    @JsonProperty("gx:policy")
    @NotNull
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JsonSerialize(contentUsing = StringSerializer.class)
    @JsonDeserialize(contentUsing = StringDeserializer.class)
    private List<String> policy;

    @Schema(description = "List of data protection regime", example = "[\"GDPR2016\"]")
    @JsonProperty("gx:dataProtectionRegime")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JsonSerialize(contentUsing = StringSerializer.class)
    @JsonDeserialize(contentUsing = StringDeserializer.class)
    private List<String> dataProtectionRegime;

    @Schema(description = "A list of methods to export user account data out of the service")
    @JsonProperty("gx:dataAccountExport")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @NotNull
    private List<GxDataAccountExport> dataAccountExport;

    @Schema(description = "Human readable name of the offering", example = "Some Service")
    @JsonProperty("schema:name")
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = StringDeserializer.class)
    private String name;

    @Schema(description = "Human readable description of the offering", example = "Some Service Description")
    @JsonProperty("schema:description")
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = StringDeserializer.class)
    private String description;

    @Schema(description = "The ID which is used to identify the offering in the EDC catalog. Currently, this is the asset ID, because an asset will only be used in one offering.", example = "8d3c927a-9bb7-4bc8-a3e7-4f9c9a57d571")
    @JsonProperty("px:assetId")
    @JsonSerialize(using = StringSerializer.class)
    @JsonDeserialize(using = StringDeserializer.class)
    private String assetId;

    @Schema(description = "EDC protocol URL of the offering provider", example = "https://edc.someorg.com/protocol")
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
