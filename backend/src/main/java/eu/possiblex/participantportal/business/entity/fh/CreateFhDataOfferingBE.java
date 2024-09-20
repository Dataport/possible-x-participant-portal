package eu.possiblex.participantportal.business.entity.fh;

import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.resources.GxDataResourceCredentialSubject;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CreateFhDataOfferingBE extends CreateFhServiceOfferingBE {

    private GxDataResourceCredentialSubject dataResourceCredentialSubject;
}
