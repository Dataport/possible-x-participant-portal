package eu.possiblex.participantportal.business.entity.fh.catalog;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.possiblex.participantportal.business.entity.common.JsonLdBase;
import eu.possiblex.participantportal.business.entity.common.JsonLdConstants;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class DcatDataset extends JsonLdBase {

    private static final String TYPE = JsonLdConstants.DCAT_PREFIX + "Dataset";

    private static final Map<String, String> CONTEXT = JsonLdConstants.FH_CONTEXT;

    @JsonProperty(JsonLdConstants.ODRL_PREFIX + "hasPolicy")
    private Policy hasPolicy;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JsonProperty(JsonLdConstants.DCAT_PREFIX + "distribution")
    private List<DcatDistribution> distribution;

    @JsonProperty(JsonLdConstants.DCT_PREFIX + "title")
    private String title;

    @JsonProperty(JsonLdConstants.DCT_PREFIX + "description")
    private String description;

    @JsonProperty(JsonLdConstants.EDC_PREFIX + "version")
    private String version;

    @JsonProperty(JsonLdConstants.EDC_PREFIX + "contenttype")
    private String contenttype;

    @JsonProperty("https://possible-gaia-x.de/ns/#assetId")
    private String assetId;

    @JsonProperty("@type")
    public String getType() {

        return TYPE;
    }

    @JsonProperty("@context")
    public Map<String, String> getContext() {

        return CONTEXT;
    }
}