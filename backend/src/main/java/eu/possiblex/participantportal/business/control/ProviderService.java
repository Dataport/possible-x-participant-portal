package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.CreateOfferResponseTO;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.exception.EdcOfferCreationException;
import eu.possiblex.participantportal.business.entity.exception.FhOfferCreationException;
import eu.possiblex.participantportal.business.entity.fh.CreateFhOfferBE;

public interface ProviderService {
    /**
     * Given a request for creating a dataset entry in the Fraunhofer catalog and a request for creating an EDC offer,
     * create the dataset entry and the offer in the EDC catalog.
     *
     * @param createFhOfferBE request for creating a dataset entry
     * @param createEdcOfferBE request for creating an EDC offer
     * @return create offer response object
     */
    CreateOfferResponseTO createOffer(CreateFhOfferBE createFhOfferBE, CreateEdcOfferBE createEdcOfferBE)
        throws FhOfferCreationException, EdcOfferCreationException;
}
