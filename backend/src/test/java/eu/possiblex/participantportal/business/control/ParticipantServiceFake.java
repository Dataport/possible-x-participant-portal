package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubjectSubset;
import eu.possiblex.participantportal.business.entity.exception.ParticipantNotFoundException;

public class ParticipantServiceFake implements ParticipantService{

    public static final String PARTICIPANT_ID = "did:web:test.eu";

    public static final String PARTICIPANT_NAME = "Test Organization";

    public static final String PARTICIPANT_EMAIL = "test@org.de";

    public static final String OTHER_PARTICIPANT_ID = "did:web:other.eu";

    public static final String OTHER_PARTICIPANT_NAME = "Other Organization";

    public static final String OTHER_PARTICIPANT_EMAIL = "other@org.de";

    public static final String UNKNOWN_PARTICIPANT_ID = "did:web:unknown.eu";

    @Override
    public String getParticipantId() {

        return PARTICIPANT_ID;
    }

    @Override
    public PxExtendedLegalParticipantCredentialSubjectSubset getParticipantDetails(String participantId)
        throws ParticipantNotFoundException {

        if(participantId.equals(UNKNOWN_PARTICIPANT_ID)) {
            throw new ParticipantNotFoundException("not found");
        }

        return PxExtendedLegalParticipantCredentialSubjectSubset.builder().id(OTHER_PARTICIPANT_ID)
            .name(OTHER_PARTICIPANT_NAME).mailAddress(OTHER_PARTICIPANT_EMAIL).build();
    }

    @Override
    public PxExtendedLegalParticipantCredentialSubjectSubset getParticipantDetails()
        throws ParticipantNotFoundException {

        return PxExtendedLegalParticipantCredentialSubjectSubset.builder().id(PARTICIPANT_ID)
            .name(PARTICIPANT_NAME).mailAddress(PARTICIPANT_EMAIL).build();
    }
}
