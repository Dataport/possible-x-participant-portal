package eu.possiblex.participantportal.business.entity.edc.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.possiblex.participantportal.business.entity.common.JsonLdConstants;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
@Builder
public class TerminateTransferRequest {
    private static final Map<String, String> CONTEXT = JsonLdConstants.EDC_CONTEXT;

    private static final String TYPE = "TerminateTransfer";

    private String reason;

    @JsonProperty("@type")
    public String getType() {

        return TYPE;
    }

    @JsonProperty("@context")
    public Map<String, String> getContext() {

        return CONTEXT;
    }
}
