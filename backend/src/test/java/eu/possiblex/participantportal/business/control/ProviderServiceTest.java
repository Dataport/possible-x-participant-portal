package eu.possiblex.participantportal.business.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxDataAccountExport;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxSOTermsAndConditions;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxDataResourceCredentialSubject;
import eu.possiblex.participantportal.application.entity.credentials.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.application.entity.policies.EverythingAllowedPolicy;
import eu.possiblex.participantportal.business.entity.CreateDataOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedDataResourceCredentialSubject;
import eu.possiblex.participantportal.business.entity.credentials.px.PxExtendedServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.business.entity.edc.asset.AssetCreateRequest;
import eu.possiblex.participantportal.business.entity.edc.asset.ionoss3extension.IonosS3DataSource;
import eu.possiblex.participantportal.business.entity.edc.asset.possible.PossibleAssetProperties;
import eu.possiblex.participantportal.business.entity.edc.policy.PolicyCreateRequest;
import eu.possiblex.participantportal.business.entity.exception.EdcOfferCreationException;
import eu.possiblex.participantportal.business.entity.exception.FhOfferCreationException;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

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
    void testCreateServiceOffering()
        throws JsonProcessingException, EdcOfferCreationException, FhOfferCreationException {

        reset(fhCatalogClient);
        reset(edcClient);

        //given

        GxServiceOfferingCredentialSubject offeringCs = getGxServiceOfferingCredentialSubject();

        CreateServiceOfferingRequestBE be = CreateServiceOfferingRequestBE.builder()
            .enforcementPolicies(List.of(new EverythingAllowedPolicy())).providedBy(offeringCs.getProvidedBy())
            .name(offeringCs.getName()).description(offeringCs.getDescription())
            .termsAndConditions(offeringCs.getTermsAndConditions()).dataAccountExport(offeringCs.getDataAccountExport())
            .dataProtectionRegime(offeringCs.getDataProtectionRegime()).build();

        //when
        var response = providerService.createOffering(be);

        //then
        ArgumentCaptor<AssetCreateRequest> assetCreateRequestCaptor = forClass(AssetCreateRequest.class);
        ArgumentCaptor<PolicyCreateRequest> policyCreateRequestCaptor = forClass(PolicyCreateRequest.class);

        ArgumentCaptor<PxExtendedServiceOfferingCredentialSubject> serviceOfferingCaptor = forClass(
            PxExtendedServiceOfferingCredentialSubject.class);

        verify(fhCatalogClient).addServiceOfferingToFhCatalog(serviceOfferingCaptor.capture());

        PxExtendedServiceOfferingCredentialSubject pxExtSoCs = serviceOfferingCaptor.getValue();
        assertTrue(pxExtSoCs.getId()
            .matches("urn:uuid:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));
        //check if assetId exists and provider url is set correctly
        assertNotNull(pxExtSoCs);
        assertTrue(pxExtSoCs.getAssetId()
            .matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));
        assertEquals("test", pxExtSoCs.getProviderUrl());

        verify(edcClient).createAsset(assetCreateRequestCaptor.capture());
        verify(edcClient).createPolicy(policyCreateRequestCaptor.capture());
        verify(edcClient).createContractDefinition(any());

        AssetCreateRequest assetCreateRequest = assetCreateRequestCaptor.getValue();
        //validate asset properties
        PossibleAssetProperties properties = (PossibleAssetProperties) assetCreateRequest.getProperties();
        assertEquals(offeringCs.getName(), properties.getName());
        assertEquals(offeringCs.getDescription(), properties.getDescription());
        assertEquals(offeringCs.getProvidedBy().getId(), properties.getProvidedBy().getId());
        assertThat(offeringCs.getTermsAndConditions()).usingRecursiveComparison()
            .isEqualTo(properties.getTermsAndConditions());
        assertThat(offeringCs.getDataProtectionRegime()).containsExactlyInAnyOrderElementsOf(
            properties.getDataProtectionRegime());
        assertThat(offeringCs.getDataAccountExport()).usingRecursiveComparison()
            .isEqualTo(properties.getDataAccountExport());
        //check if file name is set correctly
        assertEquals("", assetCreateRequest.getDataAddress().getKeyName());
        assertEquals("", ((IonosS3DataSource) assetCreateRequest.getDataAddress()).getBlobName());

        PolicyCreateRequest policyCreateRequest = policyCreateRequestCaptor.getValue();
        //check if policyId is set correctly
        assertTrue(policyCreateRequest.getId()
            .matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));

        assertNotNull(response);
        assertNotNull(response.getEdcResponseId());
        assertNotNull(response.getFhResponseId());
    }

    @Test
    void testCreateDataOffering() throws JsonProcessingException, EdcOfferCreationException, FhOfferCreationException {

        reset(fhCatalogClient);
        reset(edcClient);

        //given
        GxServiceOfferingCredentialSubject offeringCs = getGxServiceOfferingCredentialSubject();
        GxDataResourceCredentialSubject resourceCs = getGxDataResourceCredentialSubject();

        CreateDataOfferingRequestBE be = CreateDataOfferingRequestBE.builder().fileName(FILE_NAME)
            .enforcementPolicies(List.of(new EverythingAllowedPolicy())).providedBy(offeringCs.getProvidedBy())
            .name(offeringCs.getName()).description(offeringCs.getDescription())
            .termsAndConditions(offeringCs.getTermsAndConditions()).dataAccountExport(offeringCs.getDataAccountExport())
            .dataProtectionRegime(offeringCs.getDataProtectionRegime()).dataResource(resourceCs).build();

        //when
        var response = providerService.createOffering(be);

        //then
        ArgumentCaptor<AssetCreateRequest> assetCreateRequestCaptor = forClass(AssetCreateRequest.class);
        ArgumentCaptor<PolicyCreateRequest> policyCreateRequestCaptor = forClass(PolicyCreateRequest.class);

        ArgumentCaptor<PxExtendedServiceOfferingCredentialSubject> serviceOfferingCaptor = forClass(
            PxExtendedServiceOfferingCredentialSubject.class);

        verify(fhCatalogClient).addServiceOfferingToFhCatalog(serviceOfferingCaptor.capture());

        PxExtendedServiceOfferingCredentialSubject pxExtSoCs = serviceOfferingCaptor.getValue();
        assertNotNull(pxExtSoCs);
        String serviceOfferingId = pxExtSoCs.getId();
        PxExtendedDataResourceCredentialSubject dataResource = pxExtSoCs.getAggregationOf().get(0);
        assertTrue(serviceOfferingId.matches(
            "urn:uuid:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));
        assertTrue(dataResource.getId()
            .matches("urn:uuid:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));
        assertEquals(serviceOfferingId, dataResource.getExposedThrough().getId());
        //check if assetId exists and provider url is set correctly
        assertTrue(pxExtSoCs.getAssetId()
            .matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));
        assertEquals("test", pxExtSoCs.getProviderUrl());

        verify(edcClient).createAsset(assetCreateRequestCaptor.capture());
        verify(edcClient).createPolicy(policyCreateRequestCaptor.capture());
        verify(edcClient).createContractDefinition(any());

        AssetCreateRequest assetCreateRequest = assetCreateRequestCaptor.getValue();
        //validate asset properties
        PossibleAssetProperties properties = (PossibleAssetProperties) assetCreateRequest.getProperties();
        assertEquals(offeringCs.getName(), properties.getName());
        assertEquals(offeringCs.getDescription(), properties.getDescription());
        assertEquals(offeringCs.getProvidedBy().getId(), properties.getProvidedBy().getId());
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
        //check if file name is set correctly
        assertEquals(FILE_NAME, assetCreateRequest.getDataAddress().getKeyName());
        assertEquals(FILE_NAME, ((IonosS3DataSource) assetCreateRequest.getDataAddress()).getBlobName());

        PolicyCreateRequest policyCreateRequest = policyCreateRequestCaptor.getValue();
        //check if policyId is set correctly
        assertTrue(policyCreateRequest.getId()
            .matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));

        assertNotNull(response);
        assertNotNull(response.getEdcResponseId());
        assertNotNull(response.getFhResponseId());
    }

    @Test
    void testGetParticipantId() {
        //when
        var participantIdTO = providerService.getParticipantId();

        //then
        String expectedId = "did:web:test.eu";
        assertEquals(expectedId, participantIdTO.getParticipantId());
    }

    GxServiceOfferingCredentialSubject getGxServiceOfferingCredentialSubject() {

        return GxServiceOfferingCredentialSubject.builder()
            .providedBy(new NodeKindIRITypeId("did:web:example-organization.eu")).name("Test Service Offering")
            .description("This is the service offering description.").policy(List.of("""
                {
                  "@type": "odrl:Set",
                  "odrl:permission": [
                    {
                      "odrl:action": {
                        "odrl:type": "http://www.w3.org/ns/odrl/2/use"
                      }
                    },
                    {
                      "odrl:action": {
                        "odrl:type": "http://www.w3.org/ns/odrl/2/transfer"
                      }
                    }
                  ],
                  "odrl:prohibition": [],
                  "odrl:obligation": []
                }""")).dataAccountExport(List.of(
                GxDataAccountExport.builder().formatType("application/json").accessType("digital").requestType("API")
                    .build())).dataProtectionRegime(List.of("GDPR"))
            .termsAndConditions(List.of(GxSOTermsAndConditions.builder().url("test.eu/tnc").hash("hash123").build()))
            .id("urn:uuid:GENERATED_SERVICE_OFFERING_ID").build();
    }

    GxDataResourceCredentialSubject getGxDataResourceCredentialSubject() {

        return GxDataResourceCredentialSubject.builder().policy(List.of("""
                {
                  "@type": "odrl:Set",
                  "odrl:permission": [
                    {
                      "odrl:action": {
                        "odrl:type": "http://www.w3.org/ns/odrl/2/use"
                      }
                    },
                    {
                      "odrl:action": {
                        "odrl:type": "http://www.w3.org/ns/odrl/2/transfer"
                      }
                    }
                  ],
                  "odrl:prohibition": [],
                  "odrl:obligation": []
                }""")).name("Test Dataset").description("This is the data resource description.")
            .license(List.of("AGPL-1.0-only")).containsPII(true)
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