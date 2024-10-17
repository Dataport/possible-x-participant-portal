package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxDataResourceCredentialSubject;
import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString(callSuper = true)
public class CreateDataOfferingRequestTO extends CreateServiceOfferingRequestTO {

    private GxDataResourceCredentialSubject dataResourceCredentialSubject;

    private String fileName;

    private EnforcementPolicy enforcementPolicy;
}
