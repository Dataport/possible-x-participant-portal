package eu.possiblex.participantportal.business.control;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.business.entity.exception.OfferNotFoundException;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogOffer;
import eu.possiblex.participantportal.business.entity.fh.catalog.DcatDataset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Iterator;
import java.util.Map;

@Service
@Slf4j
public class FhCatalogClientImpl implements FhCatalogClient {

    @Value("${fh.catalog.secret-key}")
    private String fhCatalogSecretKey;

    @Value("${fh.catalog.catalog-name}")
    private String catalogName;

    private TechnicalFhCatalogClient technicalFhCatalogClient;

    public FhCatalogClientImpl(@Autowired TechnicalFhCatalogClient technicalFhCatalogClient) {
        this.technicalFhCatalogClient = technicalFhCatalogClient;
    }

    @Override
    public FhCatalogIdResponse addDatasetToFhCatalog(DcatDataset datasetToCatalogRequest) {
        log.info("using catalog with name: " + catalogName);
        FhCatalogIdResponse response = technicalFhCatalogClient.addDatasetToFhCatalog(createHeaders(), datasetToCatalogRequest, catalogName, "identifiers");
        log.info("got offer id: " + response.getId());
        return response;
    }

    @Override
    public FhCatalogOffer getFhCatalogOffer(String datasetId) throws OfferNotFoundException {
        log.info("fetching offer for fh catalog ID " + datasetId);
        String offerJsonContent = null;
        try {
            offerJsonContent = technicalFhCatalogClient.getFhCatalogOffer(datasetId);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                throw new OfferNotFoundException("no FH Catalog offer found with ID " + datasetId);
            }
            throw e;
        }
        log.info("answer for fh catalog ID: " + offerJsonContent);

        return parseFhOfferJson(offerJsonContent);
    }

    private FhCatalogOffer parseFhOfferJson(String offerJsonContent) {

        FhCatalogOffer fhCatalogOffer;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode offerJson = mapper.readTree(offerJsonContent);

            String assetId = getValueForAttribute("px:assetId", offerJson);
            String providerURL = getValueForAttribute("px:providerUrl", offerJson);
            int aggregationOfSize = getListSizeForAttribute("gx:aggregationOf", offerJson);
            log.info("parsed fh catalog offer id px:assetId: " + assetId);
            log.info("parsed fh catalog offer id px:providerUrl: " + providerURL);
            log.info("parsed fh catalog offer id size(gx:aggregationOf): " + aggregationOfSize);

            if ((assetId == null) || (providerURL == null) || assetId.isEmpty() || providerURL.isEmpty() || aggregationOfSize <= 0) {
                throw new RuntimeException("FH catalog offer did not contain all expected infos! asset-ID: "
                        + assetId + ", provider URL: " + providerURL + ", size of aggregationOf: " + aggregationOfSize);
            }

            fhCatalogOffer = new FhCatalogOffer();
            fhCatalogOffer.setAssetId(assetId);
            fhCatalogOffer.setCounterPartyAddress(providerURL);

        } catch (Exception e) {
            throw new RuntimeException("failed to parse fh catalog offer json: " + offerJsonContent, e);
        }

        return fhCatalogOffer;
    }

    /**
     * Recursively parses the given JSON object. Looks for the first occurrence of an attribute with the given name and returns its value.
     * An attribute in the JSON object will match the given attribute name, if:
     * * it has the same name
     * * it ends with "#" + the given attribute name
     * * it ends with ":" + the given attribute name
     * <p>
     * This method can be improved to better deal with JSON-LD structures in the future if necessary.
     *
     * @param attribute
     * @param jsonNode
     * @return
     */
    private String getValueForAttribute(String attribute, JsonNode jsonNode) {

        if (jsonNode.isArray()) {
            for (int i = 0; i < jsonNode.size(); i++) {
                JsonNode child = jsonNode.get(i);

                String value = getValueForAttribute(attribute, child);

                if (value != null) {
                    return value;
                }
            }

            return null;
        }

        for (Iterator<Map.Entry<String, JsonNode>> iter = jsonNode.fields(); iter.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = iter.next();

            String key = entry.getKey();
            if (key.equals(attribute) || key.endsWith("#" + attribute) || key.endsWith(":" + attribute)) {
                return entry.getValue().asText();
            }

            String value = getValueForAttribute(attribute, entry.getValue());

            if (value != null) {
                return value;
            }
        }

        return null;
    }

    private int getListSizeForAttribute(String attribute, JsonNode jsonNode) {

        if (jsonNode.isArray()) {
            for (int i = 0; i < jsonNode.size(); i++) {
                JsonNode child = jsonNode.get(i);

                int size = getListSizeForAttribute(attribute, child);

                if (size != 0) {
                    return size;
                }
            }
            return 0;
        }

        for (Iterator<Map.Entry<String, JsonNode>> iter = jsonNode.fields(); iter.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = iter.next();

            String key = entry.getKey();
            if (key.equals(attribute) || key.endsWith("#" + attribute) || key.endsWith(":" + attribute)) {
                if (entry.getValue().isArray() && !entry.getValue().isEmpty()) {
                    return entry.getValue().size();
                }
            }

            int size = getListSizeForAttribute(attribute, entry.getValue());

            if (size != 0) {
                return size;
            }
        }

        return 0;
    }

    private Map<String, String> createHeaders() {
        return Map.of("Content-Type", "application/json", "Authorization", "Bearer " + fhCatalogSecretKey);
    }

}
