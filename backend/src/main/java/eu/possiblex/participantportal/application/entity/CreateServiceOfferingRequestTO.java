package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.application.entity.credentials.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    @Valid
    @NotNull(message = "Service offering credential subject is required")
    private GxServiceOfferingCredentialSubject serviceOfferingCredentialSubject;

    @Valid
    private List<EnforcementPolicy> enforcementPolicies;
}
