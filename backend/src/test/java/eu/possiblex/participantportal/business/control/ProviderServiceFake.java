package eu.possiblex.participantportal.business.control;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.edc.common.IdResponse;
import eu.possiblex.participantportal.business.entity.fh.CreateFhOfferBE;

public class ProviderServiceFake implements ProviderService{

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
    public ObjectNode createOffer(CreateFhOfferBE createDatasetEntryBE, CreateEdcOfferBE createEdcOfferBE) {
        ObjectNode node =  JsonNodeFactory.instance.objectNode();
        node.put("EDC-ID", CREATE_OFFER_RESPONSE_ID);
        node.put("FH-ID", CREATE_OFFER_RESPONSE_ID);
        return node;
    }
}
