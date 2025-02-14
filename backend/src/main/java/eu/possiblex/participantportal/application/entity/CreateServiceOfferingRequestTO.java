package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.application.entity.credentials.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Service offering credential subject")
    @Valid
    @NotNull(message = "Service offering credential subject is required")
    private GxServiceOfferingCredentialSubject serviceOfferingCredentialSubject;

    @Schema(description = "List of enforcement policies for this service offering")
    @Valid
    private List<EnforcementPolicy> enforcementPolicies;
}
