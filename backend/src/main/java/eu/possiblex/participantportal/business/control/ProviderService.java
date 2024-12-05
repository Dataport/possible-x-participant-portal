package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.CreateOfferResponseTO;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;

public interface ProviderService {
    /**
     * Given a request for creating an offering in the Fraunhofer catalog and a request for creating an EDC offer,
     * create the service offering and the offer in the EDC catalog.
     *
     * @param request request for creating a service offering
     * @return create offer response object
     */
    CreateOfferResponseTO createOffering(CreateServiceOfferingRequestBE request);

}
