package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.entity.ParticipantDetailsTO;
import eu.possiblex.participantportal.application.entity.ParticipantIdTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/participant")
public interface ParticipantRestApi {

    /**
     * GET endpoint to retrieve the participant's id
     *
     * @return participant id
     */
    @GetMapping(value = "/id/me", produces = MediaType.APPLICATION_JSON_VALUE)
    ParticipantIdTO getParticipantId();

    /**
     * GET endpoint to retrieve a participant's details by id
     *
     * @return participant details
     */
    @GetMapping(value = "/details/{participantId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ParticipantDetailsTO getParticipantDetails(@PathVariable String participantId);

    /**
     * GET endpoint to retrieve the participant's details
     *
     * @return participant details
     */
    @GetMapping(value = "/details/me", produces = MediaType.APPLICATION_JSON_VALUE)
    ParticipantDetailsTO getParticipantDetails();
}
