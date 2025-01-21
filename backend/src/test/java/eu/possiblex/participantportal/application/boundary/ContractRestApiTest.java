package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.configuration.AppConfigurer;
import eu.possiblex.participantportal.application.control.ConsumerApiMapper;
import eu.possiblex.participantportal.application.control.ContractApiMapper;
import eu.possiblex.participantportal.business.control.*;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContractRestApiImpl.class)
@ContextConfiguration(classes = { ContractRestApiTest.TestConfig.class, ContractRestApiImpl.class, AppConfigurer.class })
class ContractRestApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractApiMapper contractApiMapper;

    @Autowired
    private ConsumerApiMapper consumerApiMapper;

    @Test
    @WithMockUser(username = "admin")
    void shouldReturnMessageOnGetContractAgreements() throws Exception {
        //when
        //then

        this.mockMvc.perform(get("/contract/agreement")).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(ContractServiceFake.FAKE_ID_CONTRACT_AGREEMENT))
            .andExpect(jsonPath("$[0].contractSigningDate").value(ContractServiceFake.getDateAsOffsetDateTime().toString()))
            .andExpect(jsonPath("$[0].providerDetails").exists())
            .andExpect(jsonPath("$[0].consumerDetails").exists())
            .andExpect(jsonPath("$[0].provider").value(false))
            .andExpect(jsonPath("$[0].dataOffering").value(false))
            .andExpect(jsonPath("$[0].assetId").value(ContractServiceFake.FAKE_ID_ASSET))
            .andExpect(jsonPath("$[0].assetDetails.name").value(ContractServiceFake.NAME))
            .andExpect(jsonPath("$[0].assetDetails.description").value(ContractServiceFake.DESCRIPTION))
            .andExpect(jsonPath("$[0].policy['odrl:target']['@id']").value(ContractServiceFake.FAKE_ID_ASSET))
            .andExpect(jsonPath("$[0].policy['odrl:prohibition']").isEmpty())
            .andExpect(jsonPath("$[0].policy['odrl:obligation']").isEmpty())
            .andExpect(jsonPath("$[0].policy['odrl:permission']").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldReturnMessageOnGetContractDetailsByContractAgreementId() throws Exception {
        //when
        //then

        this.mockMvc.perform(get("/contract/details/anyId")).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ContractServiceFake.FAKE_ID_CONTRACT_AGREEMENT))
            .andExpect(jsonPath("$.contractSigningDate").value(ContractServiceFake.getDateAsOffsetDateTime().toString()))
            .andExpect(jsonPath("$.providerDetails").exists())
            .andExpect(jsonPath("$.consumerDetails").exists())
            .andExpect(jsonPath("$.assetId").value(ContractServiceFake.FAKE_ID_ASSET))
            .andExpect(jsonPath("$.catalogOffering['schema:name']").value(ContractServiceFake.NAME))
            .andExpect(jsonPath("$.catalogOffering['schema:description']").value(ContractServiceFake.DESCRIPTION))
            .andExpect(jsonPath("$.offerRetrievalDate").exists())
            .andExpect(jsonPath("$.policy['odrl:target']['@id']").value(ContractServiceFake.FAKE_ID_ASSET))
            .andExpect(jsonPath("$.policy['odrl:prohibition']").isEmpty())
            .andExpect(jsonPath("$.policy['odrl:obligation']").isEmpty())
            .andExpect(jsonPath("$.policy['odrl:permission']").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldReturnMessageOnGetOfferWithTimestampByContractAgreementId() throws Exception {
        //when
        //then

        this.mockMvc.perform(get("/contract/details/anyId/offer")).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.catalogOffering['schema:name']").value(ContractServiceFake.NAME))
            .andExpect(jsonPath("$.catalogOffering['schema:description']").value(ContractServiceFake.DESCRIPTION))
            .andExpect(jsonPath("$.offerRetrievalDate").exists());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ContractService contractService() {

            return Mockito.spy(new ContractServiceFake());
        }

        @Bean
        public ConsumerApiMapper consumerApiMapper() {

            return Mappers.getMapper(ConsumerApiMapper.class);
        }

        @Bean
        public ContractApiMapper contractApiMapper() {

            return Mappers.getMapper(ContractApiMapper.class);
        }
    }
}