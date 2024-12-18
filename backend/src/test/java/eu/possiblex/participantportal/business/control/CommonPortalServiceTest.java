package eu.possiblex.participantportal.business.control;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;

@SpringBootTest
@ContextConfiguration(classes = {CommonPortalServiceTest.TestConfig.class, CommonPortalServiceImpl.class})
class CommonPortalServiceTest {
    @Autowired
    private CommonPortalService commonPortalService;

    @Autowired
    private FhCatalogClient fhCatalogClient;

    @Test
    void getNameMapping() {
        reset(fhCatalogClient);

        // WHEN

        Map<String, String> response = commonPortalService.getNameMapping();

        // THEN

        Mockito.verify(fhCatalogClient).getParticipantDetails();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertNotNull(response.get(FhCatalogClientFake.FAKE_DID));
        assertEquals(OmejdnConnectorApiClientFake.PARTICIPANT_NAME, response.get(FhCatalogClientFake.FAKE_DID));
    }

    // Test-specific configuration to provide mocks
    @TestConfiguration
    static class TestConfig {
        @Bean
        public FhCatalogClient fhCatalogClient() {
            return Mockito.spy(new FhCatalogClientFake());
        }
    }

}