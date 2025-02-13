package eu.possiblex.participantportal.application.boundary;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ContractModuleTest extends GeneralModuleTest {

    @Test
    @WithMockUser(username = "admin")
    void getContractAgreementsSuccess() throws Exception {

        this.mockMvc.perform(get("/contract/agreement")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    void getContractDetailsByContractAgreementIdSuccess() throws Exception {

        this.mockMvc.perform(get("/contract/details/1")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    void getOfferWithTimestampByContractAgreementIdSuccess() throws Exception {

        this.mockMvc.perform(get("/contract/details/1/offer")).andDo(print()).andExpect(status().isOk());
    }
}
