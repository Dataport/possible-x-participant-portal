package eu.possiblex.participantportal.application.entity;

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
    @NotBlank(message = "Offering ID is required")
    private String fhCatalogOfferId;
}

