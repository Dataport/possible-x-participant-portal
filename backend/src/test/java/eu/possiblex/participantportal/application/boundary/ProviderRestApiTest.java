package eu.possiblex.participantportal.application.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.possiblex.participantportal.application.control.ProviderApiMapper;
import eu.possiblex.participantportal.application.entity.CreateOfferRequestTO;
import eu.possiblex.participantportal.business.control.ProviderService;
import eu.possiblex.participantportal.business.control.ProviderServiceFake;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.fh.CreateDatasetEntryBE;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProviderRestApiImpl.class)
@ContextConfiguration(classes = { ProviderRestApiTest.TestConfig.class, ProviderRestApiImpl.class })
class ProviderRestApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProviderService providerService;

    public static String asJsonString(final Object obj) {

        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnMessageOnCreateOffer() throws Exception {
        //given
        ObjectNode policy = JsonNodeFactory.instance.objectNode();
        policy.put("policy", "");

        CreateOfferRequestTO request = CreateOfferRequestTO.builder().offerDescription("description").offerName("name")
            .offerType("type").fileName("fileName").policy(policy).build();

        //when
        //then
        this.mockMvc.perform(post("/provider/offer").content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ProviderServiceFake.CREATE_OFFER_RESPONSE_ID));

        ArgumentCaptor<CreateDatasetEntryBE> createDatasetEntryCaptor = ArgumentCaptor.forClass(
            CreateDatasetEntryBE.class);
        ArgumentCaptor<CreateEdcOfferBE> createEdcOfferCaptor = ArgumentCaptor.forClass(CreateEdcOfferBE.class);

        verify(providerService).createOffer(createDatasetEntryCaptor.capture(), createEdcOfferCaptor.capture());

        CreateDatasetEntryBE createDatasetEntry = createDatasetEntryCaptor.getValue();
        CreateEdcOfferBE createEdcOfferBE = createEdcOfferCaptor.getValue();
        //check if request is mapped correctly
        assertEquals(request.getPolicy(), createDatasetEntry.getPolicy());
        assertEquals(request.getFileName(), createEdcOfferBE.getFileName());
        assertEquals(request.getPolicy(), createEdcOfferBE.getPolicy());
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
