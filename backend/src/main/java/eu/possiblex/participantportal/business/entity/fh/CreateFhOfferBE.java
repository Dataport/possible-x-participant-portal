package eu.possiblex.participantportal.business.entity.fh;

import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
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

    private Policy policy;

}