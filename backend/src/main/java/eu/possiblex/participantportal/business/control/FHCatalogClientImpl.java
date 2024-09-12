package eu.possiblex.participantportal.business.control;

import eu.possiblex.participantportal.business.entity.fh.FhCatalogIdResponse;
import eu.possiblex.participantportal.business.entity.fh.FhCatalogOffer;
import eu.possiblex.participantportal.business.entity.fh.catalog.DcatDataset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Map;

@Service
@Slf4j
public class FHCatalogClientImpl implements FHCatalogClient {

    @Value("${fh.catalog.url}")
    private String fhCatalogUrl;

    @Value("${fh.catalog.secret-key}")
    private String fhCatalogSecretKey;

    @Value("${fh.catalog.catalog-name}")
    private String catalogName;

    @Override
    public FhCatalogIdResponse addDatasetToFhCatalog(DcatDataset datasetToCatalogRequest) {
        TechnicalFhCatalogClient technicalFhCatalogClient = createTechnicalFhCatalogClient();

        FhCatalogIdResponse response = technicalFhCatalogClient.addDatasetToFhCatalog(createHeaders(), datasetToCatalogRequest, catalogName, "identifiers");
        return response;
    }

    @Override
    public FhCatalogOffer getFhCatalogOffer(String datasetId) {
        TechnicalFhCatalogClient technicalFhCatalogClient = createTechnicalFhCatalogClient();
        String offerJson = technicalFhCatalogClient.getFhCatalogOffer(datasetId);
        log.info("answer for fh catalog ID " + datasetId + ": " + offerJson);

        return null;
    }

    private Map<String, String> createHeaders() {
        return Map.of("Content-Type", "application/json", "Authorization", "Bearer " + fhCatalogSecretKey);
    }

    private TechnicalFhCatalogClient createTechnicalFhCatalogClient() {

        WebClient webClient = WebClient.builder().baseUrl(fhCatalogUrl).build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder()
                .exchangeAdapter(WebClientAdapter.create(webClient)).build();
        return httpServiceProxyFactory.createClient(TechnicalFhCatalogClient.class);
    }

}
