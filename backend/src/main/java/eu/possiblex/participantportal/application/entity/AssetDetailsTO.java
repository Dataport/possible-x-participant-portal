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
public class AssetDetailsTO {
    @Schema(description = "Name of the asset / offering", example = "Some Service")
    private String name;

    @Schema(description = "Description of the asset / offering", example = "Some Service Description")
    private String description;

    @Schema(description = "The ID which is used to identify the offering in the EDC catalog. Currently, this is the asset ID, because an asset will only be used in one offering.", example = "8d3c927a-9bb7-4bc8-a3e7-4f9c9a57d571")
    private String assetId;

    @Schema(description = "Offering ID from the catalog", example = "urn:uuid:a5a5ff71-0e0b-4e3f-944a-6e47eeac3088")
    private String offeringId;

    @Schema(description = "EDC protocol URL of the offering provider", example = "https://edc.someorg.com/protocol")
    private String providerUrl;
}
