package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.application.entity.credentials.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
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
