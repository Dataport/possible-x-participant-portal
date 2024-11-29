package eu.possiblex.participantportal.business.control;

import com.fasterxml.jackson.databind.JsonNode;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubjectSubset;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.ParticipantNotFoundException;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.business.entity.fh.OfferingDetailsQueryResult;
import eu.possiblex.participantportal.business.entity.fh.ParticipantNameQueryResult;

import java.util.Collection;
import java.util.Map;

public interface FhCatalogClient {
    /**
     * Add an offer to the FH catalog.
     *
     * @param serviceOfferingCredentialSubject the offer to be added to the FH catalog
     * @param doesContainData true: The offer contains data. false: otherwise
     * @return the ID of the created offer
     */
    FhCatalogIdResponse addServiceOfferingToFhCatalog(
        PxExtendedServiceOfferingCredentialSubject serviceOfferingCredentialSubject, boolean doesContainData);

    PxExtendedServiceOfferingCredentialSubject getFhCatalogOffer(String offeringId) throws OfferNotFoundException;

    PxExtendedLegalParticipantCredentialSubjectSubset getFhCatalogParticipant(String participant_id) throws
        ParticipantNotFoundException;

    /**
     * Delete an offer form the FH catalog.
     *
     * @param offeringId the ID of the offer to be deleted
     * @param doesContainData true: The offer contains data. false: otherwise
     */
    void deleteServiceOfferingFromFhCatalog(String offeringId, boolean doesContainData);

    Map<String, ParticipantNameQueryResult> getParticipantNames(Collection<String> dapsIds);

    Map<String, OfferingDetailsQueryResult> getServiceOfferingDetails(Collection<String> assetIds);

    Map<String, OfferingDetailsQueryResult> getDataOfferingDetails(Collection<String> assetIds);
}
