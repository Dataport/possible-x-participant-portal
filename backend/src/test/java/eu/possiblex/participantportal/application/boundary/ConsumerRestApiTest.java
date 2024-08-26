package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.business.control.ConsumerService;
import eu.possiblex.participantportal.business.control.ConsumerServiceFake;
import eu.possiblex.participantportal.application.control.ConsumerApiMapper;
import eu.possiblex.participantportal.application.entity.ConsumeOfferRequestTO;
import eu.possiblex.participantportal.application.entity.SelectOfferRequestTO;
import eu.possiblex.participantportal.business.entity.ConsumeOfferRequestBE;
import eu.possiblex.participantportal.business.entity.SelectOfferRequestBE;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcessState;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConsumerRestApiImpl.class)
@ContextConfiguration(classes = { ConsumerRestApiTest.TestConfig.class, ConsumerRestApiImpl.class })
public class ConsumerRestApiTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ConsumerServiceFake consumerService() {

            return Mockito.spy(new ConsumerServiceFake());
        }

        @Bean
        public ConsumerApiMapper consumerApiMapper() {

            return Mappers.getMapper(ConsumerApiMapper.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConsumerService consumerService;


    @Test
    void shouldSelectOfferValid() throws Exception {
        reset(consumerService);
        this.mockMvc.perform(post("/consumer/offer/select")
                .content(RestApiHelper.asJsonString(SelectOfferRequestTO
                    .builder()
                        .counterPartyAddress("http://example.com")
                        .offerId(ConsumerServiceFake.VALID_OFFER_ID)
                    .build()))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().isOk()).andExpect(jsonPath("$.offerId").value(ConsumerServiceFake.VALID_OFFER_ID));

        ArgumentCaptor<SelectOfferRequestBE> requestCaptor = ArgumentCaptor.forClass(SelectOfferRequestBE.class);

        verify(consumerService).selectContractOffer(requestCaptor.capture());

        assertEquals(ConsumerServiceFake.VALID_OFFER_ID, requestCaptor.getValue().getOfferId());
    }

    @Test
    void shouldSelectOfferMissing() throws Exception {
        this.mockMvc.perform(post("/consumer/offer/select")
                .content(RestApiHelper.asJsonString(SelectOfferRequestTO
                    .builder()
                    .counterPartyAddress("http://example.com")
                    .offerId(ConsumerServiceFake.MISSING_OFFER_ID)
                    .build()))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldAcceptOfferValid() throws Exception {
        reset(consumerService);
        this.mockMvc.perform(post("/consumer/offer/accept")
                .content(RestApiHelper.asJsonString(ConsumeOfferRequestTO
                    .builder()
                        .counterPartyAddress("http://example.com")
                        .offerId(ConsumerServiceFake.VALID_OFFER_ID)
                    .build()))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().isOk()).andExpect(jsonPath("$.state").value(TransferProcessState.COMPLETED.name()));

        ArgumentCaptor<ConsumeOfferRequestBE> requestCaptor = ArgumentCaptor.forClass(ConsumeOfferRequestBE.class);

        verify(consumerService).acceptContractOffer(requestCaptor.capture());

        assertEquals(ConsumerServiceFake.VALID_OFFER_ID, requestCaptor.getValue().getOfferId());
    }

    @Test
    void shouldAcceptOfferMissing() throws Exception {
        this.mockMvc.perform(post("/consumer/offer/accept")
                .content(RestApiHelper.asJsonString(ConsumeOfferRequestTO
                    .builder()
                    .counterPartyAddress("http://example.com")
                    .offerId(ConsumerServiceFake.MISSING_OFFER_ID)
                    .build()))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldAcceptOfferBadNegotiation() throws Exception {
        this.mockMvc.perform(post("/consumer/offer/accept")
                .content(RestApiHelper.asJsonString(ConsumeOfferRequestTO
                    .builder()
                    .counterPartyAddress("http://example.com")
                    .offerId(ConsumerServiceFake.BAD_NEGOTIATION_OFFER_ID)
                    .build()))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldAcceptOfferBadTransfer() throws Exception {
        this.mockMvc.perform(post("/consumer/offer/accept")
                .content(RestApiHelper.asJsonString(ConsumeOfferRequestTO
                    .builder()
                    .counterPartyAddress("http://example.com")
                    .offerId(ConsumerServiceFake.BAD_TRANSFER_OFFER_ID)
                    .build()))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().isInternalServerError());
    }

}
