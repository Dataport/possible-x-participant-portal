package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.business.control.CommonPortalService;
import eu.possiblex.participantportal.business.control.CommonPortalServiceFake;
import org.hamcrest.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommonPortalRestApiImpl.class)
@ContextConfiguration(classes = { CommonPortalRestApiTest.TestConfig.class, CommonPortalRestApiImpl.class })
class CommonPortalRestApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommonPortalService commonPortalService;

    @Test
    void shouldReturnMessageOnGetNameMapping() throws Exception {
        reset(commonPortalService);

        // WHEN/THEN
        this.mockMvc.perform(get("/common/participant/name-mapping")).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1))
            .andExpect(jsonPath("$", Matchers.hasEntry(CommonPortalServiceFake.PARTICIPANT_DID, CommonPortalServiceFake.PARTICIPANT_NAME)));
    }

    @Test
    void shouldReturnMessageOnGetVersion() throws Exception {
        this.mockMvc.perform(get("/common/version").contentType(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.version").value("1.0.0"))
            .andExpect(jsonPath("$.date").value("2024-12-31"));
    }


    @TestConfiguration
    static class TestConfig {
        @Bean
        public CommonPortalService commonPortalService() {
            return Mockito.spy(new CommonPortalServiceFake());
        }
    }
}
