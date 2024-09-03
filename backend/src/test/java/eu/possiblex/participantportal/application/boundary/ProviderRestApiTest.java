package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.control.ProviderApiMapper;
import eu.possiblex.participantportal.application.entity.CreateOfferRequestTO;
import eu.possiblex.participantportal.business.control.ProviderService;
import eu.possiblex.participantportal.business.control.ProviderServiceFake;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.fh.CreateFhOfferBE;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProviderRestApiImpl.class)
@ContextConfiguration(classes = { ProviderRestApiTest.TestConfig.class, ProviderRestApiImpl.class })
class ProviderRestApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProviderService providerService;

    @Test
    void shouldReturnMessageOnCreateOffer() throws Exception {
        //given

        CreateOfferRequestTO request = CreateOfferRequestTO.builder().offerDescription("description").offerName("name")
            .offerType("type").fileName("fileName").policy(new Policy()).build();

        //when
        //then
        this.mockMvc.perform(post("/provider/offer").content(RestApiHelper.asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.edcResponseId").value(ProviderServiceFake.CREATE_OFFER_RESPONSE_ID))
            .andExpect(jsonPath("$.fhResponseId").value(ProviderServiceFake.CREATE_OFFER_RESPONSE_ID));

        ArgumentCaptor<CreateFhOfferBE> createDatasetEntryCaptor = ArgumentCaptor.forClass(CreateFhOfferBE.class);
        ArgumentCaptor<CreateEdcOfferBE> createEdcOfferCaptor = ArgumentCaptor.forClass(CreateEdcOfferBE.class);

        verify(providerService).createOffer(createDatasetEntryCaptor.capture(), createEdcOfferCaptor.capture());

        CreateFhOfferBE createFhOfferBE = createDatasetEntryCaptor.getValue();
        CreateEdcOfferBE createEdcOfferBE = createEdcOfferCaptor.getValue();
        //check if request is mapped correctly
        assertThat(request.getPolicy()).usingRecursiveComparison().isEqualTo(createFhOfferBE.getPolicy());
        assertThat(request.getPolicy()).usingRecursiveComparison().isEqualTo(createEdcOfferBE.getPolicy());
        assertEquals(request.getFileName(), createEdcOfferBE.getFileName());
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
    }
}
