package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.control.ParticipantApiMapper;
import eu.possiblex.participantportal.application.entity.ParticipantDetailsTO;
import eu.possiblex.participantportal.application.entity.ParticipantIdTO;
import eu.possiblex.participantportal.business.control.ParticipantService;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubjectSubset;
import eu.possiblex.participantportal.business.entity.exception.ParticipantNotFoundException;
import eu.possiblex.participantportal.utilities.PossibleXException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*") // TODO replace this with proper CORS configuration
@Slf4j
public class ParticipantRestApiImpl implements ParticipantRestApi {

    private final ParticipantService participantService;

    private final ParticipantApiMapper participantApiMapper;

    public ParticipantRestApiImpl(@Autowired ParticipantService participantService,
        @Autowired ParticipantApiMapper participantApiMapper) {

        this.participantService = participantService;
        this.participantApiMapper = participantApiMapper;
    }

    /**
     * GET endpoint to retrieve the participant's id
     *
     * @return participant id
     */
    @Override
    public ParticipantIdTO getParticipantId() {

        return participantApiMapper.idToParticipantIdTO(participantService.getParticipantId());
    }

    @Override
    public ParticipantDetailsTO getParticipantDetails(String participantId) {
        log.info("Retrieving participant details for id: {}", participantId);

        PxExtendedLegalParticipantCredentialSubjectSubset participant;
        try {
            participant = participantService.getParticipantDetails(participantId);
        } catch (ParticipantNotFoundException e) {
            throw new PossibleXException(
                "Failed to retrieve participant details for id: " + participantId + ". ParticipantNotFoundException: " + e,
                HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new PossibleXException(
                "Failed to retrieve participant details for id: " + participantId + ". Other Exception: " + e);
        }

        return participantApiMapper.credentialSubjectSubsetToParticipantDetailsTO(participant);
    }

    @Override
    public ParticipantDetailsTO getParticipantDetails() {
        log.info("Retrieving own participant details.");

        PxExtendedLegalParticipantCredentialSubjectSubset participant;
        try {
            participant = participantService.getParticipantDetails();
        } catch (ParticipantNotFoundException e) {
            throw new PossibleXException(
                "Failed to retrieve own participant details. ParticipantNotFoundException: " + e,
                HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new PossibleXException(
                "Failed to retrieve own participant details. Other Exception: " + e);
        }
        return participantApiMapper.credentialSubjectSubsetToParticipantDetailsTO(participant);
    }
}
