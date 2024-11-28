package eu.possiblex.participantportal.business.entity.daps;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OmejdnConnectorDetailsRequest {
    @JsonProperty("client_id")
    Collection<String> clientIds;

    @JsonProperty("client_name")
    Collection<String> clientNames;
}
