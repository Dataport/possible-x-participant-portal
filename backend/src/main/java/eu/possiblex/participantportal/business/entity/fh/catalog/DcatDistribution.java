package eu.possiblex.participantportal.business.entity.fh.catalog;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.possiblex.participantportal.business.entity.common.JsonLdBase;
import eu.possiblex.participantportal.business.entity.common.JsonLdConstants;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class DcatDistribution extends JsonLdBase {

    private static final String TYPE = JsonLdConstants.DCAT_PREFIX + "distribution";

    @JsonProperty(JsonLdConstants.DCAT_PREFIX + "accessURL")
    private String accessUrl;

    @JsonProperty(JsonLdConstants.DCT_PREFIX + "license")
    private String license;

    @JsonProperty("@type")
    public String getType() {

        return TYPE;
    }
}
