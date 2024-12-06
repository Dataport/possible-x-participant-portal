package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.CreateOfferResponseTO;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.DataProductPrefillFieldsBE;
import eu.possiblex.participantportal.business.entity.PrefillFieldsBE;

public class ProviderServiceFake implements ProviderService {

    public static final String CREATE_OFFER_RESPONSE_ID = "abc123";

    public static final String PARTICIPANT_ID = "did:web:test.eu";

    public static final String SERVICE_OFFERING_NAME = "Service Offering Name";

    public static final String SERVICE_OFFERING_DESCRIPTION = "Service Offering Description";

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

    /**
     * Return the participant's id.
     *
     * @return participant id
     */
    @Override
    public PrefillFieldsBE getPrefillFields() {

        return new PrefillFieldsBE(PARTICIPANT_ID, new DataProductPrefillFieldsBE(SERVICE_OFFERING_NAME, SERVICE_OFFERING_DESCRIPTION));
    }
}
