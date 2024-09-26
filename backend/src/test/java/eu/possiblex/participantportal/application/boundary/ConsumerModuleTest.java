package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.control.ConsumerApiMapper;
import eu.possiblex.participantportal.application.entity.ConsumeOfferRequestTO;
import eu.possiblex.participantportal.application.entity.SelectOfferRequestTO;
import eu.possiblex.participantportal.business.control.*;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatCatalog;
import eu.possiblex.participantportal.business.entity.edc.catalog.DcatDataset;
import eu.possiblex.participantportal.business.entity.edc.common.IdResponse;
import eu.possiblex.participantportal.business.entity.edc.negotiation.ContractNegotiation;
import eu.possiblex.participantportal.business.entity.edc.negotiation.NegotiationState;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.edc.transfer.IonosS3TransferProcess;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcessState;
import eu.possiblex.participantportal.utilities.ExceptionHandlingFilter;
import eu.possiblex.participantportal.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

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
@WebMvcTest(ConsumerRestApiImpl.class)
@ContextConfiguration(classes = { ConsumerModuleTest.TestConfig.class, ConsumerRestApiImpl.class,
    ConsumerServiceImpl.class, FhCatalogClientImpl.class })
public class ConsumerModuleTest {

    private static String TEST_FILES_PATH = "unit_tests/ConsumerModuleTest/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private FhCatalogClientImpl fhCatalogClient;

    @Autowired
    private EdcClient edcClientMock;

    @Autowired
    private TechnicalFhCatalogClient technicalFhCatalogClientMock;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new ConsumerRestApiImpl(consumerService, Mappers.getMapper(ConsumerApiMapper.class)))
                .addFilters(new ExceptionHandlingFilter())
                .build();
    }
    @Test
    void acceptContractOfferSucceeds() throws Exception {

        // GIVEN

        reset(edcClientMock);
        reset(technicalFhCatalogClientMock);

        String edcOfferId = "edcOfferId";
        String counterPartyAddress = "counterPartyAddress";

        // let the EDC provide the test data catalog
        DcatDataset mockDatasetWrongOne = new DcatDataset(); // an offer in the EDC Catalog which the user does not look for
        mockDatasetWrongOne.setAssetId("assetIdWhichTheUserDoesNotLookFor");
        mockDatasetWrongOne.setName("wrong");
        mockDatasetWrongOne.setContenttype("wrong");
        mockDatasetWrongOne.setDescription("wrong");
        DcatDataset mockDatasetCorrectOne = new DcatDataset(); // the offer in the EDC Catalog which the user looks for
        mockDatasetCorrectOne.setAssetId(edcOfferId);
        mockDatasetCorrectOne.setName("correctName");
        mockDatasetCorrectOne.setContenttype("correctContentType");
        mockDatasetCorrectOne.setDescription("correctDescription");
        Policy policy = new Policy();
        policy.setId("policyId");
        mockDatasetCorrectOne.setHasPolicy(List.of(policy));
        DcatCatalog edcCatalogAnswerMock = new DcatCatalog();
        edcCatalogAnswerMock.setDataset(List.of(mockDatasetWrongOne, mockDatasetCorrectOne));
        Mockito.when(edcClientMock.queryCatalog(Mockito.any())).thenReturn(edcCatalogAnswerMock);

        // define EDC client behaviour for the data transfer so that it goes through
        IdResponse negotiation = new IdResponse();
        negotiation.setId("negiotiationId");
        Mockito.when(edcClientMock.negotiateOffer(Mockito.any())).thenReturn(negotiation);
        ContractNegotiation contractNegotiation = new ContractNegotiation();
        contractNegotiation.setState(NegotiationState.FINALIZED);
        Mockito.when(edcClientMock.checkOfferStatus(Mockito.eq(negotiation.getId()))).thenReturn(contractNegotiation);
        IdResponse transfer = new IdResponse();
        transfer.setId("transferId");
        Mockito.when(edcClientMock.initiateTransfer(Mockito.any())).thenReturn(transfer);
        IonosS3TransferProcess transferProcess = new IonosS3TransferProcess();
        transferProcess.setState(TransferProcessState.COMPLETED);
        Mockito.when(edcClientMock.checkTransferStatus(Mockito.any())).thenReturn(transferProcess);

        // WHEN/THEN

        this.mockMvc.perform(post("/consumer/offer/accept").content(RestApiHelper.asJsonString(
                    ConsumeOfferRequestTO.builder().edcOfferId(edcOfferId).counterPartyAddress(counterPartyAddress).dataOffering(true).build()))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.transferProcessState").value(TransferProcessState.COMPLETED.name()));

        // THEN

    }

    @Test
    void acceptContractOfferSucceedsNoTransfer() throws Exception {

        // GIVEN

        reset(edcClientMock);
        reset(technicalFhCatalogClientMock);

        String edcOfferId = "edcOfferId";
        String counterPartyAddress = "counterPartyAddress";

        // let the EDC provide the test data catalog
        DcatDataset mockDatasetWrongOne = new DcatDataset(); // an offer in the EDC Catalog which the user does not look for
        mockDatasetWrongOne.setAssetId("assetIdWhichTheUserDoesNotLookFor");
        mockDatasetWrongOne.setName("wrong");
        mockDatasetWrongOne.setContenttype("wrong");
        mockDatasetWrongOne.setDescription("wrong");
        DcatDataset mockDatasetCorrectOne = new DcatDataset(); // the offer in the EDC Catalog which the user looks for
        mockDatasetCorrectOne.setAssetId(edcOfferId);
        mockDatasetCorrectOne.setName("correctName");
        mockDatasetCorrectOne.setContenttype("correctContentType");
        mockDatasetCorrectOne.setDescription("correctDescription");
        Policy policy = new Policy();
        policy.setId("policyId");
        mockDatasetCorrectOne.setHasPolicy(List.of(policy));
        DcatCatalog edcCatalogAnswerMock = new DcatCatalog();
        edcCatalogAnswerMock.setDataset(List.of(mockDatasetWrongOne, mockDatasetCorrectOne));
        Mockito.when(edcClientMock.queryCatalog(Mockito.any())).thenReturn(edcCatalogAnswerMock);

        // define EDC client behaviour for the data transfer so that it goes through
        IdResponse negotiation = new IdResponse();
        negotiation.setId("negiotiationId");
        Mockito.when(edcClientMock.negotiateOffer(Mockito.any())).thenReturn(negotiation);
        ContractNegotiation contractNegotiation = new ContractNegotiation();
        contractNegotiation.setState(NegotiationState.FINALIZED);
        Mockito.when(edcClientMock.checkOfferStatus(Mockito.eq(negotiation.getId()))).thenReturn(contractNegotiation);
        IdResponse transfer = new IdResponse();
        transfer.setId("transferId");
        Mockito.when(edcClientMock.initiateTransfer(Mockito.any())).thenReturn(transfer);
        IonosS3TransferProcess transferProcess = new IonosS3TransferProcess();
        transferProcess.setState(TransferProcessState.COMPLETED);
        Mockito.when(edcClientMock.checkTransferStatus(Mockito.any())).thenReturn(transferProcess);

        // WHEN/THEN

        this.mockMvc.perform(post("/consumer/offer/accept").content(RestApiHelper.asJsonString(
                                ConsumeOfferRequestTO.builder().edcOfferId(edcOfferId).counterPartyAddress(counterPartyAddress).dataOffering(false).build()))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.transferProcessState").value(TransferProcessState.INITIAL.name()));

        // THEN

    }

    @Test
    void selectingOfferSucceeds() throws Exception {

        // GIVEN

        reset(edcClientMock);
        reset(technicalFhCatalogClientMock);

        // let the FH catalog provide the test data offer
        String fhCatalogOfferContent = TestUtils.loadTextFile(TEST_FILES_PATH + "validFhOffer.json");
        Mockito.when(technicalFhCatalogClientMock.getFhCatalogOffer(Mockito.eq(ConsumerServiceMock.VALID_FH_OFFER_ID)))
            .thenReturn(fhCatalogOfferContent);

        String expectedEdcProviderUrl = "EXPECTED_PROVIDER_URL_VALUE"; // from the "px:providerURL" attribute in the test data offer
        String expectedAssetId = "EXPECTED_ASSET_ID_VALUE"; // from the "px:assetId" attribute in the test data offer

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

        this.mockMvc.perform(post("/consumer/offer/select").content(RestApiHelper.asJsonString(
                    SelectOfferRequestTO.builder().fhCatalogOfferId(ConsumerServiceMock.VALID_FH_OFFER_ID).build()))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
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
        Mockito.when(technicalFhCatalogClientMock.getFhCatalogOffer(Mockito.eq(ConsumerServiceMock.VALID_FH_OFFER_ID)))
            .thenReturn(fhCatalogOfferContent);

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

        this.mockMvc.perform(post("/consumer/offer/select").content(RestApiHelper.asJsonString(
                    SelectOfferRequestTO.builder().fhCatalogOfferId(ConsumerServiceMock.VALID_FH_OFFER_ID).build()))
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
        Mockito.when(technicalFhCatalogClientMock.getFhCatalogOffer(Mockito.eq(ConsumerServiceMock.VALID_FH_OFFER_ID)))
            .thenThrow(expectedException);

        // WHEN/THEN

        this.mockMvc.perform(post("/consumer/offer/select").content(RestApiHelper.asJsonString(
                    SelectOfferRequestTO.builder().fhCatalogOfferId(ConsumerServiceMock.VALID_FH_OFFER_ID).build()))
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
        Mockito.when(technicalFhCatalogClientMock.getFhCatalogOffer(Mockito.eq(ConsumerServiceMock.VALID_FH_OFFER_ID)))
            .thenReturn(fhCatalogOfferContent);

        // WHEN/THEN

        this.mockMvc.perform(post("/consumer/offer/select").content(RestApiHelper.asJsonString(
                SelectOfferRequestTO.builder().fhCatalogOfferId(ConsumerServiceMock.VALID_FH_OFFER_ID).build()))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is5xxServerError());
    }

    @Test
    void selectOfferForFhOfferWithoutAccessUrlReturnsInternalError() throws Exception {

        // GIVEN

        reset(edcClientMock);
        reset(technicalFhCatalogClientMock);

        // let the FH catalog provide the test data offer which does not contain an asset ID
        String fhCatalogOfferContent = TestUtils.loadTextFile(TEST_FILES_PATH + "invalidFhOfferNoAccessUrl.json");
        Mockito.when(technicalFhCatalogClientMock.getFhCatalogOffer(Mockito.eq(ConsumerServiceMock.VALID_FH_OFFER_ID)))
            .thenReturn(fhCatalogOfferContent);

        // WHEN/THEN

        this.mockMvc.perform(post("/consumer/offer/select").content(RestApiHelper.asJsonString(
                SelectOfferRequestTO.builder().fhCatalogOfferId(ConsumerServiceMock.VALID_FH_OFFER_ID).build()))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is5xxServerError());
    }

    @TestConfiguration
    static class TestConfig {

        @MockBean
        private TaskScheduler taskScheduler;

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
