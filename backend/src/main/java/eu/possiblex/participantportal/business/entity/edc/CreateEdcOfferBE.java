package eu.possiblex.participantportal.business.entity.edc;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CreateEdcOfferBE {

    private String assetName;
    private String assetDescription;
    private String fileName;
    private JsonNode policy;

}