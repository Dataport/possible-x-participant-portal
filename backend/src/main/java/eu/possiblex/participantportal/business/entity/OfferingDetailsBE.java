package eu.possiblex.participantportal.business.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferingDetailsBE {
    private String name;

    private String description;

    private String assetId;

    private String offeringId;
}
