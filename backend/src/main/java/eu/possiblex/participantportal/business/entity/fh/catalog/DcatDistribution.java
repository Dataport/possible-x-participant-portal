package eu.possiblex.participantportal.business.entity.fh.catalog;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.possiblex.participantportal.business.entity.common.JsonLdConstants;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DcatDistribution {

    private static final String TYPE = JsonLdConstants.DCAT_PREFIX + "distribution";

    @JsonProperty("@id")
    private String id;

    @JsonProperty(JsonLdConstants.DCAT_PREFIX + "accessURL")
    private DcatAccessURL accessUrl;

    @JsonProperty(JsonLdConstants.DCT_PREFIX + "license")
    private DctLicense license;

    @JsonProperty("@type")
    public String getType() {

        return TYPE;
    }
}
