package eu.possiblex.participantportal.business.entity.fh;

import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.resources.GxDataResourceCredentialSubject;
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
public class CreateFhDataOfferingBE extends CreateFhServiceOfferingBE {

    private GxDataResourceCredentialSubject dataResourceCredentialSubject;
}
