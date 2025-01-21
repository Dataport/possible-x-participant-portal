package eu.possiblex.participantportal.application.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import eu.possiblex.participantportal.application.configuration.BoundaryExceptionHandler;
import eu.possiblex.participantportal.application.control.ProviderApiMapper;
import eu.possiblex.participantportal.application.entity.CreateDataOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.CreateServiceOfferingRequestTO;
import eu.possiblex.participantportal.business.control.*;
import eu.possiblex.participantportal.utilities.LogUtils;
import eu.possiblex.participantportal.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This is an integration test that tests as much of the backend as possible. Here, all real components are used from
 * all layers. Only the interface components which connect to other systems are mocked.
 */
@WebMvcTest(ProviderRestApiImpl.class)
@ContextConfiguration(classes = { ProviderModuleTest.TestConfig.class, ProviderRestApiImpl.class,
    ProviderServiceImpl.class, FhCatalogClientImpl.class, EnforcementPolicyParserServiceImpl.class,
    BoundaryExceptionHandler.class })
class ProviderModuleTest extends ProviderTestParent {

    private static String TEST_FILES_PATH = "unit_tests/ProviderModuleTest/";

    private static int WIREMOCK_PORT = 9090;

    @RegisterExtension
    private static WireMockExtension wmExt = WireMockExtension.newInstance()
        .options(WireMockConfiguration.wireMockConfig().port(WIREMOCK_PORT)).build();

    private static String FH_CATALOG_SERVICE_PATH = "fhcatalog";

    private static String FH_CATALOG_SPARQL_PATH = "sparql";

    private static String EDC_SERVICE_PATH = "edc";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EdcClient edcClientMock;

    @Autowired
    private FhCatalogClient fhCatalogClient;

    @Test
    @WithMockUser(username = "admin")
    void shouldReturnMessageOnCreateServiceOfferingWithoutData() throws Exception {

        // GIVEN

        CreateServiceOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingTOJsonString(),
            CreateServiceOfferingRequestTO.class);

        mockFhCatalogCreateServiceOffering("someId");
        mockEdcCreateAsset();
        mockEdcCreatePolicy();
        mockEdcCreateContractDefinition();

        // WHEN/THEN

        this.mockMvc.perform(post("/provider/offer/service").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldReturnMessageOnCreateServiceOfferingWithData() throws Exception {

        // GIVEN

        CreateDataOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingWithDataTOJsonString(),
            CreateDataOfferingRequestTO.class);

        mockFhCatalogCreateServiceOfferingWithData("someID");
        mockEdcCreateAsset();
        mockEdcCreatePolicy();
        mockEdcCreateContractDefinition();

        // WHEN/THEN

        this.mockMvc.perform(post("/provider/offer/data").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldDeleteOfferInFhCatalogOnEdcErrorWhenCreatingServiceOfferingWithoutData() throws Exception {

        // GIVEN

        CreateServiceOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingTOJsonString(),
            CreateServiceOfferingRequestTO.class);

        String id = "someID";
        mockFhCatalogCreateServiceOffering(id);
        mockEdcCreateAssetFailing();
        mockFhCatalogDeleteServiceOfferWithoutData();

        // WHEN/THEN

        Exception e = null;
        try {
            this.mockMvc.perform(post("/provider/offer/service").content(RestApiHelper.asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON));
        } catch (Exception ex) {
            e = ex;
        }

        Assertions.assertNotNull(e);

        String deleteUrl = "/" + FH_CATALOG_SERVICE_PATH + "/resources/service-offering/" + id;
        wmExt.verify(WireMock.exactly(1), WireMock.deleteRequestedFor(WireMock.urlEqualTo(deleteUrl)));
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldDeleteOfferInFhCatalogOnEdcErrorWhenCreatingServiceOfferingWithData() throws Exception {

        // GIVEN

        CreateDataOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingWithDataTOJsonString(),
            CreateDataOfferingRequestTO.class);

        String id = "someID";
        mockFhCatalogCreateServiceOfferingWithData(id);
        mockEdcCreateAssetFailing();
        mockFhCatalogDeleteServiceOfferWithData();

        // WHEN/THEN

        Exception e = null;
        try {
            this.mockMvc.perform(post("/provider/offer/data").content(RestApiHelper.asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON));
        } catch (Exception ex) {
            e = ex;
        }

        Assertions.assertNotNull(e);

        String deleteUrl = "/" + FH_CATALOG_SERVICE_PATH + "/resources/data-product/" + id;
        wmExt.verify(WireMock.exactly(1), WireMock.deleteRequestedFor(WireMock.urlEqualTo(deleteUrl)));
    }

    private void mockFhCatalogDeleteServiceOfferWithData() {

        WireMockRuntimeInfo wm1RuntimeInfo = wmExt.getRuntimeInfo();
        wmExt.stubFor(
            WireMock.delete(WireMock.urlPathMatching("/" + FH_CATALOG_SERVICE_PATH + "/resources/data-product" + ".*"))
                .willReturn(WireMock.aResponse().withStatus(200)));
    }

    private void mockFhCatalogDeleteServiceOfferWithoutData() {

        WireMockRuntimeInfo wm1RuntimeInfo = wmExt.getRuntimeInfo();
        wmExt.stubFor(WireMock.delete(
                WireMock.urlPathMatching("/" + FH_CATALOG_SERVICE_PATH + "/resources/service-offering" + ".*"))
            .willReturn(WireMock.aResponse().withStatus(200)));
    }

    private void mockFhCatalogCreateServiceOfferingWithData(String id) {

        WireMockRuntimeInfo wm1RuntimeInfo = wmExt.getRuntimeInfo();
        wmExt.stubFor(
            WireMock.put(WireMock.urlPathMatching("/" + FH_CATALOG_SERVICE_PATH + "/trust/data-product" + ".*"))
                .withQueryParam("id", WireMock.matching(".*"))
                .withQueryParam("verificationMethod", WireMock.equalTo("did:web:test.eu#JWK2020-PossibleLetsEncrypt"))
                .willReturn(WireMock.aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                    .withBody("{ \"id\":\"" + id + "\" }")));
    }

    private void mockFhCatalogCreateServiceOffering(String id) {

        WireMockRuntimeInfo wm1RuntimeInfo = wmExt.getRuntimeInfo();
        wmExt.stubFor(
            WireMock.put(WireMock.urlPathMatching("/" + FH_CATALOG_SERVICE_PATH + "/trust/service-offering" + ".*"))
                .withQueryParam("id", WireMock.matching(".*"))
                .withQueryParam("verificationMethod", WireMock.equalTo("did:web:test.eu#JWK2020-PossibleLetsEncrypt"))
                .willReturn(WireMock.aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                    .withBody("{ \"id\":\"" + id + "\" }")));
    }

    private void mockEdcCreateAssetFailing() {

        String edcResponseBody = TestUtils.loadTextFile(TEST_FILES_PATH + "edc_create_asset_response.json");
        WireMockRuntimeInfo wm1RuntimeInfo = wmExt.getRuntimeInfo();
        wmExt.stubFor(
            WireMock.post("/" + EDC_SERVICE_PATH + "/v3/assets").willReturn(WireMock.aResponse().withStatus(500)));
    }

    private void mockEdcCreateAsset() {

        String edcResponseBody = TestUtils.loadTextFile(TEST_FILES_PATH + "edc_create_asset_response.json");
        WireMockRuntimeInfo wm1RuntimeInfo = wmExt.getRuntimeInfo();
        wmExt.stubFor(WireMock.post("/" + EDC_SERVICE_PATH + "/v3/assets").willReturn(
            WireMock.aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                .withBody(edcResponseBody)));
    }

    private void mockEdcCreatePolicy() {

        String edcResponseBody = TestUtils.loadTextFile(TEST_FILES_PATH + "edc_create_policy_response.json");
        WireMockRuntimeInfo wm1RuntimeInfo = wmExt.getRuntimeInfo();
        wmExt.stubFor(WireMock.post("/" + EDC_SERVICE_PATH + "/v2/policydefinitions").willReturn(
            WireMock.aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                .withBody(edcResponseBody)));
    }

    private void mockEdcCreateContractDefinition() {

        String edcResponseBody = TestUtils.loadTextFile(
            TEST_FILES_PATH + "edc_create_contract_definition_response.json");
        WireMockRuntimeInfo wm1RuntimeInfo = wmExt.getRuntimeInfo();
        wmExt.stubFor(WireMock.post("/" + EDC_SERVICE_PATH + "/v2/contractdefinitions").willReturn(
            WireMock.aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                .withBody(edcResponseBody)));
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public TechnicalFhCatalogClient technicalFhCatalogClient() {

            String baseUrl = "http://localhost:" + String.valueOf(WIREMOCK_PORT) + "/" + FH_CATALOG_SERVICE_PATH;
            WebClient webClient = WebClient.builder().clientConnector(LogUtils.createHttpClient()).baseUrl(baseUrl)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("Content-Type", "application/json");
                }).build();
            HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder()
                .exchangeAdapter(WebClientAdapter.create(webClient)).build();
            return httpServiceProxyFactory.createClient(TechnicalFhCatalogClient.class);
        }

        @Bean
        public SparqlFhCatalogClient sparqlFhCatalogClient() {

            String baseUrl = "http://localhost:" + String.valueOf(WIREMOCK_PORT) + "/" + FH_CATALOG_SPARQL_PATH;
            WebClient webClient = WebClient.builder().clientConnector(LogUtils.createHttpClient()).baseUrl(baseUrl)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("Content-Type", "application/json");
                }).build();
            HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder()
                .exchangeAdapter(WebClientAdapter.create(webClient)).build();
            return httpServiceProxyFactory.createClient(SparqlFhCatalogClient.class);
        }

        @Bean
        public EdcClient edcClient() {

            String baseUrl = "http://localhost:" + String.valueOf(WIREMOCK_PORT) + "/" + EDC_SERVICE_PATH;
            WebClient webClient = WebClient.builder().clientConnector(LogUtils.createHttpClient()).baseUrl(baseUrl)
                .build();
            HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder()
                .exchangeAdapter(WebClientAdapter.create(webClient)).build();
            return httpServiceProxyFactory.createClient(EdcClient.class);
        }

        @Bean
        public ProviderServiceMapper providerServiceMapper() {

            return Mappers.getMapper(ProviderServiceMapper.class);
        }

        @Bean
        public ProviderApiMapper providerApiMapper() {

            return Mappers.getMapper(ProviderApiMapper.class);
        }

        @Bean
        public ObjectMapper objectMapper() {

            return new ObjectMapper();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests((authorizeHttpRequests) ->
                    authorizeHttpRequests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);
            return http.build();
        }
    }
}
