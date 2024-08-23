package eu.possible_x.backend.application.boundary;

import eu.possiblex.participantportal.business.control.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ConsumerRestApi.class)
public class ConsumerRestApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProviderService providerService;


}
