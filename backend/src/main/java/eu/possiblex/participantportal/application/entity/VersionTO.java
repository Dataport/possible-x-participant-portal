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
public class VersionTO {
    @Schema(description = "Version of the participant portal", example = "1.0.0")
    private String version;

    @Schema(description = "Date of the version of the participant portal", example = "2024-12-31")
    private String date;
}
