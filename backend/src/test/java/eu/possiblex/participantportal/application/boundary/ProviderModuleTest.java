package eu.possiblex.participantportal.application.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.application.entity.CreateDataOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.CreateServiceOfferingRequestTO;
import eu.possiblex.participantportal.business.control.*;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This is an integration test that tests as much of the backend as possible. Here, all real components are used from
 * all layers. Only the interface components which connect to other systems are mocked.
 */
@SpringBootTest
@ContextConfiguration(classes = { ProviderModuleTest.TestConfig.class })
@AutoConfigureMockMvc
class ProviderModuleTest extends ProviderTestParent {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EdcClient edcClient;

    @Autowired
    private TechnicalFhCatalogClient technicalFhCatalogClient;

    @BeforeEach
    void setUp() {

        reset(technicalFhCatalogClient);
        reset(edcClient);
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldReturnMessageOnCreateServiceOfferingWithoutData() throws Exception {

        CreateServiceOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingTOJsonString(),
            CreateServiceOfferingRequestTO.class);

        this.mockMvc.perform(post("/provider/offer/service").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldReturnMessageOnCreateServiceOfferingWithData() throws Exception {

        CreateDataOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingWithDataTOJsonString(),
            CreateDataOfferingRequestTO.class);

        this.mockMvc.perform(post("/provider/offer/data").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldDeleteOfferInFhCatalogOnEdcErrorWhenCreatingServiceOfferingWithoutData() throws Exception {

        CreateServiceOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingTOJsonString(),
            CreateServiceOfferingRequestTO.class);

        request.getServiceOfferingCredentialSubject().setName(EdcClientFake.BAD_GATEWAY_ASSET_ID);

        this.mockMvc.perform(post("/provider/offer/service").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON));

        verify(technicalFhCatalogClient).deleteServiceOfferingFromFhCatalog(any());

    }

    @Test
    @WithMockUser(username = "admin")
    void shouldDeleteOfferInFhCatalogOnEdcErrorWhenCreatingServiceOfferingWithData() throws Exception {

        CreateDataOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingWithDataTOJsonString(),
            CreateDataOfferingRequestTO.class);

        request.getServiceOfferingCredentialSubject().setName(EdcClientFake.BAD_GATEWAY_ASSET_ID);

        this.mockMvc.perform(post("/provider/offer/data").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON));

        verify(technicalFhCatalogClient).deleteServiceOfferingWithDataFromFhCatalog(any());
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
