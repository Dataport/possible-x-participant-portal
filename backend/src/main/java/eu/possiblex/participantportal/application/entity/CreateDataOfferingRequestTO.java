package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxLegitimateInterest;
import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxDataResourceCredentialSubject;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString(callSuper = true)
public class CreateDataOfferingRequestTO extends CreateServiceOfferingRequestTO {

    private GxDataResourceCredentialSubject dataResourceCredentialSubject;

    private String fileName;

    private Optional<GxLegitimateInterest> legitimateInterest;
}
