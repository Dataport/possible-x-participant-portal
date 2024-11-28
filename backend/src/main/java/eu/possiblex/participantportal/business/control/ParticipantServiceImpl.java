package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubjectSubset;
import eu.possiblex.participantportal.business.entity.exception.ParticipantNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing participant-related operations.
 */
@Service
@Slf4j
public class ParticipantServiceImpl implements ParticipantService{

    private final String participantId;

    private final FhCatalogClient fhCatalogClient;

    public ParticipantServiceImpl(@Autowired FhCatalogClient fhCatalogClient, @Value("${participant-id}") String participantId) {

        this.fhCatalogClient = fhCatalogClient;

        this.participantId = participantId;
    }

    @Override
    public String getParticipantId() {

        return participantId;
    }

    @Override
    public PxExtendedLegalParticipantCredentialSubjectSubset getParticipantDetails(String participantId)
        throws ParticipantNotFoundException {

        return fhCatalogClient.getFhCatalogParticipant(participantId);
    }

    @Override
    public PxExtendedLegalParticipantCredentialSubjectSubset getParticipantDetails()
        throws ParticipantNotFoundException {

        return getParticipantDetails(participantId);
    }
}
