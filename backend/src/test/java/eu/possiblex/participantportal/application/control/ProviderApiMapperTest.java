package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.CreateDataOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.CreateServiceOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.PrefillFieldsTO;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxDataAccountExport;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxSOTermsAndConditions;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxDataResourceCredentialSubject;
import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxLegitimateInterestCredentialSubject;
import eu.possiblex.participantportal.application.entity.credentials.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.application.entity.policies.*;
import eu.possiblex.participantportal.business.entity.CreateDataOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.DataServiceOfferingPrefillFieldsBE;
import eu.possiblex.participantportal.business.entity.PrefillFieldsBE;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@SpringBootTest
@ContextConfiguration(classes = { ProviderApiMapperTest.TestConfig.class })
class ProviderApiMapperTest {

    @Autowired
    private ProviderApiMapper providerApiMapper;

    @Test
    void mapGetCreateServiceOfferingRequestBE() {

        // GIVEN
        ParticipantRestrictionPolicy participantRestrictionPolicy = ParticipantRestrictionPolicy.builder()
            .allowedParticipants(List.of("did:web:123", "did:web:456")).build();
        StartAgreementOffsetPolicy startAgreementOffsetPolicy = StartAgreementOffsetPolicy.builder().offsetNumber(5)
            .offsetUnit(AgreementOffsetUnit.DAYS).build();
        EndAgreementOffsetPolicy endAgreementOffsetPolicy = EndAgreementOffsetPolicy.builder().offsetNumber(10)
            .offsetUnit(AgreementOffsetUnit.DAYS).build();
        StartDatePolicy startDatePolicy = StartDatePolicy.builder()
            .date(OffsetDateTime.parse("2025-01-01T10:00:00+00:00")).build();
        EndDatePolicy endDatePolicy = EndDatePolicy.builder().date(OffsetDateTime.parse("2125-01-01T10:00:00+00:00"))
            .build();

        List<EnforcementPolicy> policies = List.of(participantRestrictionPolicy, startAgreementOffsetPolicy,
            endAgreementOffsetPolicy, startDatePolicy, endDatePolicy);

        GxServiceOfferingCredentialSubject serviceOfferingCs = GxServiceOfferingCredentialSubject.builder()
            .providedBy(new NodeKindIRITypeId("did:web:example-organization.eu")).name("Test Service Offering")
            .description("This is the service offering description.").policy(List.of("dummyServiceOfferingPolicy"))
            .dataAccountExport(List.of(
                GxDataAccountExport.builder().formatType("application/json").accessType("digital").requestType("API")
                    .build()))
            .termsAndConditions(List.of(GxSOTermsAndConditions.builder().url("test.eu/tnc").hash("hash123").build()))
            .id("urn:uuid:GENERATED_SERVICE_OFFERING_ID").build();

        CreateServiceOfferingRequestTO to = CreateServiceOfferingRequestTO.builder().enforcementPolicies(policies)
            .serviceOfferingCredentialSubject(serviceOfferingCs).build();

        // WHEN
        CreateServiceOfferingRequestBE be = providerApiMapper.getCreateOfferingRequestBE(to);

        // THEN
        assertIterableEquals(to.getEnforcementPolicies(), be.getEnforcementPolicies());

        assertEquals(to.getServiceOfferingCredentialSubject().getProvidedBy().getId(), be.getProvidedBy().getId());
        assertEquals(to.getServiceOfferingCredentialSubject().getName(), be.getName());
        assertEquals(to.getServiceOfferingCredentialSubject().getDescription(), be.getDescription());
        assertIterableEquals(to.getServiceOfferingCredentialSubject().getPolicy(), be.getPolicy());
        assertIterableEquals(to.getServiceOfferingCredentialSubject().getTermsAndConditions(),
            be.getTermsAndConditions());
        assertIterableEquals(to.getServiceOfferingCredentialSubject().getDataProtectionRegime(),
            be.getDataProtectionRegime());
        assertIterableEquals(to.getServiceOfferingCredentialSubject().getDataAccountExport(),
            be.getDataAccountExport());

    }

    @Test
    void mapGetDataCreateOfferingRequestBE() {

        // GIVEN

        GxServiceOfferingCredentialSubject serviceOfferingCs = GxServiceOfferingCredentialSubject.builder()
            .providedBy(new NodeKindIRITypeId("did:web:example-organization.eu")).name("Test Service Offering")
            .description("This is the service offering description.").policy(List.of("dummyServiceOfferingPolicy"))
            .dataAccountExport(List.of(
                GxDataAccountExport.builder().formatType("application/json").accessType("digital").requestType("API")
                    .build()))
            .termsAndConditions(List.of(GxSOTermsAndConditions.builder().url("test.eu/tnc").hash("hash123").build()))
            .id("urn:uuid:GENERATED_SERVICE_OFFERING_ID").build();

        GxDataResourceCredentialSubject dataResourceCs = GxDataResourceCredentialSubject.builder()
            .policy(List.of("dummyDataResourcePolicy")).name("Test Dataset")
            .description("This is the data resource description.").license(List.of("AGPL-1.0-only")).containsPII(true)
            .copyrightOwnedBy(List.of("did:web:example-organization.eu"))
            .producedBy(new NodeKindIRITypeId("did:web:example-organization.eu"))
            .exposedThrough(new NodeKindIRITypeId("urn:uuid:GENERATED_SERVICE_OFFERING_ID"))
            .id("urn:uuid:GENERATED_DATA_RESOURCE_ID").build();

        GxLegitimateInterestCredentialSubject legitimateInterestCs = GxLegitimateInterestCredentialSubject.builder().dataProtectionContact("Test Contact")
            .legalBasis("Test Legal Basis").build();

        CreateDataOfferingRequestTO to = CreateDataOfferingRequestTO.builder().legitimateInterestCredentialSubject(legitimateInterestCs)
            .serviceOfferingCredentialSubject(serviceOfferingCs).fileName("text.txt").enforcementPolicies(List.of())
            .dataResourceCredentialSubject(dataResourceCs).build();

        // WHEN
        CreateDataOfferingRequestBE be = providerApiMapper.getCreateOfferingRequestBE(to);

        to.setEnforcementPolicies(null); // was empty list before
        CreateDataOfferingRequestBE beAdapted = providerApiMapper.getCreateOfferingRequestBE(to);

        // THEN
        assertIterableEquals(List.of(new EverythingAllowedPolicy()), be.getEnforcementPolicies());

        assertEquals(to.getServiceOfferingCredentialSubject().getProvidedBy().getId(), be.getProvidedBy().getId());
        assertEquals(to.getServiceOfferingCredentialSubject().getName(), be.getName());
        assertEquals(to.getServiceOfferingCredentialSubject().getDescription(), be.getDescription());
        assertIterableEquals(to.getServiceOfferingCredentialSubject().getPolicy(), be.getPolicy());
        assertIterableEquals(to.getServiceOfferingCredentialSubject().getTermsAndConditions(),
            be.getTermsAndConditions());
        assertIterableEquals(to.getServiceOfferingCredentialSubject().getDataProtectionRegime(),
            be.getDataProtectionRegime());
        assertIterableEquals(to.getServiceOfferingCredentialSubject().getDataAccountExport(),
            be.getDataAccountExport());

        assertEquals(to.getFileName(), be.getFileName());

        assertEquals(to.getDataResourceCredentialSubject().getName(), be.getDataResource().getName());
        assertEquals(to.getDataResourceCredentialSubject().getDescription(), be.getDataResource().getDescription());
        assertIterableEquals(to.getDataResourceCredentialSubject().getPolicy(), be.getDataResource().getPolicy());
        assertIterableEquals(to.getDataResourceCredentialSubject().getLicense(), be.getDataResource().getLicense());
        assertIterableEquals(to.getDataResourceCredentialSubject().getCopyrightOwnedBy(),
            be.getDataResource().getCopyrightOwnedBy());
        assertEquals(to.getDataResourceCredentialSubject().getProducedBy().getId(),
            be.getDataResource().getProducedBy().getId());
        assertEquals(to.getDataResourceCredentialSubject().getExposedThrough().getId(),
            be.getDataResource().getExposedThrough().getId());
        assertEquals(to.getDataResourceCredentialSubject().isContainsPII(), be.getDataResource().isContainsPII());
        assertEquals(to.getDataResourceCredentialSubject().getId(), be.getDataResource().getId());

        assertEquals(to.getLegitimateInterestCredentialSubject().getDataProtectionContact(),
            be.getLegitimateInterestCredentialSubject().getDataProtectionContact());
        assertEquals(to.getLegitimateInterestCredentialSubject().getLegalBasis(), be.getLegitimateInterestCredentialSubject().getLegalBasis());

        assertIterableEquals(List.of(new EverythingAllowedPolicy()), beAdapted.getEnforcementPolicies());

    }

    @Test
    void mapGetPrefillFieldsTO() {

        // GIVEN
        PrefillFieldsBE be = PrefillFieldsBE.builder().dataServiceOfferingPrefillFields(
            DataServiceOfferingPrefillFieldsBE.builder().serviceOfferingDescription("Test Description")
                .serviceOfferingName("Test Name").build()).participantId("did:web:example-organization.eu").build();

        // WHEN
        PrefillFieldsTO to = providerApiMapper.getPrefillFieldsTO(be);

        // THEN
        assertEquals(be.getDataServiceOfferingPrefillFields().getServiceOfferingDescription(),
            to.getDataServiceOfferingPrefillFields().getServiceOfferingDescription());
        assertEquals(be.getDataServiceOfferingPrefillFields().getServiceOfferingName(),
            to.getDataServiceOfferingPrefillFields().getServiceOfferingName());
        assertEquals(be.getParticipantId(), to.getParticipantId());

    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ProviderApiMapper providerApiMapper() {

            return Mappers.getMapper(ProviderApiMapper.class);
        }

    }
}
