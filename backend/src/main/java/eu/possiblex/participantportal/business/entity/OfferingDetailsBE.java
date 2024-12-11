package eu.possiblex.participantportal.business.entity;

import eu.possiblex.participantportal.business.entity.fh.TermsAndConditions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferingDetailsBE {
    private String name;

    private String description;

    private String assetId;

    private String offeringId;

    private List<TermsAndConditions> termsAndConditions;
}
