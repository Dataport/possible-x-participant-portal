package eu.possiblex.participantportal.business.control;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.JsonDocument;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.business.entity.OfferRetrievalResponseBE;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubjectSubset;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.ParticipantNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.SparqlQueryException;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.business.entity.fh.OfferingDetailsSparqlQueryResult;
import eu.possiblex.participantportal.business.entity.fh.ParticipantDetailsSparqlQueryResult;
import eu.possiblex.participantportal.business.entity.fh.SparqlQueryResponse;
import jakarta.json.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.StringReader;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

@Service
@Slf4j
public class FhCatalogClientImpl implements FhCatalogClient {

    private static final String PARTICIPANT_URI_PREFIX = "https://piveau.io/set/resource/legal-participant/";

    private final TechnicalFhCatalogClient technicalFhCatalogClient;

    private final SparqlFhCatalogClient sparqlFhCatalogClient;

    private final ObjectMapper objectMapper;

    @Value("${participant-id}")
    private String participantId;

    public FhCatalogClientImpl(@Autowired TechnicalFhCatalogClient technicalFhCatalogClient,
                               @Autowired SparqlFhCatalogClient sparqlFhCatalogClient, @Autowired ObjectMapper objectMapper) {

        this.technicalFhCatalogClient = technicalFhCatalogClient;
        this.objectMapper = objectMapper;
        this.sparqlFhCatalogClient = sparqlFhCatalogClient;
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
            PxExtendedServiceOfferingCredentialSubject serviceOfferingCredentialSubject, boolean doesContainData) {

        log.info("sending to catalog");

        String offerId = serviceOfferingCredentialSubject.getId(); // just use the ID also for the offer in the catalog
        FhCatalogIdResponse catalogOfferId = null;
        String verMethod = participantId + "#JWK2020-PossibleLetsEncrypt";
        try {
            if (doesContainData) {
                catalogOfferId = technicalFhCatalogClient.addServiceOfferingWithDataToFhCatalog(serviceOfferingCredentialSubject,
                        offerId, verMethod);
            } else {
                catalogOfferId = technicalFhCatalogClient.addServiceOfferingToFhCatalog(serviceOfferingCredentialSubject,
                        offerId, verMethod);
            }
        } catch (Exception e) {
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
        } catch (JsonLdError e) {
            throw new RuntimeException("Failed to parse fh catalog content json: " + jsonContent, e);
        }
    }

    @Override
    public Map<String, ParticipantDetailsSparqlQueryResult> getParticipantDetailsByIds(Collection<String> participantDids) {

        boolean participantIdsAvailable = participantDids != null && !participantDids.isEmpty();

        String conditionalFilterClause = participantIdsAvailable ? "FILTER(?uri IN (" + String.join(",",
            participantDids.stream().map(id -> "<" + PARTICIPANT_URI_PREFIX + id + ">").toList()) + "))" : "";

        String query = """
            PREFIX gx: <https://w3id.org/gaia-x/development#>
            PREFIX schema: <https://schema.org/>
            PREFIX px: <http://w3id.org/gaia-x/possible-x#>
            
            SELECT ?uri ?name ?mailAddress WHERE {
              ?uri a gx:LegalParticipant;
              schema:name ?name;
              px:mailAddress ?mailAddress .
            """ + conditionalFilterClause + """
            }
            """;

        String stringResult = sparqlFhCatalogClient.queryCatalog(query, null);

        SparqlQueryResponse<ParticipantDetailsSparqlQueryResult> result;
        try {
            result = objectMapper.readValue(stringResult, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new SparqlQueryException("Error during query deserialization", e);
        }

        return result.getResults().getBindings().stream()
                .collect(HashMap::new, (map, p) -> map.put(p.getUri().replace(PARTICIPANT_URI_PREFIX, ""), p),
                        HashMap::putAll);
    }

    @Override
    public Map<String, ParticipantDetailsSparqlQueryResult> getParticipantDetails() {
        return getParticipantDetailsByIds(null);
    }

    @Override
    public Map<String, OfferingDetailsSparqlQueryResult> getOfferingDetailsByAssetIds(Collection<String> assetIds) {

        String query = """
            PREFIX gx: <https://w3id.org/gaia-x/development#>
            PREFIX schema: <https://schema.org/>
            PREFIX px: <http://w3id.org/gaia-x/possible-x#>
            
            SELECT ?uri ?assetId ?name ?providerUrl ?description ?aggregationOf WHERE {
              ?uri a px:PossibleXServiceOfferingExtension;
              schema:name ?name;
              px:providerUrl ?providerUrl;
              px:assetId ?assetId .
              OPTIONAL { ?uri schema:description ?description } .
              OPTIONAL { ?uri gx:aggregationOf ?aggregationOf } .
              FILTER(?assetId IN (""" + String.join(",", assetIds.stream().map(id -> "\"" + id + "\"").toList()) + "))"
                + """
            }
            """;

        String stringResult = sparqlFhCatalogClient.queryCatalog(query, null);

        SparqlQueryResponse<OfferingDetailsSparqlQueryResult> result;
        try {
            result = objectMapper.readValue(stringResult, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new SparqlQueryException("Error during query deserialization", e);
        }

        return result.getResults().getBindings().stream()
                .collect(HashMap::new, (map, p) -> map.put(p.getAssetId(), p), HashMap::putAll);
    }

    private String getFhCatalogContent(String id, UnaryOperator<String> fetchFunction) {

        String jsonContent;
        try {
            jsonContent = fetchFunction.apply(id);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                throw new RuntimeException("no FH Catalog content found with ID " + id);
            }
            throw e;
        }
        return jsonContent;
    }

    @Override
    public OfferRetrievalResponseBE getFhCatalogOffer(String offeringId)
            throws OfferNotFoundException {

        try {
            String jsonContent;
            OffsetDateTime currentDateTime;
            // since the Piveau catalog now has two endpoints for fetching offerings (one for data offerings, one for service offerings)
            // and we do not know what type of offering we have based on the ID, we currently have to try one endpoint and
            // if the request fails, retry with the other endpoint.
            // This is unfortunately unavoidable with the current Piveau API design.
            try {
                // capture the timestamp of retrieval
                currentDateTime = OffsetDateTime.now();
                // try to fetch offer as data offer
                jsonContent = getFhCatalogContent(offeringId, technicalFhCatalogClient::getFhCatalogOfferWithData);
            } catch (RuntimeException e) {
                // capture the timestamp of retrieval
                currentDateTime = OffsetDateTime.now();
                // retry to fetch offer as service offer
                jsonContent = getFhCatalogContent(offeringId, technicalFhCatalogClient::getFhCatalogOffer);
            }
            PxExtendedServiceOfferingCredentialSubject offer = getOfferingCredentialSubjectFromJsonString(jsonContent);
            return new OfferRetrievalResponseBE(offer, currentDateTime);
        } catch (RuntimeException e) {
            throw new OfferNotFoundException("Offer not found: " + e.getMessage());
        }
    }

    private PxExtendedServiceOfferingCredentialSubject getOfferingCredentialSubjectFromJsonString(
        String jsonContent) {

        try {
            JsonObject parsedCatalogOffer = parseCatalogContent(jsonContent,
                    PxExtendedServiceOfferingCredentialSubject.TYPE,
                    PxExtendedServiceOfferingCredentialSubject.CONTEXT);
            return objectMapper.readValue(parsedCatalogOffer.toString(),
                    PxExtendedServiceOfferingCredentialSubject.class);
        } catch (JsonProcessingException e) {
            throw new JsonException("failed to parse fh catalog offer json: " + jsonContent, e);
        }
    }

    @Override
    public PxExtendedLegalParticipantCredentialSubjectSubset getFhCatalogParticipant(String participantId)
            throws ParticipantNotFoundException {

        try {
            String jsonContent = getFhCatalogContent(participantId,
                    this.technicalFhCatalogClient::getFhCatalogParticipant);
            try {
                JsonObject parsedCatalogParticipant = parseCatalogContent(jsonContent,
                        PxExtendedLegalParticipantCredentialSubjectSubset.TYPE,
                        PxExtendedLegalParticipantCredentialSubjectSubset.CONTEXT);
                return objectMapper.readValue(parsedCatalogParticipant.toString(),
                        PxExtendedLegalParticipantCredentialSubjectSubset.class);
            } catch (JsonProcessingException e) {
                throw new JsonException("failed to parse fh catalog participant json: " + jsonContent, e);
            }
        } catch (RuntimeException e) {
            throw new ParticipantNotFoundException("Participant not found: " + e.getMessage());
        }
    }

    @Override
    public void deleteServiceOfferingFromFhCatalog(String offeringId, boolean doesContainData) {

        log.info("deleting offer from fh catalog with ID {}, contains data: {}", offeringId, doesContainData);
        try {
            if (doesContainData) {
                technicalFhCatalogClient.deleteServiceOfferingWithDataFromFhCatalog(offeringId);
            } else {
                technicalFhCatalogClient.deleteServiceOfferingFromFhCatalog(offeringId);
            }
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                log.warn("no FH Catalog offer found with ID {} - nothing to delete", offeringId);
            } else {
                log.error("error when trying to delete offer from catalog!", e);
            }
        }
    }
}
