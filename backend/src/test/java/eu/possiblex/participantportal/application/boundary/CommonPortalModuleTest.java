package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.business.control.SparqlFhCatalogClient;
import eu.possiblex.participantportal.business.control.TechnicalFhCatalogClient;
import eu.possiblex.participantportal.utils.TestUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = { CommonPortalModuleTest.TestConfig.class })
@AutoConfigureMockMvc
class CommonPortalModuleTest {

    private static final String TEST_FILES_PATH = "unit_tests/ConsumerModuleTest/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SparqlFhCatalogClient sparqlFhCatalogClient;

    @Test
    @WithMockUser(username = "admin")
    void getVersionSucceeds() throws Exception {
        // WHEN/THEN
        this.mockMvc.perform(get("/common/version").contentType(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().isOk()).andExpect(jsonPath("$.version").value("1.0.0"))
            .andExpect(jsonPath("$.date").value("2024-12-31"));
    }

    @Test
    @WithMockUser(username = "admin")
    void getNameMappingSucceeds() throws Exception {

        reset(sparqlFhCatalogClient);

        // GIVEN
        String sparqlQueryResultString = TestUtils.loadTextFile(TEST_FILES_PATH + "validSparqlResultParticipant.json");
        when(sparqlFhCatalogClient.queryCatalog(any(), any())).thenReturn(sparqlQueryResultString);

        // WHEN/THEN
        this.mockMvc.perform(get("/common/participant/name-mapping")).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1)).andExpect(jsonPath("$",
                Matchers.hasEntry("did:web:portal.dev.possible-x.de:participant:df15587a-0760-32b5-9c42-bb7be66e8076",
                    "EXPECTED_NAME_VALUE")));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public TechnicalFhCatalogClient technicalFhCatalogClient() {

            return Mockito.mock(TechnicalFhCatalogClient.class);
        }

        @Bean
        @Primary
        public SparqlFhCatalogClient sparqlFhCatalogClient() {

            return Mockito.mock(SparqlFhCatalogClient.class);
        }
    }
}