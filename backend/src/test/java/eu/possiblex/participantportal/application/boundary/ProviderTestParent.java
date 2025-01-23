package eu.possiblex.participantportal.application.boundary;

import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxDataAccountExport;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxSOTermsAndConditions;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxDataResourceCredentialSubject;
import eu.possiblex.participantportal.application.entity.credentials.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.utils.TestUtils;

import java.util.List;

class ProviderTestParent {
    private static String TEST_FILES_PATH = "unit_tests/ModuleTestsCommon/";

    GxServiceOfferingCredentialSubject getGxServiceOfferingCredentialSubject() {
        String policy = TestUtils.loadTextFile(TEST_FILES_PATH + "standard_policy.json");

        return GxServiceOfferingCredentialSubject.builder()
                .providedBy(new NodeKindIRITypeId("did:web:example-organization.eu")).name("Test Service Offering")
                .description("This is the service offering description.").policy(List.of(policy)).dataAccountExport(List.of(
                        GxDataAccountExport.builder().formatType("application/json").accessType("digital").requestType("API")
                                .build()))
                .termsAndConditions(List.of(GxSOTermsAndConditions.builder().url("test.eu/tnc").hash("hash123").build()))
                .id("urn:uuid:GENERATED_SERVICE_OFFERING_ID").build();
    }

    GxDataResourceCredentialSubject getGxDataResourceCredentialSubject() {
        String policy = TestUtils.loadTextFile(TEST_FILES_PATH + "standard_policy.json");

        return GxDataResourceCredentialSubject.builder().policy(List.of(policy)).name("Test Dataset").description("This is the data resource description.")
                .license(List.of("AGPL-1.0-only")).containsPII(true)
                .copyrightOwnedBy(List.of(new NodeKindIRITypeId("did:web:example-organization.eu")))
                .producedBy(new NodeKindIRITypeId("did:web:example-organization.eu"))
                .exposedThrough(new NodeKindIRITypeId("urn:uuid:GENERATED_SERVICE_OFFERING_ID"))
                .id("urn:uuid:GENERATED_DATA_RESOURCE_ID").build();
    }

    String getCreateServiceOfferingTOJsonString() {
        return TestUtils.loadTextFile(TEST_FILES_PATH + "serviceOfferingPayload.json");
    }

    String getCreateServiceOfferingWithDataTOJsonString() {
        return TestUtils.loadTextFile(TEST_FILES_PATH + "serviceOfferingPayloadWithData.json");
    }

    String getCreateDataOfferingTOJsonString() {
        return TestUtils.loadTextFile(TEST_FILES_PATH + "dataOfferingPayload.json");
    }


}
