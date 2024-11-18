package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.business.entity.fh.OfferingDetailsQueryResult;
import eu.possiblex.participantportal.business.entity.fh.ParticipantNameQueryResult;

import java.util.Collection;
import java.util.Map;

public interface FhCatalogClient {
    FhCatalogIdResponse addServiceOfferingToFhCatalog(
        PxExtendedServiceOfferingCredentialSubject serviceOfferingCredentialSubject);

    PxExtendedServiceOfferingCredentialSubject getFhCatalogOffer(String offeringId) throws OfferNotFoundException;

    Map<String, ParticipantNameQueryResult> getParticipantNames(Collection<String> dapsIds);

    Map<String, OfferingDetailsQueryResult> getOfferingDetails(Collection<String> assetIds);
}
