package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.entity.ConsumeOfferRequestTO;
import eu.possiblex.participantportal.application.entity.SelectOfferRequestTO;
import eu.possiblex.participantportal.business.control.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This is an integration test that tests as much of the backend as possible. Here, all real components are used from
 * all layers. Only the interface components which connect to other systems are mocked.
 */
@SpringBootTest
@ContextConfiguration(classes = { ConsumerModuleTest.TestConfig.class })
@AutoConfigureMockMvc
class ConsumerModuleTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EdcClient edcClient;

    @Autowired
    private TechnicalFhCatalogClient technicalFhCatalogClient;

    @Autowired
    private SparqlFhCatalogClient sparqlFhCatalogClient;

    @BeforeEach
    void setup() {

        reset(technicalFhCatalogClient);
        reset(edcClient);
        reset(sparqlFhCatalogClient);
    }

    @Test
    @WithMockUser(username = "admin")
    void acceptContractOfferSucceeds() throws Exception {

        this.mockMvc.perform(post("/consumer/offer/accept").content(RestApiHelper.asJsonString(
                ConsumeOfferRequestTO.builder().edcOfferId(EdcClientFake.FAKE_ID).counterPartyAddress("counterPartyAddress")
                    .dataOffering(true).build())).contentType(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    void selectingOfferSucceeds() throws Exception {

        // GIVEN

        String expectedEdcProviderUrl = "EXPECTED_PROVIDER_URL_VALUE"; // from the "px:providerURL" attribute in the test data offer
        String expectedAssetId = "EXPECTED_ASSET_ID_VALUE"; // from the "px:assetId" attribute in the test data offer
        String expectedProviderId = "did:web:portal.dev.possible-x.de:participant:df15587a-0760-32b5-9c42-bb7be66e8076";

        // WHEN/THEN

        this.mockMvc.perform(post("/consumer/offer/select").content(RestApiHelper.asJsonString(
                    SelectOfferRequestTO.builder().fhCatalogOfferId(ConsumerServiceFake.VALID_FH_OFFER_ID).build()))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.catalogOffering['px:providerUrl']").value(expectedEdcProviderUrl))
            .andExpect(jsonPath("$.edcOfferId").value(expectedAssetId))
            .andExpect(jsonPath("$.dataOffering").value(true)).andExpect(jsonPath("$.providerDetails").exists())
            .andExpect(jsonPath("$.providerDetails.participantId").value(expectedProviderId))
            .andExpect(jsonPath("$.providerDetails.participantName").value("EXPECTED_NAME_VALUE"))
            .andExpect(jsonPath("$.providerDetails.participantEmail").value("EXPECTED_MAIL_ADDRESS_VALUE"))
            .andExpect(jsonPath("$.offerRetrievalDate").exists());

        // THEN

        // FH Catalog should have been queried with the offer ID given in the request
        verify(technicalFhCatalogClient, Mockito.times(1)).getFhCatalogOfferWithData(
            ConsumerServiceFake.VALID_FH_OFFER_ID);
    }

    @Test
    void selectingOfferWithoutDataSucceeds() throws Exception {

        // GIVEN

        String expectedEdcProviderUrl = "EXPECTED_PROVIDER_URL_VALUE"; // from the "px:providerURL" attribute in the test data offer
        String expectedAssetId = "EXPECTED_ASSET_ID_VALUE"; // from the "px:assetId" attribute in the test data offer
        String expectedProviderId = "did:web:portal.dev.possible-x.de:participant:df15587a-0760-32b5-9c42-bb7be66e8076";

        // WHEN/THEN

        this.mockMvc.perform(post("/consumer/offer/select").content(RestApiHelper.asJsonString(
                    SelectOfferRequestTO.builder().fhCatalogOfferId(ConsumerServiceFake.VALID_FH_OFFER_ID).build()))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.catalogOffering['px:providerUrl']").value(expectedEdcProviderUrl))
            .andExpect(jsonPath("$.edcOfferId").value(expectedAssetId))
            .andExpect(jsonPath("$.dataOffering").value(false)).andExpect(jsonPath("$.providerDetails").exists())
            .andExpect(jsonPath("$.providerDetails.participantId").value(expectedProviderId))
            .andExpect(jsonPath("$.providerDetails.participantName").value("EXPECTED_NAME_VALUE"))
            .andExpect(jsonPath("$.providerDetails.participantEmail").value("EXPECTED_MAIL_ADDRESS_VALUE"))
            .andExpect(jsonPath("$.offerRetrievalDate").exists());

        // THEN

        // FH Catalog should have been queried with the offer ID given in the request
        verify(technicalFhCatalogClient, Mockito.times(1)).getFhCatalogOfferWithData(
            ConsumerServiceFake.VALID_FH_OFFER_ID);
        verify(technicalFhCatalogClient, Mockito.times(1)).getFhCatalogOffer(ConsumerServiceFake.VALID_FH_OFFER_ID);
    }

    @Test
    void selectingOfferWhichIsNotInEdcThrows404() throws Exception {

        // GIVEN

        // WHEN/THEN

        this.mockMvc.perform(post("/consumer/offer/select").content(RestApiHelper.asJsonString(
                    SelectOfferRequestTO.builder().fhCatalogOfferId(ConsumerServiceFake.VALID_FH_OFFER_ID).build()))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void selectOfferForNotExistingFhCatalogOfferResultsIn404Response() throws Exception {

        // GIVEN

        // WHEN/THEN

        this.mockMvc.perform(post("/consumer/offer/select").content(RestApiHelper.asJsonString(
                    SelectOfferRequestTO.builder().fhCatalogOfferId(ConsumerServiceFake.VALID_FH_OFFER_ID).build()))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public EdcClient edcClient() {

            return Mockito.spy(new EdcClientFake());
        }

        @Bean
        @Primary
        public TechnicalFhCatalogClient technicalFhCatalogClient() {

            return Mockito.spy(new TechnicalFhCatalogClientFake());
        }

        @Bean
        @Primary
        public SparqlFhCatalogClient sparqlFhCatalogClient() {

            return Mockito.spy(new SparqlFhCatalogClientFake());
        }

    }

}
