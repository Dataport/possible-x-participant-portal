package eu.possiblex.participantportal.application.configuration;

import eu.possiblex.participantportal.business.control.*;
import eu.possiblex.participantportal.business.control.EdcClient;
import eu.possiblex.participantportal.business.control.SdCreationWizardApiClient;
import eu.possiblex.participantportal.business.control.SparqlFhCatalogClient;
import eu.possiblex.participantportal.business.control.TechnicalFhCatalogClient;
import eu.possiblex.participantportal.utilities.LogUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Map;

@Configuration
@EnableScheduling
public class AppConfigurer {

    private static final int EXCHANGE_STRATEGY_SIZE = 16 * 1024 * 1024;

    private static final ExchangeStrategies EXCHANGE_STRATEGIES = ExchangeStrategies.builder()
        .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(EXCHANGE_STRATEGY_SIZE)).build();

    @Value("${edc.x-api-key}")
    private String edcAccessKey;

    @Value("${edc.mgmt-base-url}")
    private String edcMgmtUrl;

    @Value("${fh.catalog.url}")
    private String fhCatalogUrl;

    @Value("${sd-creation-wizard-api.base-url}")
    private String sdCreationWizardApiBaseUri;

    @Value("${fh.catalog.secret-key}")
    private String fhCatalogSecretKey;

    @Value("${daps-server.base-url}")
    private String dapsServerBaseUri;

    @Bean
    public EdcClient edcClient() {

        WebClient webClient = WebClient.builder().baseUrl(edcMgmtUrl).clientConnector(LogUtils.createHttpClient())
            .defaultHeader("X-API-Key", edcAccessKey).build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder()
            .exchangeAdapter(WebClientAdapter.create(webClient)).build();
        return httpServiceProxyFactory.createClient(EdcClient.class);
    }

    @Bean
    public TechnicalFhCatalogClient technicalFhCatalogClient() {

        WebClient webClient = WebClient.builder().baseUrl(fhCatalogUrl + "/api/hub/repo")
            .clientConnector(LogUtils.createHttpClient()).defaultHeaders(httpHeaders -> {
                httpHeaders.set("Content-Type", "application/json");
                httpHeaders.set("Authorization", "Bearer " + fhCatalogSecretKey);
            }).build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder()
            .exchangeAdapter(WebClientAdapter.create(webClient)).build();
        return httpServiceProxyFactory.createClient(TechnicalFhCatalogClient.class);
    }

    @Bean
    public SparqlFhCatalogClient sparqlFhCatalogClient() {

        WebClient webClient = WebClient.builder().baseUrl(fhCatalogUrl + "/ld/sparql/")
            .clientConnector(LogUtils.createHttpClient()).defaultHeaders(httpHeaders -> {
                httpHeaders.set("Content-Type", "application/json");
                httpHeaders.set("Authorization", "Bearer " + fhCatalogSecretKey);
            }).build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder()
            .exchangeAdapter(WebClientAdapter.create(webClient)).build();
        return httpServiceProxyFactory.createClient(SparqlFhCatalogClient.class);
    }

    @Bean
    public SdCreationWizardApiClient sdCreationWizardApiClient() {

        WebClient webClient = WebClient.builder().exchangeStrategies(EXCHANGE_STRATEGIES)
            .baseUrl(sdCreationWizardApiBaseUri).build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(
            WebClientAdapter.create(webClient)).build();
        return httpServiceProxyFactory.createClient(SdCreationWizardApiClient.class);
    }

    @Bean
    public OmejdnConnectorApiClient dapsConnectorApiClient() {

        WebClient webClient = WebClient.builder().baseUrl(dapsServerBaseUri + "/api/v1/connectors")
            .clientConnector(LogUtils.createHttpClient()).build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder()
            .exchangeAdapter(WebClientAdapter.create(webClient)).build();
        return httpServiceProxyFactory.createClient(OmejdnConnectorApiClient.class);
    }
}
