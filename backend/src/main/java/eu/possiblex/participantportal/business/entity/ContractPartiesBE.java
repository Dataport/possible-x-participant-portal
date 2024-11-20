package eu.possiblex.participantportal.business.entity;

import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ContractPartiesBE {
    private PxExtendedLegalParticipantCredentialSubject consumer;
    private PxExtendedLegalParticipantCredentialSubject provider;
}
