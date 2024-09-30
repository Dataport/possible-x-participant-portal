package eu.possiblex.participantportal.business.entity.edc;

import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAssetProperties;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CreateEdcOfferBE {

    private PossibleAssetProperties properties;

    private String assetId;

    private String fileName;

    private Policy policy;

}