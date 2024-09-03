package eu.possiblex.participantportal.business.entity.edc;

import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
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

    private Policy policy;

}