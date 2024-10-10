package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.control.ContractApiMapper;
import eu.possiblex.participantportal.business.control.ContractService;
import eu.possiblex.participantportal.business.control.ContractServiceMock;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContractRestApiImpl.class)
@ContextConfiguration(classes = { ContractRestApiTest.TestConfig.class, ContractRestApiImpl.class })
class ContractRestApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContractService contractService;

    @Test
    void shouldReturnMessageOnGetContractAgreements() throws Exception {
        //when
        //then
        this.mockMvc.perform(get("/contract/agreement")).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(ContractServiceMock.FAKE_ID_CONTRACT_AGREEMENT)).andExpect(
                jsonPath("$[0].contractSigningDate").value(ContractServiceMock.getDateAsOffsetDateTime().toString()))
            .andExpect(jsonPath("$[0].consumerId").value(ContractServiceMock.FAKE_ID_CONSUMER))
            .andExpect(jsonPath("$[0].providerId").value(ContractServiceMock.FAKE_ID_PROVIDER))
            .andExpect(jsonPath("$[0].assetId").value(ContractServiceMock.FAKE_ID_ASSET))
            .andExpect(jsonPath("$[0].assetDetails.name").value(ContractServiceMock.NAME))
            .andExpect(jsonPath("$[0].assetDetails.description").value(ContractServiceMock.DESCRIPTION))
            .andExpect(jsonPath("$[0].policy['odrl:target']['@id']").value(ContractServiceMock.FAKE_ID_ASSET))
            .andExpect(jsonPath("$[0].policy['odrl:prohibition']").isEmpty())
            .andExpect(jsonPath("$[0].policy['odrl:obligation']").isEmpty())
            .andExpect(jsonPath("$[0].policy['odrl:permission']").isEmpty());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ContractService consumerService() {

            return Mockito.spy(new ContractServiceMock());
        }

        @Bean
        public ContractApiMapper consumerApiMapper() {

            return Mappers.getMapper(ContractApiMapper.class);
        }
    }
}