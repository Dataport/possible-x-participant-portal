package eu.possiblex.participantportal.business.control;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.JsonDocument;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.exception.SparqlQueryException;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.business.entity.fh.OfferingDetailsQueryResult;
import eu.possiblex.participantportal.business.entity.fh.ParticipantNameQueryResult;
import eu.possiblex.participantportal.business.entity.fh.QueryResponse;
import eu.possiblex.participantportal.utilities.LogUtils;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.StringReader;
import java.util.*;

@Service
@Slf4j
public class FhCatalogClientImpl implements FhCatalogClient {

    private final TechnicalFhCatalogClient technicalFhCatalogClient;

    private final SparqlFhCatalogClient sparqlFhCatalogClient;

    private final ObjectMapper objectMapper;

    public FhCatalogClientImpl(@Autowired TechnicalFhCatalogClient technicalFhCatalogClient,
        @Autowired SparqlFhCatalogClient sparqlFhCatalogClient,
        @Autowired ObjectMapper objectMapper) {

        this.technicalFhCatalogClient = technicalFhCatalogClient;
        this.sparqlFhCatalogClient = sparqlFhCatalogClient;
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

    @Override
    public PxExtendedServiceOfferingCredentialSubject getFhCatalogOffer(String offeringId)
        throws OfferNotFoundException {

        log.info("fetching offer for fh catalog ID " + offeringId);
        String offerJsonContent = null;
        try {
            offerJsonContent = technicalFhCatalogClient.getFhCatalogOffer(offeringId);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                throw new OfferNotFoundException("no FH Catalog offer found with ID " + offeringId);
            }
            throw e;
        }

        try {
            JsonDocument input = JsonDocument.of(new StringReader(offerJsonContent));
            JsonDocument offeringFrame = getFrameByType(PxExtendedServiceOfferingCredentialSubject.TYPE,
                PxExtendedServiceOfferingCredentialSubject.CONTEXT);
            JsonObject framedOffering = JsonLd.frame(input, offeringFrame).get();

            return objectMapper.readValue(framedOffering.toString(), PxExtendedServiceOfferingCredentialSubject.class);
        } catch (JsonLdError | JsonProcessingException e) {
            throw new RuntimeException("failed to parse fh catalog offer json: " + offerJsonContent, e);
        }
    }

    public Map<String, ParticipantNameQueryResult> getParticipantNames(Collection<String> dapsIds) {
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

        QueryResponse<ParticipantNameQueryResult> result;
        try {
            result = objectMapper.readValue(stringResult, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new SparqlQueryException("Error during query deserialization", e);
        }

        return result.getResults().getBindings().stream()
            .collect(HashMap::new, (map, p)
                -> map.put(p.getDapsId(), p), HashMap::putAll);
    }

    public Map<String, OfferingDetailsQueryResult> getOfferingDetails(Collection<String> assetIds) {

        String query = """
            PREFIX gx: <https://w3id.org/gaia-x/development#>
            PREFIX px: <http://w3id.org/gaia-x/possible-x#>
            
            SELECT ?uri ?assetId ?name ?description WHERE {
              ?uri a gx:ServiceOffering;
              gx:name ?name;
              gx:description ?description;
              px:assetId ?assetId .
              FILTER(?assetId IN (""" + String.join(",", assetIds.stream()
            .map(id -> "\"" + id + "\"").toList()) +  "))" + """
            }
            """;

        String stringResult = sparqlFhCatalogClient.queryCatalog(query, null);

        QueryResponse<OfferingDetailsQueryResult> result;
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
