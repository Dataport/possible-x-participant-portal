package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxLegitimateInterestCredentialSubject;
import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxDataResourceCredentialSubject;
import eu.possiblex.participantportal.application.entity.validation.ValidLegitimateInterestForPII;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Data resource credential subject")
    @Valid
    @NotNull(message = "Data resource credential subject is required")
    private GxDataResourceCredentialSubject dataResourceCredentialSubject;

    @Schema(description = "File name of the data resource", example = "data.csv")
    @NotBlank(message = "File name is required")
    private String fileName;

    @Schema(description = "Legitimate interest credential subject, must be provided if the data resource contains Personal Identifiable Information (PII).")
    @Valid
    private GxLegitimateInterestCredentialSubject legitimateInterestCredentialSubject;
}
