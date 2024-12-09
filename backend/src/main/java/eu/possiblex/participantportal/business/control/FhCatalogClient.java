package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubjectSubset;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.ParticipantNotFoundException;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.business.entity.fh.ParticipantNameSparqlQueryResult;

import java.util.Map;

import java.util.Collection;
import eu.possiblex.participantportal.business.entity.fh.OfferingDetailsSparqlQueryResult;


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

    PxExtendedLegalParticipantCredentialSubjectSubset getFhCatalogParticipant(String participant_id)
        throws ParticipantNotFoundException;

    /**
     * Delete an offer form the FH catalog.
     *
     * @param offeringId the ID of the offer to be deleted
     * @param doesContainData true: The offer contains data. false: otherwise
     */
    void deleteServiceOfferingFromFhCatalog(String offeringId, boolean doesContainData);

    /**
     * Get the names of the legal participants.
     *
     * @param dapsIds the IDs of the participants
     * @return the names of the participants
     */
    Map<String, ParticipantNameSparqlQueryResult> getParticipantNames(Collection<String> dapsIds);

    /**
     * Get the details of the offerings with the type "px:PossibleXServiceOfferingExtension".
     *
     * @param assetIds referenced in the offerings
     * @return the details of the service offerings
     */
    Map<String, OfferingDetailsSparqlQueryResult> getOfferingDetails(Collection<String> assetIds);
}