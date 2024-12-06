package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.control.ConsumerApiMapper;
import eu.possiblex.participantportal.application.control.ParticipantApiMapper;
import eu.possiblex.participantportal.business.control.ParticipantService;
import eu.possiblex.participantportal.business.control.ParticipantServiceFake;
import eu.possiblex.participantportal.utilities.ExceptionHandlingFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParticipantRestApiImpl.class)
@ContextConfiguration(classes = { ParticipantRestApiTest.TestConfig.class, ParticipantRestApiImpl.class })
class ParticipantRestApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ParticipantService participantService;

    @BeforeEach
    void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new ParticipantRestApiImpl(participantService, Mappers.getMapper(ParticipantApiMapper.class)))
            .addFilters(new ExceptionHandlingFilter()).build();
    }

    @Test
    void shouldReturnMessageOnGetParticipantId() throws Exception {
        // WHEN/THEN
        this.mockMvc.perform(get("/participant/id/me")).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.participantId").value(ParticipantServiceFake.PARTICIPANT_ID));
    }

    @Test
    void shouldReturnMessageOnGetParticipantDetails() throws Exception {
        // WHEN/THEN
        this.mockMvc.perform(get("/participant/details/me")).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.participantName").value(ParticipantServiceFake.PARTICIPANT_NAME))
            .andExpect(jsonPath("$.participantEmail").value(ParticipantServiceFake.PARTICIPANT_EMAIL));
    }

    @Test
    void shouldReturnMessageOnGetParticipantDetailsById() throws Exception {
        // WHEN/THEN
        this.mockMvc.perform(get("/participant/details/{participantId}", ParticipantServiceFake.OTHER_PARTICIPANT_ID))
            .andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.participantName").value(ParticipantServiceFake.OTHER_PARTICIPANT_NAME))
            .andExpect(jsonPath("$.participantEmail").value(ParticipantServiceFake.OTHER_PARTICIPANT_EMAIL));
    }

    @Test
    void shouldReturnMessageOnGetContractPartiesNotFound() throws Exception {
        // WHEN/THEN
        this.mockMvc.perform(get("/participant/details/{participantId}", ParticipantServiceFake.UNKNOWN_PARTICIPANT_ID))
            .andDo(print()).andExpect(status().isNotFound());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ParticipantService participantService() {

            return Mockito.spy(new ParticipantServiceFake());
        }

        @Bean
        public ParticipantApiMapper participantApiMapper() {

            return Mappers.getMapper(ParticipantApiMapper.class);
        }
    }
}