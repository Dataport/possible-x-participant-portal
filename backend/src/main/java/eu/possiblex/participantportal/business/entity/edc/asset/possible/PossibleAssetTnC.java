package eu.possiblex.participantportal.business.entity.edc.asset.possible;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PossibleAssetTnC {
    @JsonProperty("https://w3id.org/gaia-x/development#URL")
    private String url;

    @JsonProperty("https://w3id.org/gaia-x/development#hash")
    private String hash;
}
