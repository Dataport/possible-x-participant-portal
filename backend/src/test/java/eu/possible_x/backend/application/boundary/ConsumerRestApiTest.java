package eu.possible_x.backend.application.boundary;

import eu.possible_x.backend.business.control.ConsumerServiceFake;
import eu.possiblex.participantportal.application.boundary.ConsumerRestApiImpl;
import eu.possiblex.participantportal.application.control.ConsumerApiMapper;
import eu.possiblex.participantportal.application.entity.ConsumeOfferRequestTO;
import eu.possiblex.participantportal.application.entity.SelectOfferRequestTO;
import eu.possiblex.participantportal.business.entity.edc.transfer.TransferProcessState;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConsumerRestApiImpl.class)
@ContextConfiguration(classes = { ConsumerRestApiTest.TestConfig.class })
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


    @Test
    void shouldSelectOffer() throws Exception {
        this.mockMvc.perform(post("/consumer/offer/select")
                .content(RestApiHelper.asJsonString(new SelectOfferRequestTO()))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void shouldAcceptOffer() throws Exception {
        this.mockMvc.perform(post("/consumer/offer/accept")
                .content(RestApiHelper.asJsonString(new ConsumeOfferRequestTO()))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().isOk()).andExpect(jsonPath("$.state").value(TransferProcessState.COMPLETED.name()));
    }

}
