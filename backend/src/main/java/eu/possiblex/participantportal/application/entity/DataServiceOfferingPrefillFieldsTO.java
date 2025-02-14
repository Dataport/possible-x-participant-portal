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
public class DataServiceOfferingPrefillFieldsTO {
    @Schema(description = "Service offering name", example = "Data Service Offering")
    private String serviceOfferingName;

    @Schema(description = "Service offering description", example = "This is a data service offering")
    private String serviceOfferingDescription;
}
