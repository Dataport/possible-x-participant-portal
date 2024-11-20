package eu.possiblex.participantportal.application.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import eu.possiblex.participantportal.application.control.ProviderApiMapper;
import eu.possiblex.participantportal.application.entity.CreateDataOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.CreateServiceOfferingRequestTO;
import eu.possiblex.participantportal.business.control.*;
import eu.possiblex.participantportal.business.entity.common.CommonConstants;
import eu.possiblex.participantportal.utilities.LogUtils;
import eu.possiblex.participantportal.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
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
@ContextConfiguration(classes = {ProviderModuleTest.TestConfig.class, ProviderRestApiImpl.class, ProviderServiceImpl.class, FhCatalogClientImpl.class})
class ProviderModuleTest extends ProviderTestParent {

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

    private static String TEST_FILES_PATH = "unit_tests/ProviderModuleTest/";

    private static int WIREMOCK_PORT = 9090;

    @RegisterExtension
    private static WireMockExtension wmExt = WireMockExtension.newInstance().options(WireMockConfiguration.wireMockConfig().port(WIREMOCK_PORT)).build();

    private static String FH_CATALOG_SERVICE_PATH = "fhcatalog";

    private static String EDC_SERVICE_PATH = "edc";

    @Test
    void shouldReturnMessageOnCreateServiceOffering() throws Exception {

        // GIVEN

        CreateServiceOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingTOJsonString(),
                CreateServiceOfferingRequestTO.class);

        mockFhCatalogCreateServiceOffering();
        mockEdcCreateAsset();
        mockEdcCreatePolicy();
        mockEdcCreateContractDefinition();

        // WHEN/THEN

        this.mockMvc.perform(post("/provider/offer/service").content(RestApiHelper.asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void shouldReturnMessageOnCreateServiceOfferingWithData() throws Exception {

        // GIVEN

        CreateDataOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingWithDataTOJsonString(),
                CreateDataOfferingRequestTO.class);

        mockFhCatalogCreateServiceOfferingWithData();
        mockEdcCreateAsset();
        mockEdcCreatePolicy();
        mockEdcCreateContractDefinition();

        // WHEN/THEN

        this.mockMvc.perform(post("/provider/offer/data").content(RestApiHelper.asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }

    private void mockFhCatalogCreateServiceOfferingWithData() {
        WireMockRuntimeInfo wm1RuntimeInfo = wmExt.getRuntimeInfo();
        wmExt.stubFor(WireMock.put(WireMock.urlPathMatching("/" + FH_CATALOG_SERVICE_PATH + "/trust/data-product" + ".*"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                { "id":"someId" }
                                """)));
    }

    private void mockFhCatalogCreateServiceOffering() {
        WireMockRuntimeInfo wm1RuntimeInfo = wmExt.getRuntimeInfo();
        wmExt.stubFor(WireMock.put(WireMock.urlPathMatching("/" + FH_CATALOG_SERVICE_PATH + "/trust/service-offering" + ".*"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                { "id":"someId" }
                                """)));
    }

    private void mockEdcCreateAsset() {
        String edcResponseBody = TestUtils.loadTextFile(TEST_FILES_PATH + "edc_create_asset_response.json");
        WireMockRuntimeInfo wm1RuntimeInfo = wmExt.getRuntimeInfo();
        wmExt.stubFor(WireMock.post("/" + EDC_SERVICE_PATH + "/v3/assets")
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(edcResponseBody)));
    }

    private void mockEdcCreatePolicy() {
        String edcResponseBody = TestUtils.loadTextFile(TEST_FILES_PATH + "edc_create_policy_response.json");
        WireMockRuntimeInfo wm1RuntimeInfo = wmExt.getRuntimeInfo();
        wmExt.stubFor(WireMock.post("/" + EDC_SERVICE_PATH + "/v2/policydefinitions")
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(edcResponseBody)));
    }

    private void mockEdcCreateContractDefinition() {
        String edcResponseBody = TestUtils.loadTextFile(TEST_FILES_PATH + "edc_create_contract_definition_response.json");
        WireMockRuntimeInfo wm1RuntimeInfo = wmExt.getRuntimeInfo();
        wmExt.stubFor(WireMock.post("/" + EDC_SERVICE_PATH + "/v2/contractdefinitions")
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(edcResponseBody)));
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public TechnicalFhCatalogClient technicalFhCatalogClient() {
            String baseUrl = "http://localhost:" + String.valueOf(WIREMOCK_PORT) + "/" + FH_CATALOG_SERVICE_PATH;
            WebClient webClient = WebClient.builder().clientConnector(LogUtils.createHttpClient()).baseUrl(baseUrl).defaultHeaders(httpHeaders -> {
                httpHeaders.set("Content-Type", "application/json");
            }).build();
            HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder()
                    .exchangeAdapter(WebClientAdapter.create(webClient)).build();
            return httpServiceProxyFactory.createClient(TechnicalFhCatalogClient.class);
        }

        @Bean
        public EdcClient edcClient() {
            String baseUrl = "http://localhost:" + String.valueOf(WIREMOCK_PORT) + "/" + EDC_SERVICE_PATH;
            WebClient webClient = WebClient.builder().clientConnector(LogUtils.createHttpClient()).baseUrl(baseUrl).build();
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
    }
}
