package eu.possiblex.participantportal.application.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantNameTO {
    @Schema(description = "ID of the participant", example = "did:web:example.com:participant:someorgltd")
    private String participantId;

    @Schema(description = "Name of the participant", example = "Some Organization Ltd.")
    private String participantName;
}
