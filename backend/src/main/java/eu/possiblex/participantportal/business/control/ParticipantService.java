package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubjectSubset;
import eu.possiblex.participantportal.business.entity.exception.ParticipantNotFoundException;

public interface ParticipantService {

    /**
     * Get the participant's id.
     *
     * @return the participant id
     */
    String getParticipantId();

    /**
     * Get the participant's details by id.
     *
     * @param participantId the participant id
     * @return the participant details
     * @throws ParticipantNotFoundException if the participant is not found
     */
    PxExtendedLegalParticipantCredentialSubjectSubset getParticipantDetails(String participantId)
        throws ParticipantNotFoundException;

    /**
     * Get the participant's details.
     *
     * @return the participant details
     * @throws ParticipantNotFoundException if the participant is not found
     */
    PxExtendedLegalParticipantCredentialSubjectSubset getParticipantDetails() throws ParticipantNotFoundException;
}
