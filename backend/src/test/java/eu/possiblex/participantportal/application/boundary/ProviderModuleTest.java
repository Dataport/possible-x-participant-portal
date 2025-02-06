package eu.possiblex.participantportal.application.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.application.entity.CreateDataOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.CreateServiceOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxDataAccountExport;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxSOTermsAndConditions;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxDataResourceCredentialSubject;
import eu.possiblex.participantportal.application.entity.credentials.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.control.EdcClientFake;
import eu.possiblex.participantportal.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This is an integration test that tests as much of the backend as possible. Here, all real components are used from
 * all layers. Only the interface components which connect to other systems are mocked.
 */

class ProviderModuleTest extends GeneralModuleTest {

    private static final String TEST_FILES_PATH = "unit_tests/ModuleTestsCommon/";

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin")
    void shouldReturnMessageOnCreateServiceOfferingWithoutData() throws Exception {

        CreateServiceOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingTOJsonString(),
            CreateServiceOfferingRequestTO.class);

        this.mockMvc.perform(post("/provider/offer/service").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldReturnMessageOnCreateServiceOfferingWithData() throws Exception {

        CreateDataOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingWithDataTOJsonString(),
            CreateDataOfferingRequestTO.class);

        this.mockMvc.perform(post("/provider/offer/data").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldDeleteOfferInFhCatalogOnEdcErrorWhenCreatingServiceOfferingWithoutData() throws Exception {

        CreateServiceOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingTOJsonString(),
            CreateServiceOfferingRequestTO.class);

        request.getServiceOfferingCredentialSubject().setName(EdcClientFake.BAD_GATEWAY_ASSET_ID);

        this.mockMvc.perform(post("/provider/offer/service").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON));

        verify(technicalFhCatalogClient).deleteServiceOfferingFromFhCatalog(any());

    }

    @Test
    @WithMockUser(username = "admin")
    void shouldDeleteOfferInFhCatalogOnEdcErrorWhenCreatingServiceOfferingWithData() throws Exception {

        CreateDataOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingWithDataTOJsonString(),
            CreateDataOfferingRequestTO.class);

        request.getServiceOfferingCredentialSubject().setName(EdcClientFake.BAD_GATEWAY_ASSET_ID);

        this.mockMvc.perform(post("/provider/offer/data").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON));

        verify(technicalFhCatalogClient).deleteServiceOfferingWithDataFromFhCatalog(any());
    }

    private GxServiceOfferingCredentialSubject getGxServiceOfferingCredentialSubject() {

        String policy = TestUtils.loadTextFile(TEST_FILES_PATH + "standard_policy.json");

        return GxServiceOfferingCredentialSubject.builder()
            .providedBy(new NodeKindIRITypeId("did:web:example-organization.eu")).name("Test Service Offering")
            .description("This is the service offering description.").policy(List.of(policy)).dataAccountExport(List.of(
                GxDataAccountExport.builder().formatType("application/json").accessType("digital").requestType("API")
                    .build()))
            .termsAndConditions(List.of(GxSOTermsAndConditions.builder().url("test.eu/tnc").hash("hash123").build()))
            .id("urn:uuid:GENERATED_SERVICE_OFFERING_ID").build();
    }

    private GxDataResourceCredentialSubject getGxDataResourceCredentialSubject() {

        String policy = TestUtils.loadTextFile(TEST_FILES_PATH + "standard_policy.json");

        return GxDataResourceCredentialSubject.builder().policy(List.of(policy)).name("Test Dataset")
            .description("This is the data resource description.").license(List.of("AGPL-1.0-only")).containsPII(true)
            .copyrightOwnedBy(List.of("did:web:example-organization.eu"))
            .producedBy(new NodeKindIRITypeId("did:web:example-organization.eu"))
            .exposedThrough(new NodeKindIRITypeId("urn:uuid:GENERATED_SERVICE_OFFERING_ID"))
            .id("urn:uuid:GENERATED_DATA_RESOURCE_ID").build();
    }

    private String getCreateServiceOfferingTOJsonString() {

        return TestUtils.loadTextFile(TEST_FILES_PATH + "serviceOfferingPayload.json");
    }

    private String getCreateServiceOfferingWithDataTOJsonString() {

        return TestUtils.loadTextFile(TEST_FILES_PATH + "serviceOfferingPayloadWithData.json");
    }

}
