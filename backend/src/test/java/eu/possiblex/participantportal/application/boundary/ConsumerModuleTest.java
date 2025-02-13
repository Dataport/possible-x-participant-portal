package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.entity.ConsumeOfferRequestTO;
import eu.possiblex.participantportal.application.entity.SelectOfferRequestTO;
import eu.possiblex.participantportal.business.control.EdcClientFake;
import eu.possiblex.participantportal.business.control.TechnicalFhCatalogClientFake;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This is an integration test that tests as much of the backend as possible. Here, all real components are used from
 * all layers. Only the interface components which connect to other systems are mocked.
 */
class ConsumerModuleTest extends GeneralModuleTest {

    @Test
    @WithMockUser(username = "admin")
    void acceptContractOfferSucceeds() throws Exception {

        this.mockMvc.perform(post("/consumer/offer/accept").content(RestApiHelper.asJsonString(
                ConsumeOfferRequestTO.builder().edcOfferId(EdcClientFake.FAKE_ID).counterPartyAddress("counterPartyAddress")
                    .dataOffering(true).build())).contentType(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    void selectingOfferSucceeds() throws Exception {

        this.mockMvc.perform(post("/consumer/offer/select").content(RestApiHelper.asJsonString(
                SelectOfferRequestTO.builder().fhCatalogOfferId(TechnicalFhCatalogClientFake.VALID_FH_DATA_OFFER_ID)
                    .build())).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.catalogOffering['px:providerUrl']").value("EXPECTED_PROVIDER_URL_VALUE"))
            .andExpect(jsonPath("$.edcOfferId").value(EdcClientFake.FAKE_ID))
            .andExpect(jsonPath("$.dataOffering").value(true)).andExpect(jsonPath("$.providerDetails").exists())
            .andExpect(jsonPath("$.providerDetails.participantId").value(
                "did:web:portal.dev.possible-x.de:participant:df15587a-0760-32b5-9c42-bb7be66e8076"))
            .andExpect(jsonPath("$.providerDetails.participantName").value("EXPECTED_NAME_VALUE"))
            .andExpect(jsonPath("$.providerDetails.participantEmail").value("EXPECTED_MAIL_ADDRESS_VALUE"))
            .andExpect(jsonPath("$.offerRetrievalDate").exists());

        // FH Catalog should have been queried with the offer ID given in the request
        verify(technicalFhCatalogClient, Mockito.times(1)).getFhCatalogOfferWithData(
            TechnicalFhCatalogClientFake.VALID_FH_DATA_OFFER_ID);
    }

    @Test
    @WithMockUser(username = "admin")
    void selectingOfferWithoutDataSucceeds() throws Exception {

        this.mockMvc.perform(post("/consumer/offer/select").content(RestApiHelper.asJsonString(
                SelectOfferRequestTO.builder().fhCatalogOfferId(TechnicalFhCatalogClientFake.VALID_FH_SERVICE_OFFER_ID)
                    .build())).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.catalogOffering['px:providerUrl']").value("EXPECTED_PROVIDER_URL_VALUE"))
            .andExpect(jsonPath("$.edcOfferId").value(EdcClientFake.FAKE_ID))
            .andExpect(jsonPath("$.dataOffering").value(false)).andExpect(jsonPath("$.providerDetails").exists())
            .andExpect(jsonPath("$.providerDetails.participantId").value(
                "did:web:portal.dev.possible-x.de:participant:df15587a-0760-32b5-9c42-bb7be66e8076"))
            .andExpect(jsonPath("$.providerDetails.participantName").value("EXPECTED_NAME_VALUE"))
            .andExpect(jsonPath("$.providerDetails.participantEmail").value("EXPECTED_MAIL_ADDRESS_VALUE"))
            .andExpect(jsonPath("$.offerRetrievalDate").exists());

        // FH Catalog should have been queried with the offer ID given in the request
        verify(technicalFhCatalogClient, Mockito.times(1)).getFhCatalogOfferWithData(
            TechnicalFhCatalogClientFake.VALID_FH_SERVICE_OFFER_ID);
        verify(technicalFhCatalogClient, Mockito.times(1)).getFhCatalogOffer(
            TechnicalFhCatalogClientFake.VALID_FH_SERVICE_OFFER_ID);
    }

}
