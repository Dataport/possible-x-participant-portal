package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubjectSubset;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;

public class FhCatalogClientFake implements FhCatalogClient {
    public static final String FAKE_PROVIDER_ID = "providerId";

    public static final String FAKE_EMAIL_ADDRESS = "example@mail.com";

    @Override
    public FhCatalogIdResponse addServiceOfferingToFhCatalog(
        PxExtendedServiceOfferingCredentialSubject serviceOfferingCredentialSubject, boolean doesContainData) {

        return new FhCatalogIdResponse("id");
    }

    @Override
    public PxExtendedServiceOfferingCredentialSubject getFhCatalogOffer(String datasetId) {

        return null;
    }

    @Override
    public PxExtendedLegalParticipantCredentialSubjectSubset getFhCatalogParticipant(String participant_id) {

        return null;
    }

    public void deleteServiceOfferingFromFhCatalog(String offeringId, boolean doesContainData) {

    }
}
