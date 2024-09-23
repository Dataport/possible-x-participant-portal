package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.resources.GxDataResourceCredentialSubject;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
public class CreateDataOfferingRequestTO extends CreateServiceOfferingRequestTO {

    private GxDataResourceCredentialSubject dataResourceCredentialSubject;

    private String fileName;
}
