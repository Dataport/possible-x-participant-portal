package eu.possiblex.participantportal.application.entity;

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
    private DcatDataset edcOffering; // TODO validate whether we really want to pass the full EDC response to the frontend
}
