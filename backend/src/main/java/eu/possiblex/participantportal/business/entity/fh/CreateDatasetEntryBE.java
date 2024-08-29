package eu.possiblex.participantportal.business.entity.fh;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CreateDatasetEntryBE {

    private JsonNode policy;

}