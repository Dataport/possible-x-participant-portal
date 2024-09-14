package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.control.ConsumerApiMapper;
import eu.possiblex.participantportal.application.entity.SelectOfferRequestTO;
import eu.possiblex.participantportal.business.control.*;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatCatalog;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDataset;
import eu.possiblex.participantportal.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This is an integration test that tests as much of the backend as possible. Here, all real components are used from all layers.
 * Only the interface components which connect to other systems are mocked.
 */
@WebMvcTest(ConsumerRestApiImpl.class)
@ContextConfiguration(classes = {ConsumerModuleTest.TestConfig.class, ConsumerRestApiImpl.class, ConsumerServiceImpl.class, FHCatalogClientImpl.class})
public class ConsumerModuleTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ConsumerService consumerService;
    @Autowired
    private FHCatalogClientImpl fhCatalogClient;
    @Autowired
    private EdcClient edcClientMock;
    @Autowired
    private TechnicalFhCatalogClient technicalFhCatalogClientMock;

    private static String TEST_FILES_PATH = "unit_tests/ConsumerModuleTest/";

    @Test
    void selectingOfferSucceeds() throws Exception {

        // GIVEN

        reset(edcClientMock);
        reset(technicalFhCatalogClientMock);

        // let the FH catalog provide the test data offer
        String fhCatalogOfferContent = TestUtils.loadTextFile(TEST_FILES_PATH + "validFhOffer.json");
        Mockito.when(technicalFhCatalogClientMock.getFhCatalogOffer(Mockito.eq(ConsumerServiceMock.VALID_FH_OFFER_ID))).thenReturn(fhCatalogOfferContent);

        String expectedEdcProviderUrl = "EXPECTED_ACCESS_URL_VALUE"; // from the "dcat:accessURL" attribute in the test data offer
        String expectedAssetId = "EXPECTED_ASSET_ID_VALUE"; // from the "https://possible-gaia-x.de/ns/#assetId" attribute in the test data offer

        // let the EDC provide the test data catalog
        DcatDataset mockDatasetWrongOne = new DcatDataset(); // an offer in the EDC Catalog which the user does not look for
        mockDatasetWrongOne.setAssetId("assetIdWhichTheUserDoesNotLookFor");
        mockDatasetWrongOne.setName("wrong");
        mockDatasetWrongOne.setContenttype("wrong");
        mockDatasetWrongOne.setDescription("wrong");
        DcatDataset mockDatasetCorrectOne = new DcatDataset(); // the offer in the EDC Catalog which the user looks for
        mockDatasetCorrectOne.setAssetId(expectedAssetId);
        mockDatasetCorrectOne.setName("correctName");
        mockDatasetCorrectOne.setContenttype("correctContentType");
        mockDatasetCorrectOne.setDescription("correctDescription");
        DcatCatalog edcCatalogAnswerMock = new DcatCatalog();
        edcCatalogAnswerMock.setDataset(List.of(mockDatasetWrongOne, mockDatasetCorrectOne));
        Mockito.when(edcClientMock.queryCatalog(Mockito.any())).thenReturn(edcCatalogAnswerMock);

        // WHEN/THEN

        this.mockMvc.perform(post("/consumer/offer/select")
                        .content(RestApiHelper.asJsonString(SelectOfferRequestTO
                                .builder()
                                .fhCatalogOfferId(ConsumerServiceMock.VALID_FH_OFFER_ID)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.counterPartyAddress").value(expectedEdcProviderUrl))
                .andExpect(jsonPath("$.edcOfferId").value(expectedAssetId))
                .andExpect(jsonPath("$.name").value(mockDatasetCorrectOne.getName()))
                .andExpect(jsonPath("$.description").value(mockDatasetCorrectOne.getDescription()))
                .andExpect(jsonPath("$.contentType").value(mockDatasetCorrectOne.getContenttype()));

        // THEN

        // FH Catalog should have been queried with the offer ID given in the request
        verify(technicalFhCatalogClientMock, Mockito.times(1)).getFhCatalogOffer(ConsumerServiceMock.VALID_FH_OFFER_ID);
    }

    @Test
    void selectingOfferWhichIsNotInEdcThrows404() throws Exception {

        // GIVEN

        reset(edcClientMock);
        reset(technicalFhCatalogClientMock);

        // let the FH catalog provide the test data offer
        String fhCatalogOfferContent = TestUtils.loadTextFile(TEST_FILES_PATH + "validFhOffer.json");
        Mockito.when(technicalFhCatalogClientMock.getFhCatalogOffer(Mockito.eq(ConsumerServiceMock.VALID_FH_OFFER_ID))).thenReturn(fhCatalogOfferContent);

        // let the EDC provide the test data catalog which does not contain the offer from the user
        DcatDataset mockDatasetWrongOne = new DcatDataset(); // an offer in the EDC Catalog which the user does not look for
        mockDatasetWrongOne.setAssetId("assetIdWhichTheUserDoesNotLookFor");
        mockDatasetWrongOne.setName("wrong");
        mockDatasetWrongOne.setContenttype("wrong");
        mockDatasetWrongOne.setDescription("wrong");
        DcatCatalog edcCatalogAnswerMock = new DcatCatalog();
        edcCatalogAnswerMock.setDataset(List.of(mockDatasetWrongOne));
        Mockito.when(edcClientMock.queryCatalog(Mockito.any())).thenReturn(edcCatalogAnswerMock);

        // WHEN/THEN

        this.mockMvc.perform(post("/consumer/offer/select")
                        .content(RestApiHelper.asJsonString(SelectOfferRequestTO
                                .builder()
                                .fhCatalogOfferId(ConsumerServiceMock.VALID_FH_OFFER_ID)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void selectOfferForNotExistingFhCatalogOfferResultsIn404Response() throws Exception {

        // GIVEN

        reset(edcClientMock);
        reset(technicalFhCatalogClientMock);

        // let the FH catalog client throw a 404 error
        WebClientResponseException expectedException = Mockito.mock(WebClientResponseException.class);
        Mockito.when(expectedException.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
        Mockito.when(technicalFhCatalogClientMock.getFhCatalogOffer(Mockito.eq(ConsumerServiceMock.VALID_FH_OFFER_ID))).thenThrow(expectedException);

        // WHEN/THEN

        this.mockMvc.perform(post("/consumer/offer/select")
                        .content(RestApiHelper.asJsonString(SelectOfferRequestTO
                                .builder()
                                .fhCatalogOfferId(ConsumerServiceMock.VALID_FH_OFFER_ID)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void selectOfferForFhOfferWithoutAssetIdReturnsInternalError() throws Exception {

        // GIVEN

        reset(edcClientMock);
        reset(technicalFhCatalogClientMock);

        // let the FH catalog provide the test data offer which does not contain an asset ID
        String fhCatalogOfferContent = TestUtils.loadTextFile(TEST_FILES_PATH + "invalidFhOfferNoAssetId.json");
        Mockito.when(technicalFhCatalogClientMock.getFhCatalogOffer(Mockito.eq(ConsumerServiceMock.VALID_FH_OFFER_ID))).thenReturn(fhCatalogOfferContent);

        // WHEN/THEN

        this.mockMvc.perform(post("/consumer/offer/select")
                        .content(RestApiHelper.asJsonString(SelectOfferRequestTO
                                .builder()
                                .fhCatalogOfferId(ConsumerServiceMock.VALID_FH_OFFER_ID)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    void selectOfferForFhOfferWithoutAccessUrlReturnsInternalError() throws Exception {

        // GIVEN

        reset(edcClientMock);
        reset(technicalFhCatalogClientMock);

        // let the FH catalog provide the test data offer which does not contain an asset ID
        String fhCatalogOfferContent = TestUtils.loadTextFile(TEST_FILES_PATH + "invalidFhOfferNoAccessUrl.json");
        Mockito.when(technicalFhCatalogClientMock.getFhCatalogOffer(Mockito.eq(ConsumerServiceMock.VALID_FH_OFFER_ID))).thenReturn(fhCatalogOfferContent);

        // WHEN/THEN

        this.mockMvc.perform(post("/consumer/offer/select")
                        .content(RestApiHelper.asJsonString(SelectOfferRequestTO
                                .builder()
                                .fhCatalogOfferId(ConsumerServiceMock.VALID_FH_OFFER_ID)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ConsumerApiMapper consumerApiMapper() {

            return Mappers.getMapper(ConsumerApiMapper.class);
        }

        @Bean
        public EdcClient edcClientMock() {

            return Mockito.mock(EdcClient.class);
        }

        @Bean
        public TechnicalFhCatalogClient technicalFhCatalogClientMock() {

            return Mockito.mock(TechnicalFhCatalogClient.class);
        }
    }


}
