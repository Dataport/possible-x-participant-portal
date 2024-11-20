package eu.possiblex.participantportal.application.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractPartyDetailsRequestTO {
    /**
     * The ID of the participant providing the offer.
     */
    private String providerId;
}
