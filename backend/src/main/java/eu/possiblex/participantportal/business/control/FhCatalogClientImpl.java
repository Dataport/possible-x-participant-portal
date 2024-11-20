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

    private <T> T parseCatalogContent(String id, boolean isOffer, Class<T> returnType) throws OfferNotFoundException,
        ParticipantNotFoundException {
        log.info("fetching " + (isOffer ? "offer" : "participant") + " for fh catalog ID " + id);
        String jsonContent = null;
        try {
            jsonContent = isOffer ? technicalFhCatalogClient.getFhCatalogOffer(id) : technicalFhCatalogClient.getFhCatalogParticipant(id);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 404 && isOffer) {
                throw new OfferNotFoundException("no FH Catalog offer found with ID " + id);
            }
            if (e.getStatusCode().value() == 404 && !isOffer) {
                throw new ParticipantNotFoundException("no FH Catalog participant found with ID " + id);
            }
            throw e;
        }

        try {
            JsonDocument input = JsonDocument.of(new StringReader(jsonContent));
            JsonDocument offeringFrame = getFrameByType(PxExtendedServiceOfferingCredentialSubject.TYPE,
                PxExtendedServiceOfferingCredentialSubject.CONTEXT);
            JsonObject framedOffering = JsonLd.frame(input, offeringFrame).get();

            return objectMapper.readValue(framedOffering.toString(), returnType);
        } catch (JsonLdError | JsonProcessingException e) {
            throw new RuntimeException("failed to parse fh catalog " + (isOffer ? "offer" : "participant") + " json: " + jsonContent, e);
        }
    }
    @Override
    public PxExtendedServiceOfferingCredentialSubject getFhCatalogOffer(String offeringId) throws OfferNotFoundException {
        try {
            return parseCatalogContent(offeringId, true, PxExtendedServiceOfferingCredentialSubject.class);
        } catch (ParticipantNotFoundException e) {
            throw new OfferNotFoundException(e.getMessage());
        }
    }

    @Override
    public PxExtendedLegalParticipantCredentialSubjectSubset getFhCatalogParticipant(String participantId) throws
        ParticipantNotFoundException {
        try {
            return parseCatalogContent(participantId, false, PxExtendedLegalParticipantCredentialSubjectSubset.class);
        } catch (OfferNotFoundException e) {
            throw new ParticipantNotFoundException(e.getMessage());
        }
    }
}
