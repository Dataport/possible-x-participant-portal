package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxLegitimateInterest;
import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxDataResourceCredentialSubject;
import eu.possiblex.participantportal.application.entity.validation.ValidLegitimateInterestForPII;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString(callSuper = true)
@ValidLegitimateInterestForPII
public class CreateDataOfferingRequestTO extends CreateServiceOfferingRequestTO {
    @Valid
    @NotNull(message = "Data resource credential subject is required")
    private GxDataResourceCredentialSubject dataResourceCredentialSubject;

    @NotBlank(message = "File name is required")
    private String fileName;

    @Valid
    private GxLegitimateInterest legitimateInterest;
}
