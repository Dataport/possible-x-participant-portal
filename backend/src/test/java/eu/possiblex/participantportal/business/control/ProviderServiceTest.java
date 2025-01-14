package eu.possiblex.participantportal.business.control;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxDataAccountExport;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxSOTermsAndConditions;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxDataResourceCredentialSubject;
import eu.possiblex.participantportal.application.entity.credentials.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.application.entity.policies.*;
import eu.possiblex.participantportal.business.entity.CreateDataOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.PrefillFieldsBE;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedDataResourceCredentialSubject;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.edc.asset.AssetCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.asset.ionoss3extension.IonosS3DataSource;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAssetProperties;
import eu.possiblex.participantportal.business.entity.edc.contractdefinition.ContractDefinitionCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.policy.*;
import eu.possiblex.participantportal.utilities.PossibleXException;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = { ProviderServiceTest.TestConfig.class, ProviderServiceImpl.class })
class ProviderServiceTest {
    private static final String FILE_NAME = "file.txt";

    @Autowired
    ProviderService providerService;

    @Autowired
    EdcClient edcClient;

    @Autowired
    FhCatalogClient fhCatalogClient;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testCreateServiceOffering() {

        reset(fhCatalogClient);
        reset(edcClient);

        //given

        GxServiceOfferingCredentialSubject offeringCs = getGxServiceOfferingCredentialSubject();

        CreateServiceOfferingRequestBE be = CreateServiceOfferingRequestBE.builder()
            .enforcementPolicies(List.of(new EverythingAllowedPolicy())).providedBy(offeringCs.getProvidedBy())
            .name(offeringCs.getName()).description(offeringCs.getDescription())
            .termsAndConditions(offeringCs.getTermsAndConditions()).dataAccountExport(offeringCs.getDataAccountExport())
            .policy(offeringCs.getPolicy()).dataProtectionRegime(offeringCs.getDataProtectionRegime()).build();

        //when
        var response = providerService.createOffering(be);

        //then
        ArgumentCaptor<AssetCreateRequest> assetCreateRequestCaptor = forClass(AssetCreateRequest.class);
        ArgumentCaptor<PolicyCreateRequest> policyCreateRequestCaptor = forClass(PolicyCreateRequest.class);
        ArgumentCaptor<ContractDefinitionCreateRequest> contractDefinitionCreateRequestCaptor = forClass(
            ContractDefinitionCreateRequest.class);

        ArgumentCaptor<PxExtendedServiceOfferingCredentialSubject> serviceOfferingCaptor = forClass(
            PxExtendedServiceOfferingCredentialSubject.class);

        verify(fhCatalogClient).addServiceOfferingToFhCatalog(serviceOfferingCaptor.capture(), Mockito.anyBoolean());

        PxExtendedServiceOfferingCredentialSubject pxExtSoCs = serviceOfferingCaptor.getValue();
        assertNotNull(pxExtSoCs);
        assertTrue(pxExtSoCs.getId()
            .matches("urn:uuid:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));
        String expectedProvidedBy = "did:web:test.eu";
        assertEquals(expectedProvidedBy, pxExtSoCs.getProvidedBy().getId());
        //check if assetId exists and provider url is set correctly
        assertTrue(pxExtSoCs.getAssetId()
            .matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));
        assertEquals("test", pxExtSoCs.getProviderUrl());
        assertThat(pxExtSoCs.getPolicy()).hasSize(2).contains("dummyServiceOfferingPolicy");

        verify(edcClient).createAsset(assetCreateRequestCaptor.capture());
        verify(edcClient, times(2)).createPolicy(policyCreateRequestCaptor.capture());
        verify(edcClient).createContractDefinition(contractDefinitionCreateRequestCaptor.capture());

        AssetCreateRequest assetCreateRequest = assetCreateRequestCaptor.getValue();
        //validate asset properties
        PossibleAssetProperties properties = (PossibleAssetProperties) assetCreateRequest.getProperties();
        assertEquals(offeringCs.getName(), properties.getName());
        assertEquals(offeringCs.getDescription(), properties.getDescription());
        assertEquals(expectedProvidedBy, properties.getProvidedBy().getId());
        assertThat(offeringCs.getTermsAndConditions()).usingRecursiveComparison()
            .isEqualTo(properties.getTermsAndConditions());
        assertThat(offeringCs.getDataProtectionRegime()).containsExactlyInAnyOrderElementsOf(
            properties.getDataProtectionRegime());
        assertThat(offeringCs.getDataAccountExport()).usingRecursiveComparison()
            .isEqualTo(properties.getDataAccountExport());
        assertThat(properties.getOfferingPolicy()).hasSize(2).contains("dummyServiceOfferingPolicy");
        assertThat(properties.getDataPolicy()).isNull();
        //check if file name is set correctly
        assertEquals("", assetCreateRequest.getDataAddress().getKeyName());
        assertEquals("", ((IonosS3DataSource) assetCreateRequest.getDataAddress()).getBlobName());

        ContractDefinitionCreateRequest contractDefinitionCreateRequest = contractDefinitionCreateRequestCaptor.getValue();

        List<PolicyCreateRequest> policyCreateRequests = policyCreateRequestCaptor.getAllValues();
        boolean foundAccessPolicy = false;
        for (var policy : policyCreateRequests) {
            //check if policyId is set correctly
            assertTrue(
                policy.getId().matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));

            if (contractDefinitionCreateRequest.getAccessPolicyId().equals(policy.getId())) {
                foundAccessPolicy = true;
                // for access policy make sure there are no constraints
                for (OdrlPermission permission : policy.getPolicy().getPermission()) {
                    assertTrue(permission.getConstraint().isEmpty());
                }
            }
        }
        assertTrue(foundAccessPolicy);

        assertNotNull(response);
        assertNotNull(response.getEdcResponseId());
        assertNotNull(response.getFhResponseId());
    }

    @Test
    void testCreateDataOffering() {

        reset(fhCatalogClient);
        reset(edcClient);

        //given
        GxServiceOfferingCredentialSubject offeringCs = getGxServiceOfferingCredentialSubject();
        GxDataResourceCredentialSubject resourceCs = getGxDataResourceCredentialSubject();

        CreateDataOfferingRequestBE be = CreateDataOfferingRequestBE.builder().fileName(FILE_NAME)
            .enforcementPolicies(List.of(new EverythingAllowedPolicy())).providedBy(offeringCs.getProvidedBy())
            .name(offeringCs.getName()).description(offeringCs.getDescription())
            .termsAndConditions(offeringCs.getTermsAndConditions()).dataAccountExport(offeringCs.getDataAccountExport())
            .dataProtectionRegime(offeringCs.getDataProtectionRegime()).policy(offeringCs.getPolicy())
            .dataResource(resourceCs).build();

        //when
        var response = providerService.createOffering(be);

        //then
        ArgumentCaptor<AssetCreateRequest> assetCreateRequestCaptor = forClass(AssetCreateRequest.class);
        ArgumentCaptor<PolicyCreateRequest> policyCreateRequestCaptor = forClass(PolicyCreateRequest.class);
        ArgumentCaptor<ContractDefinitionCreateRequest> contractDefinitionCreateRequestCaptor = forClass(
            ContractDefinitionCreateRequest.class);

        ArgumentCaptor<PxExtendedServiceOfferingCredentialSubject> serviceOfferingCaptor = forClass(
            PxExtendedServiceOfferingCredentialSubject.class);

        verify(fhCatalogClient).addServiceOfferingToFhCatalog(serviceOfferingCaptor.capture(), Mockito.anyBoolean());

        PxExtendedServiceOfferingCredentialSubject pxExtSoCs = serviceOfferingCaptor.getValue();
        assertNotNull(pxExtSoCs);
        String serviceOfferingId = pxExtSoCs.getId();
        PxExtendedDataResourceCredentialSubject dataResource = pxExtSoCs.getAggregationOf().get(0);
        assertTrue(serviceOfferingId.matches(
            "urn:uuid:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));
        assertTrue(dataResource.getId()
            .matches("urn:uuid:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));
        assertEquals(serviceOfferingId, dataResource.getExposedThrough().getId());
        String expectedProvidedBy = "did:web:test.eu";
        assertEquals(expectedProvidedBy, pxExtSoCs.getProvidedBy().getId());
        //check if assetId exists and provider url is set correctly
        assertTrue(pxExtSoCs.getAssetId()
            .matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));
        assertEquals("test", pxExtSoCs.getProviderUrl());
        assertThat(pxExtSoCs.getPolicy()).hasSize(2).contains("dummyServiceOfferingPolicy");
        assertThat(pxExtSoCs.getAggregationOf()).hasSize(1);
        assertThat(pxExtSoCs.getAggregationOf().get(0).getPolicy()).contains("dummyDataResourcePolicy");

        verify(edcClient).createAsset(assetCreateRequestCaptor.capture());
        verify(edcClient, times(2)).createPolicy(policyCreateRequestCaptor.capture());
        verify(edcClient).createContractDefinition(contractDefinitionCreateRequestCaptor.capture());

        AssetCreateRequest assetCreateRequest = assetCreateRequestCaptor.getValue();
        //validate asset properties
        PossibleAssetProperties properties = (PossibleAssetProperties) assetCreateRequest.getProperties();
        assertEquals(offeringCs.getName(), properties.getName());
        assertEquals(offeringCs.getDescription(), properties.getDescription());
        assertEquals(expectedProvidedBy, properties.getProvidedBy().getId());
        assertThat(offeringCs.getTermsAndConditions()).usingRecursiveComparison()
            .isEqualTo(properties.getTermsAndConditions());
        assertThat(offeringCs.getDataProtectionRegime()).containsExactlyInAnyOrderElementsOf(
            properties.getDataProtectionRegime());
        assertThat(offeringCs.getDataAccountExport()).usingRecursiveComparison()
            .isEqualTo(properties.getDataAccountExport());
        assertEquals(resourceCs.getCopyrightOwnedBy().getId(), properties.getCopyrightOwnedBy().getId());
        assertEquals(resourceCs.getProducedBy().getId(), properties.getProducedBy().getId());
        assertEquals(resourceCs.getExposedThrough().getId(), properties.getExposedThrough().getId());
        assertThat(resourceCs.getLicense()).containsExactlyInAnyOrderElementsOf(properties.getLicense());
        assertEquals(resourceCs.isContainsPII(), properties.isContainsPII());
        assertThat(properties.getOfferingPolicy()).hasSize(2).contains("dummyServiceOfferingPolicy");
        assertThat(properties.getDataPolicy()).hasSize(1).contains("dummyDataResourcePolicy");
        //check if file name is set correctly
        assertEquals(FILE_NAME, assetCreateRequest.getDataAddress().getKeyName());
        assertEquals(FILE_NAME, ((IonosS3DataSource) assetCreateRequest.getDataAddress()).getBlobName());

        ContractDefinitionCreateRequest contractDefinitionCreateRequest = contractDefinitionCreateRequestCaptor.getValue();

        List<PolicyCreateRequest> policyCreateRequests = policyCreateRequestCaptor.getAllValues();

        boolean foundAccessPolicy = false;
        for (var policy : policyCreateRequests) {
            //check if policyId is set correctly
            assertTrue(
                policy.getId().matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));

            if (contractDefinitionCreateRequest.getAccessPolicyId().equals(policy.getId())) {
                foundAccessPolicy = true;
                // for access policy make sure there are no constraints
                for (OdrlPermission permission : policy.getPolicy().getPermission()) {
                    assertTrue(permission.getConstraint().isEmpty());
                }
            }
        }
        assertTrue(foundAccessPolicy);

        assertNotNull(response);
        assertNotNull(response.getEdcResponseId());
        assertNotNull(response.getFhResponseId());
    }

    @Test
    void testCreateServiceOfferingEdcError() {

        reset(fhCatalogClient);
        reset(edcClient);

        //given
        GxServiceOfferingCredentialSubject offeringCs = getGxServiceOfferingCredentialSubject();

        CreateServiceOfferingRequestBE be = CreateServiceOfferingRequestBE.builder()
            .enforcementPolicies(List.of(new EverythingAllowedPolicy())).providedBy(offeringCs.getProvidedBy())
            .name(EdcClientFake.BAD_GATEWAY_ASSET_ID).description(offeringCs.getDescription())
            .termsAndConditions(offeringCs.getTermsAndConditions()).dataAccountExport(offeringCs.getDataAccountExport())
            .policy(offeringCs.getPolicy()).dataProtectionRegime(offeringCs.getDataProtectionRegime()).build();

        //when
        assertThrows(PossibleXException.class, () -> providerService.createOffering(be));
        verify(fhCatalogClient).deleteServiceOfferingFromFhCatalog(any(), Mockito.anyBoolean());
    }

    @Test
    void testGetPrefillFields() {
        //when
        PrefillFieldsBE prefillFields = providerService.getPrefillFields();

        //then
        String expectedId = "did:web:test.eu";
        String expectedServiceOfferingName = "Data Product Service for Data Resource <Data resource name>";
        String expectedServiceOfferingDescription = "Data Product Service provides data (<Data resource name>).";
        assertEquals(expectedId, prefillFields.getParticipantId());
        assertEquals(expectedServiceOfferingName, prefillFields.getDataProductPrefillFields().getServiceOfferingName());
        assertEquals(expectedServiceOfferingDescription,
            prefillFields.getDataProductPrefillFields().getServiceOfferingDescription());
    }

    @Test
    void testEnforcementPoliciesAreTranslatedCorrectly() {
        //given
        GxServiceOfferingCredentialSubject offeringCs = getGxServiceOfferingCredentialSubject();

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

        CreateServiceOfferingRequestBE be = CreateServiceOfferingRequestBE.builder().enforcementPolicies(policies)
            .providedBy(offeringCs.getProvidedBy()).name(offeringCs.getName()).description(offeringCs.getDescription())
            .termsAndConditions(offeringCs.getTermsAndConditions()).dataAccountExport(offeringCs.getDataAccountExport())
            .policy(offeringCs.getPolicy()).dataProtectionRegime(offeringCs.getDataProtectionRegime()).build();

        //when
        providerService.createOffering(be);

        //then
        ArgumentCaptor<PolicyCreateRequest> policyCreateRequestCaptor = forClass(PolicyCreateRequest.class);
        verify(edcClient, times(2)).createPolicy(policyCreateRequestCaptor.capture());

        List<PolicyCreateRequest> policyCreateRequests = policyCreateRequestCaptor.getAllValues();
        // contract policy gets created last
        PolicyCreateRequest contractPolicyCreateRequest = policyCreateRequests.get(policyCreateRequests.size() - 1);
        //check if policyId is set correctly
        assertTrue(contractPolicyCreateRequest.getId()
            .matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));
        int constraintCount = 0;
        // for access policy make sure there are no constraints
        for (OdrlPermission permission : contractPolicyCreateRequest.getPolicy().getPermission()) {
            if (permission.getAction().equals(OdrlAction.USE)) {  // currently only use is enforced
                continue;
            }
            for (OdrlConstraint constraint : permission.getConstraint()) {
                constraintCount++;
                if (constraint.getLeftOperand().equals(ParticipantRestrictionPolicy.EDC_OPERAND)) {
                    assertIterableEquals(participantRestrictionPolicy.getAllowedParticipants(),
                        Arrays.asList(constraint.getRightOperand().split(",")));
                } else if (constraint.getLeftOperand().equals(TimeAgreementOffsetPolicy.EDC_OPERAND)) {
                    boolean endDate = constraint.getOperator().equals(OdrlOperator.LEQ);
                    boolean offsetPolicy = constraint.getRightOperand().startsWith("contractAgreement+");

                    if (offsetPolicy) {
                        String offsetString = constraint.getRightOperand().substring("contractAgreement+".length());
                        int offsetNumber = Integer.parseInt(offsetString.substring(0, offsetString.length() - 1));
                        AgreementOffsetUnit offsetUnit = AgreementOffsetUnit.forValue(
                            offsetString.substring(offsetString.length() - 1));
                        if (endDate) {
                            assertEquals(endAgreementOffsetPolicy.getOffsetNumber(), offsetNumber);
                            assertEquals(endAgreementOffsetPolicy.getOffsetUnit(), offsetUnit);
                        } else {
                            assertEquals(startAgreementOffsetPolicy.getOffsetNumber(), offsetNumber);
                            assertEquals(startAgreementOffsetPolicy.getOffsetUnit(), offsetUnit);
                        }
                    } else {
                        if (endDate) {
                            assertEquals(endDatePolicy.getDate(), OffsetDateTime.parse(constraint.getRightOperand()));
                        } else {
                            assertEquals(startDatePolicy.getDate(), OffsetDateTime.parse(constraint.getRightOperand()));
                        }
                    }
                } else {
                    fail("Unexpected constraint: " + constraint);
                }
            }
        }
        assertEquals(policies.size(), constraintCount);
    }

    GxServiceOfferingCredentialSubject getGxServiceOfferingCredentialSubject() {

        return GxServiceOfferingCredentialSubject.builder()
            .providedBy(new NodeKindIRITypeId("did:web:example-organization.eu")).name("Test Service Offering")
            .description("This is the service offering description.").policy(List.of("dummyServiceOfferingPolicy"))
            .dataAccountExport(List.of(
                GxDataAccountExport.builder().formatType("application/json").accessType("digital").requestType("API")
                    .build())).dataProtectionRegime(List.of("GDPR"))
            .termsAndConditions(List.of(GxSOTermsAndConditions.builder().url("test.eu/tnc").hash("hash123").build()))
            .id("urn:uuid:GENERATED_SERVICE_OFFERING_ID").build();
    }

    GxDataResourceCredentialSubject getGxDataResourceCredentialSubject() {

        return GxDataResourceCredentialSubject.builder().policy(List.of("dummyDataResourcePolicy")).name("Test Dataset")
            .description("This is the data resource description.").license(List.of("AGPL-1.0-only")).containsPII(true)
            .copyrightOwnedBy(new NodeKindIRITypeId("did:web:example-organization.eu"))
            .producedBy(new NodeKindIRITypeId("did:web:example-organization.eu"))
            .exposedThrough(new NodeKindIRITypeId("urn:uuid:GENERATED_SERVICE_OFFERING_ID"))
            .id("urn:uuid:GENERATED_DATA_RESOURCE_ID").build();
    }

    // Test-specific configuration to provide a fake implementation of EdcClient and FhCatalogClient
    @TestConfiguration
    static class TestConfig {
        @Bean
        public EdcClient edcClient() {

            return Mockito.spy(new EdcClientFake());
        }

        @Bean
        public FhCatalogClient fhCatalogClient() {

            return Mockito.spy(new FhCatalogClientFake());
        }

        @Bean
        public ObjectMapper objectMapper() {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper;
        }

        @Bean
        public ProviderServiceMapper providerServiceMapper() {

            return Mappers.getMapper(ProviderServiceMapper.class);
        }
    }

}