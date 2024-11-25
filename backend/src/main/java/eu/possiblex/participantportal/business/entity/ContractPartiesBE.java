package eu.possiblex.participantportal.business.entity;

import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubjectSubset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ContractPartiesBE {
    private PxExtendedLegalParticipantCredentialSubjectSubset consumer;
    private PxExtendedLegalParticipantCredentialSubjectSubset provider;
}
