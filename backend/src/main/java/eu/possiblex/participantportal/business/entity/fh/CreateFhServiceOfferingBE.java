package eu.possiblex.participantportal.business.entity.fh;

import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class CreateFhServiceOfferingBE {

    private GxServiceOfferingCredentialSubject serviceOfferingCredentialSubject;
}