package eu.possiblex.participantportal.application.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.application.control.ProviderApiMapper;
import eu.possiblex.participantportal.application.entity.CreateDataOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.CreateServiceOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxDataAccountExport;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxSOTermsAndConditions;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxDataResourceCredentialSubject;
import eu.possiblex.participantportal.application.entity.credentials.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.control.ProviderService;
import eu.possiblex.participantportal.business.control.ProviderServiceFake;
import eu.possiblex.participantportal.business.entity.CreateDataOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.utils.TestUtils;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProviderTestParent {
    private static String TEST_FILES_PATH = "unit_tests/ModuleTestsCommon/";

    protected GxServiceOfferingCredentialSubject getGxServiceOfferingCredentialSubject() {
        String policy = TestUtils.loadTextFile(TEST_FILES_PATH + "standard_policy.json");

        return GxServiceOfferingCredentialSubject.builder()
            .providedBy(new NodeKindIRITypeId("did:web:example-organization.eu")).name("Test Service Offering")
            .description("This is the service offering description.").policy(List.of(policy)).dataAccountExport(List.of(
                GxDataAccountExport.builder().formatType("application/json").accessType("digital").requestType("API")
                    .build()))
            .termsAndConditions(List.of(GxSOTermsAndConditions.builder().url("test.eu/tnc").hash("hash123").build()))
            .id("urn:uuid:GENERATED_SERVICE_OFFERING_ID").build();
    }

    protected GxDataResourceCredentialSubject getGxDataResourceCredentialSubject() {
        String policy = TestUtils.loadTextFile(TEST_FILES_PATH + "standard_policy.json");

        return GxDataResourceCredentialSubject.builder().policy(List.of(policy)).name("Test Dataset").description("This is the data resource description.")
            .license(List.of("AGPL-1.0-only")).containsPII(true)
            .copyrightOwnedBy(new NodeKindIRITypeId("did:web:example-organization.eu"))
            .producedBy(new NodeKindIRITypeId("did:web:example-organization.eu"))
            .exposedThrough(new NodeKindIRITypeId("urn:uuid:GENERATED_SERVICE_OFFERING_ID"))
            .id("urn:uuid:GENERATED_DATA_RESOURCE_ID").build();
    }

    protected String getCreateServiceOfferingTOJsonString() {
        return TestUtils.loadTextFile(TEST_FILES_PATH + "serviceOfferingPayload.json");
    }

    protected String getCreateDataOfferingTOJsonString() {
        return TestUtils.loadTextFile(TEST_FILES_PATH + "dataOfferingPayload.json");
    }

}
