package eu.possiblex.participantportal.business.entity.fh;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CreateFhOfferBE {

    private String offerType;
    private String offerName;
    private String offerDescription;
    private JsonNode policy;

}