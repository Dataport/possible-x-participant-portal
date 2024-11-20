package eu.possiblex.participantportal.business.control;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.JsonDocument;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
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
            if( isServiceOfferWithData(serviceOfferingCredentialSubject)) {
                catalogOfferId = technicalFhCatalogClient.addServiceOfferingWithDataToFhCatalog(serviceOfferingCredentialSubject, offerId);
            }
            else {
                catalogOfferId = technicalFhCatalogClient.addServiceOfferingToFhCatalog(serviceOfferingCredentialSubject, offerId);
            }
        } catch (Exception e) {
            log.error("error when trying to send offer to catalog!", e);
            throw e;
        }

        return catalogOfferId;

    }

    /**
     * Check if the service offer payload contains data.
     * Currently, the check is just if gx:aggregationOf is empty.
     *
     * @param serviceOfferPayload the service offer payload
     * @return true: The service offer contains data. false: otherwise
     */
    private boolean isServiceOfferWithData(PxExtendedServiceOfferingCredentialSubject serviceOfferPayload) {
        boolean serviceOfferContainsData = !serviceOfferPayload.getAggregationOf().isEmpty();

        return serviceOfferContainsData;
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

}
