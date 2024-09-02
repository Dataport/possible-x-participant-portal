package eu.possiblex.participantportal.business.entity.fh.catalog;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.possiblex.participantportal.business.entity.common.JsonLdConstants;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDistribution;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DcatDataset {

    private static final String TYPE = JsonLdConstants.DCAT_PREFIX + "Dataset";
    private static final Map<String, String> CONTEXT = JsonLdConstants.FH_CONTEXT;

    @JsonProperty("@id")
    private String id;

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

    @JsonProperty("@type")
    public String getType() {

        return TYPE;
    }

    @JsonProperty("@context")
    public Map<String, String> getContext() {
        return CONTEXT;
    }
}