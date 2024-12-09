package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.control.ConsumerApiMapper;
import eu.possiblex.participantportal.application.entity.ConsumeOfferRequestTO;
import eu.possiblex.participantportal.application.entity.SelectOfferRequestTO;
import eu.possiblex.participantportal.application.entity.TransferOfferRequestTO;
import eu.possiblex.participantportal.business.control.ConsumerService;
import eu.possiblex.participantportal.business.control.ConsumerServiceFake;
import eu.possiblex.participantportal.business.entity.ConsumeOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferRequestBE;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcessState;
import eu.possiblex.participantportal.utilities.ExceptionHandlingFilter;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConsumerRestApiImpl.class)
@ContextConfiguration(classes = { ConsumerRestApiTest.TestConfig.class, ConsumerRestApiImpl.class })
class ConsumerRestApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConsumerService consumerService;

    @BeforeEach
    void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new ConsumerRestApiImpl(consumerService, Mappers.getMapper(ConsumerApiMapper.class)))
            .addFilters(new ExceptionHandlingFilter()).build();
    }

    @Test
    void shouldSelectOfferValid() throws Exception {

        reset(consumerService);
        this.mockMvc.perform(post("/consumer/offer/select").content(RestApiHelper.asJsonString(
                    SelectOfferRequestTO.builder().fhCatalogOfferId(ConsumerServiceFake.VALID_FH_OFFER_ID).build()))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk()).andExpect(
                jsonPath("$.catalogOffering['px:providerUrl']").value(ConsumerServiceFake.VALID_COUNTER_PARTY_ADDRESS))
            .andExpect(jsonPath("$.edcOfferId").value(ConsumerServiceFake.VALID_ASSET_ID))
            .andExpect(jsonPath("$.providerDetails").exists())
            .andExpect(jsonPath("$.providerDetails.participantId").value(ConsumerServiceFake.FAKE_DID))
            .andExpect(jsonPath("$.providerDetails.participantEmail").value(ConsumerServiceFake.FAKE_EMAIL_ADDRESS))
            .andExpect(jsonPath("$.participantNames.size()").value(1));

        ArgumentCaptor<SelectOfferRequestBE> requestCaptor = ArgumentCaptor.forClass(SelectOfferRequestBE.class);

        // check that business logic was called and that parameter from REST was given
        verify(consumerService).selectContractOffer(requestCaptor.capture());
        assertEquals(ConsumerServiceFake.VALID_FH_OFFER_ID, requestCaptor.getValue().getFhCatalogOfferId());
    }

    @Test
    void shouldSelectOfferMissing() throws Exception {

        this.mockMvc.perform(post("/consumer/offer/select").content(RestApiHelper.asJsonString(
                SelectOfferRequestTO.builder().fhCatalogOfferId(ConsumerServiceFake.MISSING_OFFER_ID).build()))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void shouldAcceptOfferValid() throws Exception {

        reset(consumerService);
        this.mockMvc.perform(post("/consumer/offer/accept").content(RestApiHelper.asJsonString(
                ConsumeOfferRequestTO.builder().edcOfferId(ConsumerServiceFake.VALID_EDC_OFFER_ID).build()))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());

        ArgumentCaptor<ConsumeOfferRequestBE> requestCaptor = ArgumentCaptor.forClass(ConsumeOfferRequestBE.class);

        // check that business logic was called and that parameter from REST was given
        verify(consumerService).acceptContractOffer(requestCaptor.capture());
        assertEquals(ConsumerServiceFake.VALID_EDC_OFFER_ID, requestCaptor.getValue().getEdcOfferId());
    }

    @Test
    void shouldAcceptOfferMissing() throws Exception {

        this.mockMvc.perform(post("/consumer/offer/accept").content(RestApiHelper.asJsonString(
                ConsumeOfferRequestTO.builder().edcOfferId(ConsumerServiceFake.MISSING_OFFER_ID).build()))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void shouldAcceptOfferBadNegotiation() throws Exception {

        this.mockMvc.perform(post("/consumer/offer/accept").content(RestApiHelper.asJsonString(
                ConsumeOfferRequestTO.builder().edcOfferId(ConsumerServiceFake.BAD_EDC_OFFER_ID).build()))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isInternalServerError());
    }

    @Test
    void shouldNotTransferOffer() throws Exception {

        this.mockMvc.perform(post("/consumer/offer/transfer").content(RestApiHelper.asJsonString(
                TransferOfferRequestTO.builder().edcOfferId(ConsumerServiceFake.BAD_TRANSFER_OFFER_ID).build()))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isInternalServerError());
    }

    @Test
    void shouldTransferOffer() throws Exception {

        this.mockMvc.perform(post("/consumer/offer/transfer").content(RestApiHelper.asJsonString(
                    TransferOfferRequestTO.builder().edcOfferId(ConsumerServiceFake.VALID_EDC_OFFER_ID).build()))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.transferProcessState").value(TransferProcessState.COMPLETED.toString()));
    }

    @Test
    void shouldTransferOfferMissing() throws Exception {

        this.mockMvc.perform(post("/consumer/offer/transfer").content(RestApiHelper.asJsonString(
                TransferOfferRequestTO.builder().edcOfferId(ConsumerServiceFake.MISSING_OFFER_ID).build()))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isNotFound());
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
