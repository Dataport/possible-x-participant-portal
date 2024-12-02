package eu.possiblex.participantportal.business.control;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.JsonDocument;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedLegalParticipantCredentialSubjectSubset;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.ParticipantNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.SparqlQueryException;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.business.entity.fh.OfferingDetailsSparqlQueryResult;
import eu.possiblex.participantportal.business.entity.fh.ParticipantNameSparqlQueryResult;
import eu.possiblex.participantportal.business.entity.fh.SparqlQueryResponse;
import jakarta.json.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.StringReader;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import java.util.*;


@Service
@Slf4j
public class FhCatalogClientImpl implements FhCatalogClient {

    private final TechnicalFhCatalogClient technicalFhCatalogClient;

    private final SparqlFhCatalogClient sparqlFhCatalogClient;

    private final ObjectMapper objectMapper;

    public FhCatalogClientImpl(@Autowired TechnicalFhCatalogClient technicalFhCatalogClient,
        @Autowired ObjectMapper objectMapper, @Autowired SparqlFhCatalogClient sparqlFhCatalogClient) {

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
        try {
            if( doesContainData ) {
                catalogOfferId = technicalFhCatalogClient.addServiceOfferingWithDataToFhCatalog(serviceOfferingCredentialSubject, offerId);
            }
            else {
                catalogOfferId = technicalFhCatalogClient.addServiceOfferingToFhCatalog(serviceOfferingCredentialSubject, offerId);
            }
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
        } catch (JsonLdError e) {
            throw new RuntimeException("Failed to parse fh catalog content json: " + jsonContent, e);
        }
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
    public PxExtendedServiceOfferingCredentialSubject getFhCatalogOffer(String offeringId) throws OfferNotFoundException {
        try {
            String jsonContent;
            try {
                jsonContent = getFhCatalogContent(offeringId, technicalFhCatalogClient::getFhCatalogOfferWithData);
            } catch (RuntimeException e) {
                jsonContent = getFhCatalogContent(offeringId, technicalFhCatalogClient::getFhCatalogOffer);
            }
            try {
                JsonObject parsedCatalogOffer = parseCatalogContent(jsonContent, PxExtendedServiceOfferingCredentialSubject.TYPE, PxExtendedServiceOfferingCredentialSubject.CONTEXT);
                return objectMapper.readValue(parsedCatalogOffer.toString(), PxExtendedServiceOfferingCredentialSubject.class);
            } catch (JsonProcessingException e) {
                throw new JsonException("failed to parse fh catalog offer json: " + jsonContent, e);
            }
        } catch (RuntimeException e) {
            throw new OfferNotFoundException("Offer not found: " + e.getMessage());
        }
    }

    @Override
    public PxExtendedLegalParticipantCredentialSubjectSubset getFhCatalogParticipant(String participantId) throws
        ParticipantNotFoundException {
        try {
            String jsonContent = getFhCatalogContent(participantId, this.technicalFhCatalogClient::getFhCatalogParticipant);
            try {
                JsonObject parsedCatalogParticipant = parseCatalogContent(jsonContent, PxExtendedLegalParticipantCredentialSubjectSubset.TYPE, PxExtendedLegalParticipantCredentialSubjectSubset.CONTEXT);
                return objectMapper.readValue(parsedCatalogParticipant.toString(), PxExtendedLegalParticipantCredentialSubjectSubset.class);
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
            if( doesContainData ) {
                technicalFhCatalogClient.deleteServiceOfferingWithDataFromFhCatalog(offeringId);
            }
            else {
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

    @Override
    public Map<String, ParticipantNameSparqlQueryResult> getParticipantNames(Collection<String> dapsIds) {
        String query = """
            PREFIX gx: <https://w3id.org/gaia-x/development#>
            PREFIX px: <http://w3id.org/gaia-x/possible-x#>
            
            SELECT ?uri ?dapsId ?name WHERE {
              ?uri a gx:LegalParticipant;
              px:dapsId ?dapsId;
              gx:name ?name .
              FILTER(?dapsId IN (""" + String.join(",", dapsIds.stream()
            .map(id -> "\"" + id + "\"").toList()) +  "))" + """
            }
            """;
        String stringResult = sparqlFhCatalogClient.queryCatalog(query, null);

        SparqlQueryResponse<ParticipantNameSparqlQueryResult> result;
        try {
            result = objectMapper.readValue(stringResult, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new SparqlQueryException("Error during query deserialization", e);
        }

        return result.getResults().getBindings().stream()
            .collect(HashMap::new, (map, p)
                -> map.put(p.getDapsId(), p), HashMap::putAll);
    }

    @Override
    public Map<String, OfferingDetailsSparqlQueryResult> getServiceOfferingDetails(Collection<String> assetIds) {
        return getOfferingDetails(assetIds, "gx:ServiceOffering");
    }

    @Override
    public Map<String, OfferingDetailsSparqlQueryResult> getDataOfferingDetails(Collection<String> assetIds) {
        return getOfferingDetails(assetIds, "px:DataProduct");
    }

    private Map<String, OfferingDetailsSparqlQueryResult> getOfferingDetails(Collection<String> assetIds, String type) {

        String query = """
            PREFIX gx: <https://w3id.org/gaia-x/development#>
            PREFIX px: <http://w3id.org/gaia-x/possible-x#>
            PREFIX schema: <https://schema.org/>
            SELECT ?uri ?assetId ?name ?description ?providerUrl WHERE {
              ?uri a\s""" + type + """
              ;
              schema:name ?name;
              schema:description ?description;
              px:providerUrl ?providerUrl;
              px:assetId ?assetId .
              FILTER(?assetId IN (""" + String.join(",", assetIds.stream()
            .map(id -> "\"" + id + "\"").toList()) +  "))" + """
            }
            """;
        log.info("Sparql Query: {}", query);
        String stringResult = sparqlFhCatalogClient.queryCatalog(query, null);
        log.info("Sparql Query Result: {}", stringResult);
        SparqlQueryResponse<OfferingDetailsSparqlQueryResult> result;
        try {
            result = objectMapper.readValue(stringResult, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new SparqlQueryException("Error during query deserialization", e);
        }

        return result.getResults().getBindings().stream()
            .collect(HashMap::new, (map, p)
                -> map.put(p.getAssetId(), p), HashMap::putAll);
    }

}
