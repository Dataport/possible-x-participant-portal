package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.edc.common.IdResponse;
import eu.possiblex.participantportal.business.entity.fh.CreateDatasetEntryBE;

public interface ProviderService {
    /**
     * Given a request for creating a dataset entry in the Fraunhofer catalog and
     * a request for creating an EDC offer, create the dataset entry and the offer in the EDC catalog.
     * @param createDatasetEntryBE request for creating a dataset entry
     * @param createEdcOfferBE request for creating an EDC offer
     * @return success message (currently an IdResponse)
     */
    IdResponse createOffer(CreateDatasetEntryBE createDatasetEntryBE, CreateEdcOfferBE createEdcOfferBE);
}
