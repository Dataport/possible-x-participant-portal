package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.business.control.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommonPortalRestApiImpl.class)
@ContextConfiguration(classes = { CommonPortalModuleTest.TestConfig.class, CommonPortalServiceImpl.class, FhCatalogClientImpl.class})
@TestPropertySource(properties = {"version.no = thisistheversion", "version.date = 21.03.2022"})
class CommonPortalModuleTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommonPortalService commonPortalService;

    @Autowired
    private FhCatalogClientImpl fhCatalogClient;

    @Autowired
    private TechnicalFhCatalogClient technicalFhCatalogClientMock;

    @Autowired
    private SparqlFhCatalogClient sparqlFhCatalogClientMock;

    @Test
    void getVersionSucceeds() throws Exception {
        this.mockMvc.perform(get("/common/version").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value("thisistheversion"))
                .andExpect(jsonPath("$.date").value("21.03.2022"));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public TechnicalFhCatalogClient technicalFhCatalogClientMock() {

            return Mockito.mock(TechnicalFhCatalogClient.class);
        }

        @Bean
        public SparqlFhCatalogClient sparqlFhCatalogClient() {

            return Mockito.mock(SparqlFhCatalogClient.class);
        }
    }
}