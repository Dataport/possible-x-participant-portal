package eu.possiblex.participantportal.controller;

import eu.possiblex.participantportal.application.boundary.RestApiHelper;
import eu.possiblex.participantportal.application.boundary.ProviderRestApi;
import eu.possiblex.participantportal.application.entity.CreateOfferRequestTO;
import eu.possiblex.participantportal.business.entity.edc.common.IdResponse;
import eu.possiblex.participantportal.business.control.ProviderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProviderRestApi.class)
class ProviderControllerTest {

    private static final String ASSET_NAME = "BestAsset3000";

    private static final String CREATE_OFFER_RESPONSE_ID = "abc123";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProviderService providerService;

    @BeforeEach
    public void beforeEach() {

        IdResponse createOfferResponse = new IdResponse();
        createOfferResponse.setId(CREATE_OFFER_RESPONSE_ID);
        lenient().when(providerService.createOffer()).thenReturn(createOfferResponse);

    }

    @Test
    void shouldReturnMessageOnCreateOffer() throws Exception {
        //when
        //then
        this.mockMvc.perform(post("/provider/offer").content(RestApiHelper.asJsonString(new CreateOfferRequestTO()))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(CREATE_OFFER_RESPONSE_ID));
    }
}
