package eu.possiblex.participantportal.application.entity;

import eu.possible_x.backend.business.entity.edc.transfer.TransferProcessState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferDetailsTO {
    private TransferProcessState state;
}
