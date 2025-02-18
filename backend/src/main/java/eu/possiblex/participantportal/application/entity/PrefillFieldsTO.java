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
public class PrefillFieldsTO {
    @Schema(description = "ID of the participant", example = "did:web:example.com:participant:someorgltd")
    private String participantId;

    @Schema(description = "Values to help prefill specific fields when providing offerings containing data resources")
    private DataServiceOfferingPrefillFieldsTO dataServiceOfferingPrefillFields;
}
