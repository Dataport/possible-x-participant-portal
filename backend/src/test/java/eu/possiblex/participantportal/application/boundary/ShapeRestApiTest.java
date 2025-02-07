package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.configuration.AppConfigurer;
import eu.possiblex.participantportal.application.configuration.BoundaryExceptionHandler;
import eu.possiblex.participantportal.business.control.SdCreationWizardApiService;
import eu.possiblex.participantportal.business.control.SdCreationWizardApiServiceFake;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShapeRestApiImpl.class)
@ContextConfiguration(classes = { ShapeRestApiTest.TestConfig.class, ShapeRestApiImpl.class, AppConfigurer.class,
    BoundaryExceptionHandler.class })
class ShapeRestApiTest {
    @Autowired
    SdCreationWizardApiService sdCreationWizardApiService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin")
    void getGxServiceOfferingShape() throws Exception {

        this.mockMvc.perform(get("/shapes/gx/serviceoffering").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.someKey").value("someValue"));
    }

    @Test
    @WithMockUser(username = "admin")
    void getGxDataResourceShape() throws Exception {

        this.mockMvc.perform(get("/shapes/gx/resource/dataresource").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.someKey").value("someValue"));
    }

    @Test
    @WithMockUser(username = "admin")
    void getGxVirtualResourceShape() throws Exception {

        this.mockMvc.perform(get("/shapes/gx/resource/virtualresource").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.someKey").value("someValue"));
    }

    @Test
    @WithMockUser(username = "admin")
    void getGxPhysicalResourceShape() throws Exception {

        this.mockMvc.perform(get("/shapes/gx/resource/physicalresource").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.someKey").value("someValue"));
    }

    @Test
    @WithMockUser(username = "admin")
    void getGxInstantiatedVirtualResourceShape() throws Exception {

        this.mockMvc.perform(
                get("/shapes/gx/resource/instantiatedvirtualresource").contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.someKey").value("someValue"));
    }

    @Test
    @WithMockUser(username = "admin")
    void getGxSoftwareResourceShape() throws Exception {

        this.mockMvc.perform(get("/shapes/gx/resource/softwareresource").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.someKey").value("someValue"));
    }

    @Test
    @WithMockUser(username = "admin")
    void getGxLegitimateInterestShape() throws Exception {

        this.mockMvc.perform(get("/shapes/gx/resource/legitimateinterest").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.someKey").value("someValue"));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public SdCreationWizardApiService sdCreationWizardApiService() {

            return Mockito.spy(new SdCreationWizardApiServiceFake());
        }
    }
}