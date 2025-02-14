package eu.possiblex.participantportal.application.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractParticipantDetailsTO {
    @Schema(description = "Name of the participant", example = "Some Organization Ltd.")
    private String name;

    @Schema(description = "(D)ID of the participant", example = "did:web:example.com:participant:someorgltd")
    private String did;

    @Schema(description = "DAPS ID of the participant", example = "06:B2:4A:FD:8D:E9:38:C8:38:88:0C:C5:FE:15:15:BD:3C:DA:47:EE:keyid:06:B2:4A:FD:8D:E9:38:C8:38:88:0C:C5:FE:15:15:BD:3C:DA:47:EE")
    private String dapsId;
}
