package eu.possiblex.participantportal.application.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectOfferRequestTO {
    @Schema(description = "Offering ID from the catalog", example = "urn:uuid:a5a5ff71-0e0b-4e3f-944a-6e47eeac3088")
    @NotBlank(message = "Offering ID is required")
    private String fhCatalogOfferId;
}

