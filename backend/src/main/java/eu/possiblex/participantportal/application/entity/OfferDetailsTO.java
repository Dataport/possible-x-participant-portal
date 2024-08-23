package eu.possiblex.participantportal.application.entity;

import com.fasterxml.jackson.databind.JsonNode;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDataset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferDetailsTO {
    private DcatDataset edcOffering;
}
