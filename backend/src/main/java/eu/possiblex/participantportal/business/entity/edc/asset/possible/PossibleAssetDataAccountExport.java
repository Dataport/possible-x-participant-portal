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
public class PossibleAssetDataAccountExport {
    @JsonProperty("https://w3id.org/gaia-x/development#requestType")
    private String requestType;

    @JsonProperty("https://w3id.org/gaia-x/development#accessType")
    private String accessType;

    @JsonProperty("https://w3id.org/gaia-x/development#formatType")
    private String formatType;
}
