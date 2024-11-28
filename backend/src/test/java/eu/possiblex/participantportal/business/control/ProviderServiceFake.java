package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.CreateOfferResponseTO;
import eu.possiblex.participantportal.application.entity.ParticipantIdTO;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.exception.EdcOfferCreationException;
import eu.possiblex.participantportal.business.entity.exception.FhOfferCreationException;

public class ProviderServiceFake implements ProviderService {

    public static final String CREATE_OFFER_RESPONSE_ID = "abc123";

    /**
     * Given a request for creating an offering in the Fraunhofer catalog and a request for creating an EDC offer,
     * create the data offering and the offer in the EDC catalog.
     *
     * @param requestBE request for creating an offering
     * @return create offer response object
     */
    @Override
    public CreateOfferResponseTO createOffering(CreateServiceOfferingRequestBE requestBE) {

        return CreateOfferResponseTO.builder().edcResponseId(CREATE_OFFER_RESPONSE_ID)
            .fhResponseId(CREATE_OFFER_RESPONSE_ID).build();
    }
}
