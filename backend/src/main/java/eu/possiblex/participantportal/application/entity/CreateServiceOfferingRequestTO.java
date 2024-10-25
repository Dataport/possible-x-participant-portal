package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.application.entity.credentials.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
public class CreateServiceOfferingRequestTO {

    private GxServiceOfferingCredentialSubject serviceOfferingCredentialSubject;

    private List<EnforcementPolicy> enforcementPolicies;
}
