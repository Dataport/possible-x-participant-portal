package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.business.control.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.reset;

@SpringBootTest
@ContextConfiguration(classes = { GeneralModuleTest.TestConfig.class })
@AutoConfigureMockMvc
abstract class GeneralModuleTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected TechnicalFhCatalogClient technicalFhCatalogClient;

    @Autowired
    protected SparqlFhCatalogClient sparqlFhCatalogClient;

    @Autowired
    protected EdcClient edcClient;

    @BeforeEach
    void setUp() {

        reset(technicalFhCatalogClient);
        reset(sparqlFhCatalogClient);
        reset(edcClient);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public TechnicalFhCatalogClient technicalFhCatalogClient() {

            return Mockito.spy(new TechnicalFhCatalogClientFake());
        }

        @Bean
        @Primary
        public SparqlFhCatalogClient sparqlFhCatalogClient() {

            return Mockito.spy(new SparqlFhCatalogClientFake());
        }

        @Bean
        @Primary
        public EdcClient edcClient() {

            return Mockito.spy(new EdcClientFake());
        }
    }
}
