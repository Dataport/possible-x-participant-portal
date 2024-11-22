package eu.possiblex.participantportal.business.control;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.JsonDocument;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubjectSubset;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.ParticipantNotFoundException;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@Service
@Slf4j
public class FhCatalogClientImpl implements FhCatalogClient {

    private final TechnicalFhCatalogClient technicalFhCatalogClient;

    private final ObjectMapper objectMapper;

    public FhCatalogClientImpl(@Autowired TechnicalFhCatalogClient technicalFhCatalogClient,
        @Autowired ObjectMapper objectMapper) {

        this.technicalFhCatalogClient = technicalFhCatalogClient;
        this.objectMapper = objectMapper;
    }

    private static JsonDocument getFrameByType(List<String> type, Map<String, String> context) {

        JsonObjectBuilder contextBuilder = Json.createObjectBuilder();
        context.forEach(contextBuilder::add);

        JsonArrayBuilder typeArrayBuilder = Json.createArrayBuilder();
        type.forEach(typeArrayBuilder::add);

        return JsonDocument.of(
            Json.createObjectBuilder().add("@context", contextBuilder.build()).add("@type", typeArrayBuilder.build())
                .build());
    }

    @Override
    public FhCatalogIdResponse addServiceOfferingToFhCatalog(
        PxExtendedServiceOfferingCredentialSubject serviceOfferingCredentialSubject) {

        log.info("sending to catalog");

        String offerId = serviceOfferingCredentialSubject.getId(); // just use the ID also for the offer in the catalog
        FhCatalogIdResponse catalogOfferId = null;
        try {
            catalogOfferId = technicalFhCatalogClient.addServiceOfferingToFhCatalog(serviceOfferingCredentialSubject, offerId);
        } catch (Exception e){
            log.error("error when trying to send offer to catalog!", e);
            throw e;
        }

        return catalogOfferId;

    }

    private JsonObject parseCatalogContent(String jsonContent, List<String> type, Map<String, String> context) {
        try {
            JsonDocument input = JsonDocument.of(new StringReader(jsonContent));
            JsonDocument frame = getFrameByType(type, context);
            return JsonLd.frame(input, frame).get();
        } catch (JsonLdError | JsonProcessingException e) {
            throw new RuntimeException("failed to parse fh catalog " + (isOffer ? "offer" : "participant") + " json: " + jsonContent, e);
        }
    }

    private String getFhCatalogContent(String id, UnaryOperator<String> fetchFunction) {
        String jsonContent;
        try {
            jsonContent = fetchFunction.apply(id);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                throw new OfferNotFoundException("no FH Catalog offer found with ID " + id);
            }
            throw e;
        }
        return jsonContent;
    }

    @Override
    public PxExtendedServiceOfferingCredentialSubject getFhCatalogOffer(String offeringId) throws OfferNotFoundException {
        try {
            boolean isOffer = true;
            String jsonContent = getFhCatalogContent(offeringId, isOffer);
            return parseCatalogContent(jsonContent, isOffer, PxExtendedServiceOfferingCredentialSubject.class);
        } catch (ParticipantNotFoundException e) {
            throw new OfferNotFoundException("Something went wrong: ParticipantNotFoundException in getFhCatalogOffer method: " + e.getMessage());
        }
    }

    @Override
    public PxExtendedLegalParticipantCredentialSubjectSubset getFhCatalogParticipant(String participantId) throws
        ParticipantNotFoundException {
        try {
            boolean isOffer = false;
            String jsonContent = getFhCatalogContent(participantId, this.technicalFhCatalogClient::getFhCatalogParticipant);
            return parseCatalogContent(jsonContent, isOffer, PxExtendedLegalParticipantCredentialSubjectSubset.class);
        } catch (OfferNotFoundException e) {
            throw new ParticipantNotFoundException("Something went wrong: OfferNotFoundException in getFhCatalogParticipant method: " + e.getMessage());
        }
    }
}
