package eu.possiblex.participantportal.business.entity.edc.asset.possible;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.possiblex.participantportal.business.entity.edc.asset.ionoss3extension.IonosS3DataAddress;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PossibleAsset {
    @JsonProperty("@id")
    private String id;

    @JsonProperty("@type")
    private String type;

    @JsonProperty("@context")
    private Map<String, String> context;

    private PossibleAssetProperties properties;

    private IonosS3DataAddress dataAddress;
}
