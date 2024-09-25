package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.CreateOfferResponseTO;
import eu.possiblex.participantportal.application.entity.ParticipantIdTO;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.exception.EdcOfferCreationException;
import eu.possiblex.participantportal.business.entity.exception.FhOfferCreationException;

public interface ProviderService {
    /**
     * Given a request for creating an offering in the Fraunhofer catalog and a request for creating an EDC offer,
     * create the service offering and the offer in the EDC catalog.
     *
     * @param request request for creating a service offering
     * @return create offer response object
     */
    CreateOfferResponseTO createOffering(CreateServiceOfferingRequestBE request)
        throws FhOfferCreationException, EdcOfferCreationException;

    /**
     * Return the participant's id.
     *
     * @return participant id
     */
    ParticipantIdTO getParticipantId();
}
