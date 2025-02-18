package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferDetailsTO {
    @Schema(description = "The ID which is used to identify the offering in the EDC catalog. Currently, this is the asset ID, because an asset will only be used in one offering.",
        example = "8d3c927a-9bb7-4bc8-a3e7-4f9c9a57d571")
    private String edcOfferId;

    @Schema(description = "Offering credential subject as retrieved from the catalog")
    private PxExtendedServiceOfferingCredentialSubject catalogOffering;

    @Schema(description = "Flag whether the offering contains data resources or not", example = "true")
    private boolean dataOffering;

    @Schema(description = "List of enforcement policies for this offering")
    private List<EnforcementPolicy> enforcementPolicies;

    @Schema(description = "Provider details")
    private ParticipantDetailsTO providerDetails;

    @Schema(description = "Date when the offering was retrieved from the catalog", example = "2024-12-31T23:59:59Z")
    private OffsetDateTime offerRetrievalDate;
}
