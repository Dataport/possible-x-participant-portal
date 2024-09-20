package eu.possiblex.participantportal.business.entity.fh;

import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CreateFhServiceOfferingBE {

    private GxServiceOfferingCredentialSubject serviceOfferingCredentialSubject;
}