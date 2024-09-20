package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.CreateOfferResponseTO;
import eu.possiblex.participantportal.application.entity.ParticipantIdTO;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.exception.EdcOfferCreationException;
import eu.possiblex.participantportal.business.entity.exception.FhOfferCreationException;
import eu.possiblex.participantportal.business.entity.fh.CreateFhServiceOfferingBE;
import eu.possiblex.participantportal.business.entity.fh.catalog.CreateFhDataOfferingBE;

public interface ProviderService {
    /**
     * Given a request for creating a service offering in the Fraunhofer catalog and a request for creating an EDC
     * offer, create the service offering and the offer in the EDC catalog.
     *
     * @param createFhServiceOfferingBE request for creating a dataset entry
     * @param createEdcOfferBE request for creating an EDC offer
     * @return create offer response object
     */
    CreateOfferResponseTO createServiceOffering(CreateFhServiceOfferingBE createFhServiceOfferingBE,
        CreateEdcOfferBE createEdcOfferBE) throws FhOfferCreationException, EdcOfferCreationException;

    /**
     * Given a request for creating a data offering in the Fraunhofer catalog and a request for creating an EDC offer,
     * create the data offering and the offer in the EDC catalog.
     *
     * @param createFhDataOfferingBE request for creating a dataset entry
     * @param createEdcOfferBE request for creating an EDC offer
     * @return create offer response object
     */
    CreateOfferResponseTO createDataOffering(CreateFhDataOfferingBE createFhDataOfferingBE,
        CreateEdcOfferBE createEdcOfferBE) throws FhOfferCreationException, EdcOfferCreationException;

    /**
     * Return the participant's id.
     *
     * @return participant id
     */
    ParticipantIdTO getParticipantId();
}
