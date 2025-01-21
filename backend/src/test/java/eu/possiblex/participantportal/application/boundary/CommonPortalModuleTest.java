package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.business.control.*;
import eu.possiblex.participantportal.utils.TestUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommonPortalRestApiImpl.class)
@ContextConfiguration(classes = { CommonPortalModuleTest.TestConfig.class, CommonPortalRestApiImpl.class,
    CommonPortalServiceImpl.class, FhCatalogClientImpl.class })
@TestPropertySource(properties = { "version.no = thisistheversion", "version.date = 21.03.2022" })
class CommonPortalModuleTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommonPortalService commonPortalService;

    @Autowired
    private FhCatalogClientImpl fhCatalogClient;

    @Autowired
    private TechnicalFhCatalogClient technicalFhCatalogClient;

    @Autowired
    private SparqlFhCatalogClient sparqlFhCatalogClient;

    private static final String TEST_FILES_PATH = "unit_tests/ConsumerModuleTest/";

    @Test
    @WithMockUser(username = "admin")
    void getVersionSucceeds() throws Exception {
        // WHEN/THEN
        this.mockMvc.perform(get("/common/version").contentType(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().isOk()).andExpect(jsonPath("$.version").value("thisistheversion"))
            .andExpect(jsonPath("$.date").value("21.03.2022"));
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
                Matchers.hasEntry("did:web:portal.dev.possible-x.de:participant:df15587a-0760-32b5-9c42-bb7be66e8076", "EXPECTED_NAME_VALUE")));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public TechnicalFhCatalogClient technicalFhCatalogClient() {

            return Mockito.mock(TechnicalFhCatalogClient.class);
        }

        @Bean
        public SparqlFhCatalogClient sparqlFhCatalogClient() {

            return Mockito.mock(SparqlFhCatalogClient.class);
        }
    }
}