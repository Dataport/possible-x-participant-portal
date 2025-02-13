package eu.possiblex.participantportal.application.entity;

import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcessState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferOfferResponseTO {
    @Schema(description = "The state of the data transfer", example = "COMPLETED")
    private TransferProcessState transferProcessState;
}
