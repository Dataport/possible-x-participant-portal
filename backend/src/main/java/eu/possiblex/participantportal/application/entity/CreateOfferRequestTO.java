package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CreateOfferRequestTO {

    private String offerType;

    private String offerName;

    private String offerDescription;

    private String fileName;

    private Policy policy;

}
