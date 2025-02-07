package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.configuration.AppConfigurer;
import eu.possiblex.participantportal.application.configuration.BoundaryExceptionHandler;
import eu.possiblex.participantportal.application.control.ConsumerApiMapper;
import eu.possiblex.participantportal.application.entity.ConsumeOfferRequestTO;
import eu.possiblex.participantportal.application.entity.SelectOfferRequestTO;
import eu.possiblex.participantportal.application.entity.TransferOfferRequestTO;
import eu.possiblex.participantportal.business.control.ConsumerService;
import eu.possiblex.participantportal.business.control.ConsumerServiceFake;
import eu.possiblex.participantportal.business.entity.ConsumeOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferRequestBE;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcessState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConsumerRestApiImpl.class)
@ContextConfiguration(classes = { ConsumerRestApiTest.TestConfig.class, ConsumerRestApiImpl.class,
    BoundaryExceptionHandler.class, AppConfigurer.class })
class ConsumerRestApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConsumerService consumerService;

    @BeforeEach
    void setup() {

        reset(consumerService);
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldSelectOfferValid() throws Exception {

        this.mockMvc.perform(post("/consumer/offer/select").content(RestApiHelper.asJsonString(
                    SelectOfferRequestTO.builder().fhCatalogOfferId(ConsumerServiceFake.VALID_FH_OFFER_ID).build()))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk()).andExpect(
                jsonPath("$.catalogOffering['px:providerUrl']").value(ConsumerServiceFake.VALID_COUNTER_PARTY_ADDRESS))
            .andExpect(jsonPath("$.edcOfferId").value(ConsumerServiceFake.VALID_ASSET_ID))
            .andExpect(jsonPath("$.providerDetails").exists())
            .andExpect(jsonPath("$.providerDetails.participantId").value(ConsumerServiceFake.FAKE_DID))
            .andExpect(jsonPath("$.providerDetails.participantEmail").value(ConsumerServiceFake.FAKE_EMAIL_ADDRESS))
            .andExpect(jsonPath("$.offerRetrievalDate").exists());

        ArgumentCaptor<SelectOfferRequestBE> requestCaptor = ArgumentCaptor.forClass(SelectOfferRequestBE.class);

        // check that business logic was called and that parameter from REST was given
        verify(consumerService).selectContractOffer(requestCaptor.capture());
        assertEquals(ConsumerServiceFake.VALID_FH_OFFER_ID, requestCaptor.getValue().getFhCatalogOfferId());
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldSelectOfferMissing() throws Exception {

        this.mockMvc.perform(post("/consumer/offer/select").content(RestApiHelper.asJsonString(
                SelectOfferRequestTO.builder().fhCatalogOfferId(ConsumerServiceFake.MISSING_OFFER_ID).build()))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldAcceptOfferValid() throws Exception {

        ConsumeOfferRequestTO request = getValidConsumeOfferRequestTO();

        this.mockMvc.perform(post("/consumer/offer/accept").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());

        ArgumentCaptor<ConsumeOfferRequestBE> requestCaptor = ArgumentCaptor.forClass(ConsumeOfferRequestBE.class);

        // check that business logic was called and that parameter from REST was given
        verify(consumerService).acceptContractOffer(requestCaptor.capture());
        assertEquals(ConsumerServiceFake.VALID_EDC_OFFER_ID, requestCaptor.getValue().getEdcOfferId());
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldAcceptOfferMissing() throws Exception {

        ConsumeOfferRequestTO request = getValidConsumeOfferRequestTO();
        request.setEdcOfferId(ConsumerServiceFake.MISSING_OFFER_ID);

        this.mockMvc.perform(post("/consumer/offer/accept").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldAcceptOfferBadNegotiation() throws Exception {

        ConsumeOfferRequestTO request = getValidConsumeOfferRequestTO();
        request.setEdcOfferId(ConsumerServiceFake.BAD_EDC_OFFER_ID);

        this.mockMvc.perform(post("/consumer/offer/accept").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldNotTransferOffer() throws Exception {

        TransferOfferRequestTO request = getValidTransferOfferRequestTO();
        request.setEdcOfferId(ConsumerServiceFake.BAD_TRANSFER_OFFER_ID);

        this.mockMvc.perform(post("/consumer/offer/transfer").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldTransferOffer() throws Exception {

        TransferOfferRequestTO request = getValidTransferOfferRequestTO();

        this.mockMvc.perform(post("/consumer/offer/transfer").content(RestApiHelper.asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.transferProcessState").value(TransferProcessState.COMPLETED.toString()));
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldTransferOfferMissing() throws Exception {

        TransferOfferRequestTO request = getValidTransferOfferRequestTO();
        request.setEdcOfferId(ConsumerServiceFake.MISSING_OFFER_ID);

        this.mockMvc.perform(post("/consumer/offer/transfer").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isNotFound());
    }

    private ConsumeOfferRequestTO getValidConsumeOfferRequestTO() {

        return ConsumeOfferRequestTO.builder().counterPartyAddress("counterPartyAddress")
            .edcOfferId(ConsumerServiceFake.VALID_EDC_OFFER_ID).dataOffering(true).build();
    }

    private TransferOfferRequestTO getValidTransferOfferRequestTO() {

        return TransferOfferRequestTO.builder().contractAgreementId("contractAgreementId")
            .counterPartyAddress("counterPartyAddress").edcOfferId(ConsumerServiceFake.VALID_EDC_OFFER_ID).build();
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ConsumerService consumerService() {

            return Mockito.spy(new ConsumerServiceFake());
        }

        @Bean
        public ConsumerApiMapper consumerApiMapper() {

            return Mappers.getMapper(ConsumerApiMapper.class);
        }
    }

}
