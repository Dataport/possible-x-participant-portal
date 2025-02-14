package eu.possiblex.participantportal.application.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDetailsTO extends ParticipantNameTO {
    @Schema(description = "Email address of the participant", example = "contact@someorg.com")
    private String participantEmail;
}
