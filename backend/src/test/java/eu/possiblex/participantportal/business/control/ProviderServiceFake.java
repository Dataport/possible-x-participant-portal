package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.application.entity.CreateOfferResponseTO;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.fh.CreateFhOfferBE;

public class ProviderServiceFake implements ProviderService {

    public static final String CREATE_OFFER_RESPONSE_ID = "abc123";

    /**
     * Given a request for creating a dataset entry in the Fraunhofer catalog and a request for creating an EDC offer,
     * create the dataset entry and the offer in the EDC catalog.
     *
     * @param createDatasetEntryBE request for creating a dataset entry
     * @param createEdcOfferBE request for creating an EDC offer
     * @return success message (currently an IdResponse)
     */
    @Override
    public CreateOfferResponseTO createOffer(CreateFhOfferBE createDatasetEntryBE, CreateEdcOfferBE createEdcOfferBE) {

        return CreateOfferResponseTO.builder().edcResponseId(CREATE_OFFER_RESPONSE_ID)
            .fhResponseId(CREATE_OFFER_RESPONSE_ID).build();
    }
}
