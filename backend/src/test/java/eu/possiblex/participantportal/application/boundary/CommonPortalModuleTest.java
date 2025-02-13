package eu.possiblex.participantportal.application.boundary;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommonPortalModuleTest extends GeneralModuleTest {

    @Test
    @WithMockUser(username = "admin")
    void getVersionSucceeds() throws Exception {

        this.mockMvc.perform(get("/common/version").contentType(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().isOk()).andExpect(jsonPath("$.version").value("1.0.0"))
            .andExpect(jsonPath("$.date").value("2024-12-31"));
    }

    @Test
    @WithMockUser(username = "admin")
    void getNameMappingSucceeds() throws Exception {

        this.mockMvc.perform(get("/common/participant/name-mapping")).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1)).andExpect(jsonPath("$",
                Matchers.hasEntry("did:web:portal.dev.possible-x.de:participant:df15587a-0760-32b5-9c42-bb7be66e8076",
                    "EXPECTED_NAME_VALUE")));
    }

}