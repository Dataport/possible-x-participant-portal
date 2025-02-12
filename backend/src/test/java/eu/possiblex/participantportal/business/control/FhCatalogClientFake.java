package eu.possiblex.participantportal.business.control;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.business.entity.OfferRetrievalResponseBE;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubjectSubset;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.business.entity.fh.OfferingDetailsSparqlQueryResult;
import eu.possiblex.participantportal.business.entity.fh.ParticipantDetailsSparqlQueryResult;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public class FhCatalogClientFake implements FhCatalogClient {
    public static final String FAKE_PROVIDER_ID = "providerId";

    public static final String FAKE_DID = "did:web:123";

    public static final String FAKE_EMAIL_ADDRESS = "example@mail.com";

    public static final String INVALID_OFFERING = "invalid";

    public static final String UNPROCESSABLE_OFFERING = "unprocessable";

    public static final String ERROR = "error";

    @Override
    public FhCatalogIdResponse addServiceOfferingToFhCatalog(
        PxExtendedServiceOfferingCredentialSubject serviceOfferingCredentialSubject, boolean doesContainData) {

        if (serviceOfferingCredentialSubject.getName().equals(INVALID_OFFERING)
            || serviceOfferingCredentialSubject.getName().equals(UNPROCESSABLE_OFFERING)) {
            byte[] responseBody;
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            WebClientResponseException e;

            // Custom decoder function
            Function<ResolvableType, JsonNode> decoderFunction;

            if (serviceOfferingCredentialSubject.getName().equals(INVALID_OFFERING)) {
                responseBody = "{\"error\":\"\"}".getBytes(StandardCharsets.UTF_8);
            } else {
                responseBody = "{}".getBytes(StandardCharsets.UTF_8);
            }

            // set custom decoder function
            decoderFunction = resolvableType -> {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.readValue(responseBody, JsonNode.class);
                } catch (Exception ex) {
                    throw new RuntimeException("Failed to decode response body", ex);
                }
            };

            e = new WebClientResponseException(500, "Error", headers, responseBody, StandardCharsets.UTF_8);
            e.setBodyDecodeFunction(decoderFunction); // Set with custom decoder function
            throw e;
        }

        if (serviceOfferingCredentialSubject.getName().equals(ERROR)) {
            throw new RuntimeException("Error");
        }

        return new FhCatalogIdResponse("id");
    }

    @Override
    public OfferRetrievalResponseBE getFhCatalogOffer(String offeringId) {

        return null;
    }

    @Override
    public Map<String, ParticipantDetailsSparqlQueryResult> getParticipantDetailsByIds(Collection<String> dids) {

        return Map.of(OmejdnConnectorApiClientFake.PARTICIPANT_ID,
            ParticipantDetailsSparqlQueryResult.builder().name(OmejdnConnectorApiClientFake.PARTICIPANT_NAME)
                .uri("https://example.com/legal-participant/" + OmejdnConnectorApiClientFake.PARTICIPANT_ID)
                .mailAddress(FAKE_EMAIL_ADDRESS).build());
    }

    @Override
    public Map<String, ParticipantDetailsSparqlQueryResult> getParticipantDetails() {

        return getParticipantDetailsByIds(null);
    }

    @Override
    public Map<String, OfferingDetailsSparqlQueryResult> getOfferingDetailsByAssetIds(Collection<String> assetIds) {

        return Map.of(EdcClientFake.FAKE_ID,
            OfferingDetailsSparqlQueryResult.builder().name("name").description("description")
                .uri("https://example.com/service-offering/" + EdcClientFake.FAKE_ID).assetId(EdcClientFake.FAKE_ID)
                .build());

    }

    @Override
    public PxExtendedLegalParticipantCredentialSubjectSubset getFhCatalogParticipant(String participantId) {

        return null;
    }

    @Override
    public void deleteServiceOfferingFromFhCatalog(String offeringId, boolean doesContainData) {
        // request worked
    }
}
