package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubjectSubset;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.business.entity.fh.OfferingDetailsSparqlQueryResult;
import eu.possiblex.participantportal.business.entity.fh.ParticipantNameSparqlQueryResult;

import java.util.Collection;
import java.util.Map;

public class FhCatalogClientFake implements FhCatalogClient {
    public static final String FAKE_PROVIDER_ID = "providerId";

    public static final String FAKE_DID = "did:web:123";

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
    public Map<String, ParticipantNameSparqlQueryResult> getParticipantNames(Collection<String> dids) {

        return Map.of(FAKE_DID,
            ParticipantNameSparqlQueryResult.builder().name(OmejdnConnectorApiClientFake.PARTICIPANT_NAME)
                .uri("https://piveau.io/set/resource/legal-participant/" + FAKE_DID).build());
    }

    @Override
    public Map<String, OfferingDetailsSparqlQueryResult> getOfferingDetails(Collection<String> assetIds) {

        return Map.of(EdcClientFake.FAKE_ID,
            OfferingDetailsSparqlQueryResult.builder().name("name").description("description")
                .uri("https://piveau.io/set/resource/service-offering/" + EdcClientFake.FAKE_ID).build());

    }

    @Override
    public PxExtendedLegalParticipantCredentialSubjectSubset getFhCatalogParticipant(String participant_id) {

        return null;
    }

    @Override
    public void deleteServiceOfferingFromFhCatalog(String offeringId, boolean doesContainData) {

    }
}
