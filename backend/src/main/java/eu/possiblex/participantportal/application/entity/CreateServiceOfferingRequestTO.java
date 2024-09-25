package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
public class CreateServiceOfferingRequestTO {

    private GxServiceOfferingCredentialSubject serviceOfferingCredentialSubject;

    private Policy policy;
}
