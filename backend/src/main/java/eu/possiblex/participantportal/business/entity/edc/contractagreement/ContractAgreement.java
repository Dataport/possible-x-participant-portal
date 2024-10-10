package eu.possiblex.participantportal.business.entity.edc.contractagreement;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import lombok.*;

import java.math.BigInteger;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractAgreement {
    @JsonProperty("@type")
    private String type;

    @JsonProperty("@id")
    private String id;

    private String assetId;

    private Policy policy;

    private BigInteger contractSigningDate;

    private String consumerId;

    private String providerId;

    @JsonProperty("@context")
    private Map<String, String> context;
}
