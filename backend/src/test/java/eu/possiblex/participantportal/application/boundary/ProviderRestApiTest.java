package eu.possiblex.participantportal.application.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.possiblex.participantportal.application.control.ProviderApiMapper;
import eu.possiblex.participantportal.application.entity.CreateOfferRequestTO;
import eu.possiblex.participantportal.business.control.ProviderService;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.edc.common.IdResponse;
import eu.possiblex.participantportal.business.entity.fh.CreateFhOfferBE;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProviderRestApiImpl.class)
@ContextConfiguration(classes = { ProviderRestApiTest.TestConfig.class, ProviderRestApiImpl.class })
class ProviderRestApiTest {

    private static final String ASSET_NAME = "BestAsset3000";
    private static final String EDC_RESPONSE_ID = "abc123";
    private static final String FH_RESPONSE_ID = "abc123";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProviderService providerService;

    public static String asJsonString(final Object obj) {

        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void beforeEach() {

        ObjectNode node =  JsonNodeFactory.instance.objectNode();
        node.put("EDC-ID", EDC_RESPONSE_ID);
        node.put("FH-ID", FH_RESPONSE_ID);

        lenient().when(providerService.createOffer(any(), any())).thenReturn(node);

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
            .andExpect(jsonPath("$.EDC-ID").value(EDC_RESPONSE_ID))
                .andExpect(jsonPath("$.FH-ID").value(FH_RESPONSE_ID));

        ArgumentCaptor<CreateFhOfferBE> createDatasetEntryCaptor = ArgumentCaptor.forClass(
            CreateFhOfferBE.class);
        ArgumentCaptor<CreateEdcOfferBE> createEdcOfferCaptor = ArgumentCaptor.forClass(CreateEdcOfferBE.class);

        verify(providerService).createOffer(createDatasetEntryCaptor.capture(), createEdcOfferCaptor.capture());

        CreateFhOfferBE createDatasetEntry = createDatasetEntryCaptor.getValue();
        CreateEdcOfferBE createEdcOfferBE = createEdcOfferCaptor.getValue();
        //check if request is mapped correctly
        assertEquals(request.getPolicy(), createDatasetEntry.getPolicy());
        assertEquals(request.getFileName(), createEdcOfferBE.getFileName());
        assertEquals(request.getPolicy(), createEdcOfferBE.getPolicy());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ProviderApiMapper providerApiMapper() {

            return Mappers.getMapper(ProviderApiMapper.class);
        }
    }
}
