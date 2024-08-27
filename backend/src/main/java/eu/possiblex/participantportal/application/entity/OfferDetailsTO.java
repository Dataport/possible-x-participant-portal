package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferDetailsTO {
    private String offerId;
    private String name;
    private String description;
    private String contentType;
    private String version;
    private List<Policy> policies;
}
