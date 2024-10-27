package eu.possiblex.participantportal.application.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.application.control.ProviderApiMapper;
import eu.possiblex.participantportal.application.entity.CreateDataOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.CreateServiceOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxDataAccountExport;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxSOTermsAndConditions;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxDataResourceCredentialSubject;
import eu.possiblex.participantportal.application.entity.credentials.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import eu.possiblex.participantportal.application.entity.policies.EverythingAllowedPolicy;
import eu.possiblex.participantportal.business.control.ProviderService;
import eu.possiblex.participantportal.business.control.ProviderServiceFake;
import eu.possiblex.participantportal.business.entity.CreateDataOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProviderRestApiImpl.class)
@ContextConfiguration(classes = { ProviderRestApiTest.TestConfig.class, ProviderRestApiImpl.class })
class ProviderRestApiTest extends ProviderTestParent {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnMessageOnCreateServiceOffering() throws Exception {

        // GIVEN

        reset(providerService);

        CreateServiceOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingTOJsonString(),
            CreateServiceOfferingRequestTO.class);

        GxServiceOfferingCredentialSubject expectedServiceOfferingCS = getGxServiceOfferingCredentialSubject();

        // WHEN/THEN

        this.mockMvc.perform(post("/provider/offer/service").content(RestApiHelper.asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.edcResponseId").value(ProviderServiceFake.CREATE_OFFER_RESPONSE_ID))
            .andExpect(jsonPath("$.fhResponseId").value(ProviderServiceFake.CREATE_OFFER_RESPONSE_ID));

        ArgumentCaptor<CreateServiceOfferingRequestBE> createServiceOfferingCaptor = ArgumentCaptor.forClass(
            CreateServiceOfferingRequestBE.class);

        verify(providerService).createOffering(createServiceOfferingCaptor.capture());

        CreateServiceOfferingRequestBE createServiceOfferingBE = createServiceOfferingCaptor.getValue();

        List<EnforcementPolicy> serviceOfferingPolicy = createServiceOfferingBE.getEnforcementPolicies();

        //check if request is mapped correctly
        assertThat(List.of(new EverythingAllowedPolicy())).usingRecursiveComparison().isEqualTo(serviceOfferingPolicy);
        assertThat(expectedServiceOfferingCS.getProvidedBy()).usingRecursiveComparison()
            .isEqualTo(createServiceOfferingBE.getProvidedBy());
        assertThat(expectedServiceOfferingCS.getDataAccountExport()).usingRecursiveComparison()
            .isEqualTo(createServiceOfferingBE.getDataAccountExport());
        assertThat(expectedServiceOfferingCS.getDataProtectionRegime()).usingRecursiveComparison()
            .isEqualTo(createServiceOfferingBE.getDataProtectionRegime());
        assertThat(expectedServiceOfferingCS.getTermsAndConditions()).usingRecursiveComparison()
            .isEqualTo(createServiceOfferingBE.getTermsAndConditions());
        assertEquals(expectedServiceOfferingCS.getName(), createServiceOfferingBE.getName());
        assertEquals(expectedServiceOfferingCS.getDescription(), createServiceOfferingBE.getDescription());
    }

    @Test
    void shouldReturnMessageOnCreateDataOffering() throws Exception {

        // GIVEN

        reset(providerService);

        CreateDataOfferingRequestTO request = objectMapper.readValue(getCreateDataOfferingTOJsonString(),
            CreateDataOfferingRequestTO.class);

        GxServiceOfferingCredentialSubject expectedServiceOfferingCS = getGxServiceOfferingCredentialSubject();
        GxDataResourceCredentialSubject expectedDataResourceCS = getGxDataResourceCredentialSubject();

        // WHEN/THEN

        this.mockMvc.perform(post("/provider/offer/data").content(RestApiHelper.asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.edcResponseId").value(ProviderServiceFake.CREATE_OFFER_RESPONSE_ID))
            .andExpect(jsonPath("$.fhResponseId").value(ProviderServiceFake.CREATE_OFFER_RESPONSE_ID));

        ArgumentCaptor<CreateDataOfferingRequestBE> createDataOfferingRequestBEArgumentCaptor = ArgumentCaptor.forClass(
            CreateDataOfferingRequestBE.class);

        verify(providerService).createOffering(createDataOfferingRequestBEArgumentCaptor.capture());

        CreateDataOfferingRequestBE createDataOfferingRequestBE = createDataOfferingRequestBEArgumentCaptor.getValue();
        List<EnforcementPolicy> serviceOfferingPolicy = createDataOfferingRequestBE.getEnforcementPolicies();

        //check if request is mapped correctly
        assertThat(List.of(new EverythingAllowedPolicy())).usingRecursiveComparison().isEqualTo(serviceOfferingPolicy);
        assertThat(expectedServiceOfferingCS.getProvidedBy()).usingRecursiveComparison()
            .isEqualTo(createDataOfferingRequestBE.getProvidedBy());
        assertThat(expectedServiceOfferingCS.getDataAccountExport()).usingRecursiveComparison()
            .isEqualTo(createDataOfferingRequestBE.getDataAccountExport());
        assertThat(expectedServiceOfferingCS.getDataProtectionRegime()).usingRecursiveComparison()
            .isEqualTo(createDataOfferingRequestBE.getDataProtectionRegime());
        assertThat(expectedServiceOfferingCS.getTermsAndConditions()).usingRecursiveComparison()
            .isEqualTo(createDataOfferingRequestBE.getTermsAndConditions());
        assertEquals(expectedServiceOfferingCS.getName(), createDataOfferingRequestBE.getName());
        assertEquals(expectedServiceOfferingCS.getDescription(), createDataOfferingRequestBE.getDescription());
        assertEquals(request.getFileName(), createDataOfferingRequestBE.getFileName());

        assertThat(expectedDataResourceCS).usingRecursiveComparison()
            .isEqualTo(createDataOfferingRequestBE.getDataResource());
    }

    @Test
    void shouldReturnMessageOnGetParticipantId() throws Exception {
        // WHEN/THEN
        this.mockMvc.perform(get("/provider/id")).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.participantId").value(ProviderServiceFake.PARTICIPANT_ID));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ProviderService providerService() {

            return Mockito.spy(new ProviderServiceFake());
        }

        @Bean
        public ProviderApiMapper providerApiMapper() {

            return Mappers.getMapper(ProviderApiMapper.class);
        }

        @Bean
        public ObjectMapper objectMapper() {

            return new ObjectMapper();
            // Customize the ObjectMapper if needed
        }
    }
}
