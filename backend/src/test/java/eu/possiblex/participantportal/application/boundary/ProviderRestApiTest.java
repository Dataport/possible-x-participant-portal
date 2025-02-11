package eu.possiblex.participantportal.application.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.possiblex.participantportal.application.configuration.AppConfigurer;
import eu.possiblex.participantportal.application.configuration.BoundaryExceptionHandler;
import eu.possiblex.participantportal.application.control.ProviderApiMapper;
import eu.possiblex.participantportal.application.entity.CreateDataOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.CreateServiceOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxDataAccountExport;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.GxSOTermsAndConditions;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxDataResourceCredentialSubject;
import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxLegitimateInterest;
import eu.possiblex.participantportal.application.entity.credentials.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import eu.possiblex.participantportal.application.entity.policies.EverythingAllowedPolicy;
import eu.possiblex.participantportal.business.control.ProviderService;
import eu.possiblex.participantportal.business.control.ProviderServiceFake;
import eu.possiblex.participantportal.business.entity.CreateDataOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProviderRestApiImpl.class)
@ContextConfiguration(classes = { ProviderRestApiTest.TestConfig.class, ProviderRestApiImpl.class,
    BoundaryExceptionHandler.class, AppConfigurer.class })
class ProviderRestApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        reset(providerService);
    }

    @Test
    @WithMockUser(username = "admin")
    void createServiceOfferingSuccess() throws Exception {

        // GIVEN

        CreateServiceOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingTOJsonString(),
            CreateServiceOfferingRequestTO.class);

        GxServiceOfferingCredentialSubject expectedServiceOfferingCS = getGxServiceOfferingCredentialSubject();

        // WHEN/THEN

        this.mockMvc.perform(post("/provider/offer/service").content(RestApiHelper.asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.edcResponseId").value(ProviderServiceFake.CREATE_OFFER_RESPONSE_ID))
            .andExpect(jsonPath("$.fhResponseId").value(ProviderServiceFake.CREATE_OFFER_RESPONSE_ID));

        ArgumentCaptor<CreateServiceOfferingRequestBE> createServiceOfferingCaptor = ArgumentCaptor.forClass(
            CreateServiceOfferingRequestBE.class);

        verify(providerService).createOffering(createServiceOfferingCaptor.capture());

        CreateServiceOfferingRequestBE createServiceOfferingBE = createServiceOfferingCaptor.getValue();

        List<EnforcementPolicy> serviceOfferingPolicy = createServiceOfferingBE.getEnforcementPolicies();

        //check if request is mapped correctly
        assertThat(List.of(new EverythingAllowedPolicy())).usingRecursiveComparison().isEqualTo(serviceOfferingPolicy);
        assertThat(expectedServiceOfferingCS.getProvidedBy()).usingRecursiveComparison()
            .isEqualTo(createServiceOfferingBE.getProvidedBy());
        assertThat(expectedServiceOfferingCS.getDataAccountExport()).usingRecursiveComparison()
            .isEqualTo(createServiceOfferingBE.getDataAccountExport());
        assertThat(expectedServiceOfferingCS.getDataProtectionRegime()).usingRecursiveComparison()
            .isEqualTo(createServiceOfferingBE.getDataProtectionRegime());
        assertThat(expectedServiceOfferingCS.getTermsAndConditions()).usingRecursiveComparison()
            .isEqualTo(createServiceOfferingBE.getTermsAndConditions());
        assertEquals(expectedServiceOfferingCS.getName(), createServiceOfferingBE.getName());
        assertEquals(expectedServiceOfferingCS.getDescription(), createServiceOfferingBE.getDescription());
        assertEquals("dummyServiceOfferingPolicy", createServiceOfferingBE.getPolicy().get(0));
    }

    @Test
    @WithMockUser(username = "admin")
    void createServiceOfferingInvalidDataAccountExportFormatType() throws Exception {

        // GIVEN

        CreateServiceOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingTOJsonString(),
            CreateServiceOfferingRequestTO.class);
        request.getServiceOfferingCredentialSubject().getDataAccountExport().get(0).setFormatType("invalid");

        // WHEN/THEN

        MvcResult result = this.mockMvc.perform(post("/provider/offer/service").content(RestApiHelper.asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest()).andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("formatType"));
    }

    @Test
    @WithMockUser(username = "admin")
    void createServiceOfferingEdcOfferCreationFailed() throws Exception {

        // GIVEN

        CreateServiceOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingTOJsonString(),
            CreateServiceOfferingRequestTO.class);
        request.getServiceOfferingCredentialSubject().setName(ProviderServiceFake.EDC_OFFER_CREATION_FAILED_NAME);

        // WHEN/THEN

        this.mockMvc.perform(post("/provider/offer/service").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isUnprocessableEntity());

    }

    @Test
    @WithMockUser(username = "admin")
    void createServiceOfferingCatalogOfferCreationFailed() throws Exception {

        // GIVEN

        CreateServiceOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingTOJsonString(),
            CreateServiceOfferingRequestTO.class);
        request.getServiceOfferingCredentialSubject().setName(ProviderServiceFake.CATALOG_OFFER_CREATION_FAILED_NAME);

        // WHEN/THEN

        this.mockMvc.perform(post("/provider/offer/service").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isUnprocessableEntity());

    }

    @Test
    @WithMockUser(username = "admin")
    void createServiceOfferingNonCompliant() throws Exception {

        // GIVEN

        CreateServiceOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingTOJsonString(),
            CreateServiceOfferingRequestTO.class);
        request.getServiceOfferingCredentialSubject().setName(ProviderServiceFake.COMPLIANCE_ERROR_NAME);

        // WHEN/THEN

        this.mockMvc.perform(post("/provider/offer/service").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isUnprocessableEntity());

    }

    @Test
    @WithMockUser(username = "admin")
    void createDataOfferingSuccess() throws Exception {

        // GIVEN

        CreateDataOfferingRequestTO request = objectMapper.readValue(getCreateDataOfferingTOJsonString(),
            CreateDataOfferingRequestTO.class);

        GxServiceOfferingCredentialSubject expectedServiceOfferingCS = getGxServiceOfferingCredentialSubject();
        GxDataResourceCredentialSubject expectedDataResourceCS = getGxDataResourceCredentialSubject();

        // WHEN/THEN

        this.mockMvc.perform(post("/provider/offer/data").content(RestApiHelper.asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.edcResponseId").value(ProviderServiceFake.CREATE_OFFER_RESPONSE_ID))
            .andExpect(jsonPath("$.fhResponseId").value(ProviderServiceFake.CREATE_OFFER_RESPONSE_ID));

        ArgumentCaptor<CreateDataOfferingRequestBE> createDataOfferingRequestBEArgumentCaptor = ArgumentCaptor.forClass(
            CreateDataOfferingRequestBE.class);

        verify(providerService).createOffering(createDataOfferingRequestBEArgumentCaptor.capture());

        CreateDataOfferingRequestBE createDataOfferingRequestBE = createDataOfferingRequestBEArgumentCaptor.getValue();
        List<EnforcementPolicy> serviceOfferingPolicy = createDataOfferingRequestBE.getEnforcementPolicies();

        //check if request is mapped correctly
        assertThat(List.of(new EverythingAllowedPolicy())).usingRecursiveComparison().isEqualTo(serviceOfferingPolicy);
        assertThat(expectedServiceOfferingCS.getProvidedBy()).usingRecursiveComparison()
            .isEqualTo(createDataOfferingRequestBE.getProvidedBy());
        assertThat(expectedServiceOfferingCS.getDataAccountExport()).usingRecursiveComparison()
            .isEqualTo(createDataOfferingRequestBE.getDataAccountExport());
        assertThat(expectedServiceOfferingCS.getDataProtectionRegime()).usingRecursiveComparison()
            .isEqualTo(createDataOfferingRequestBE.getDataProtectionRegime());
        assertThat(expectedServiceOfferingCS.getTermsAndConditions()).usingRecursiveComparison()
            .isEqualTo(createDataOfferingRequestBE.getTermsAndConditions());
        assertEquals(expectedServiceOfferingCS.getName(), createDataOfferingRequestBE.getName());
        assertEquals(expectedServiceOfferingCS.getDescription(), createDataOfferingRequestBE.getDescription());
        assertEquals(request.getFileName(), createDataOfferingRequestBE.getFileName());
        assertEquals("dummyServiceOfferingPolicy", createDataOfferingRequestBE.getPolicy().get(0));

        assertThat(expectedDataResourceCS).usingRecursiveComparison()
            .isEqualTo(createDataOfferingRequestBE.getDataResource());
    }

    @Test
    @WithMockUser(username = "admin")
    void createDataOfferingContainingPIISuccess() throws Exception {

        // GIVEN

        CreateDataOfferingRequestTO request = objectMapper.readValue(getCreateDataOfferingTOJsonString(),
            CreateDataOfferingRequestTO.class);
        request.getDataResourceCredentialSubject().setContainsPII(true);
        request.setLegitimateInterest(
            GxLegitimateInterest.builder().dataProtectionContact("contact").legalBasis("basis").build());

        GxServiceOfferingCredentialSubject expectedServiceOfferingCS = getGxServiceOfferingCredentialSubject();
        GxDataResourceCredentialSubject expectedDataResourceCS = getGxDataResourceCredentialSubject();
        expectedDataResourceCS.setContainsPII(true);
        GxLegitimateInterest expectedLegitimateInterest = GxLegitimateInterest.builder()
            .dataProtectionContact("contact").legalBasis("basis").build();

        // WHEN/THEN

        this.mockMvc.perform(post("/provider/offer/data").content(RestApiHelper.asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.edcResponseId").value(ProviderServiceFake.CREATE_OFFER_RESPONSE_ID))
            .andExpect(jsonPath("$.fhResponseId").value(ProviderServiceFake.CREATE_OFFER_RESPONSE_ID));

        ArgumentCaptor<CreateDataOfferingRequestBE> createDataOfferingRequestBEArgumentCaptor = ArgumentCaptor.forClass(
            CreateDataOfferingRequestBE.class);

        verify(providerService).createOffering(createDataOfferingRequestBEArgumentCaptor.capture());

        CreateDataOfferingRequestBE createDataOfferingRequestBE = createDataOfferingRequestBEArgumentCaptor.getValue();
        List<EnforcementPolicy> serviceOfferingPolicy = createDataOfferingRequestBE.getEnforcementPolicies();

        //check if request is mapped correctly
        assertThat(List.of(new EverythingAllowedPolicy())).usingRecursiveComparison().isEqualTo(serviceOfferingPolicy);
        assertThat(expectedServiceOfferingCS.getProvidedBy()).usingRecursiveComparison()
            .isEqualTo(createDataOfferingRequestBE.getProvidedBy());
        assertThat(expectedServiceOfferingCS.getDataAccountExport()).usingRecursiveComparison()
            .isEqualTo(createDataOfferingRequestBE.getDataAccountExport());
        assertThat(expectedServiceOfferingCS.getDataProtectionRegime()).usingRecursiveComparison()
            .isEqualTo(createDataOfferingRequestBE.getDataProtectionRegime());
        assertThat(expectedServiceOfferingCS.getTermsAndConditions()).usingRecursiveComparison()
            .isEqualTo(createDataOfferingRequestBE.getTermsAndConditions());
        assertEquals(expectedServiceOfferingCS.getName(), createDataOfferingRequestBE.getName());
        assertEquals(expectedServiceOfferingCS.getDescription(), createDataOfferingRequestBE.getDescription());
        assertEquals(request.getFileName(), createDataOfferingRequestBE.getFileName());
        assertEquals("dummyServiceOfferingPolicy", createDataOfferingRequestBE.getPolicy().get(0));

        assertThat(expectedDataResourceCS).usingRecursiveComparison()
            .isEqualTo(createDataOfferingRequestBE.getDataResource());

        assertThat(expectedLegitimateInterest).usingRecursiveComparison()
            .isEqualTo(createDataOfferingRequestBE.getLegitimateInterest());
    }

    @Test
    @WithMockUser(username = "admin")
    void createDataOfferingContainingPIIMissingLegitimateInterest() throws Exception {

        // GIVEN

        CreateDataOfferingRequestTO request = objectMapper.readValue(getCreateDataOfferingTOJsonString(),
            CreateDataOfferingRequestTO.class);
        request.getDataResourceCredentialSubject().setContainsPII(true);

        // WHEN/THEN

        MvcResult result = this.mockMvc.perform(
            post("/provider/offer/data").content(RestApiHelper.asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest()).andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("legitimateInterest"));
    }

    @Test
    @WithMockUser(username = "admin")
    void getPrefillFields() throws Exception {
        // WHEN/THEN
        this.mockMvc.perform(get("/provider/prefillFields")).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.participantId").value(ProviderServiceFake.PARTICIPANT_ID)).andExpect(
                jsonPath("$.dataProductPrefillFields.serviceOfferingName").value(ProviderServiceFake.SERVICE_OFFERING_NAME))
            .andExpect(jsonPath("$.dataProductPrefillFields.serviceOfferingDescription").value(
                ProviderServiceFake.SERVICE_OFFERING_DESCRIPTION));
    }

    GxServiceOfferingCredentialSubject getGxServiceOfferingCredentialSubject() {

        return GxServiceOfferingCredentialSubject.builder()
            .providedBy(new NodeKindIRITypeId("did:web:example-organization.eu")).name("Test Service Offering")
            .description("This is the service offering description.").policy(List.of("dummyServiceOfferingPolicy"))
            .dataAccountExport(List.of(
                GxDataAccountExport.builder().formatType("application/json").accessType("digital").requestType("API")
                    .build()))
            .termsAndConditions(List.of(GxSOTermsAndConditions.builder().url("test.eu/tnc").hash("hash123").build()))
            .id("urn:uuid:GENERATED_SERVICE_OFFERING_ID").build();
    }

    GxDataResourceCredentialSubject getGxDataResourceCredentialSubject() {

        return GxDataResourceCredentialSubject.builder().policy(List.of("dummyDataResourcePolicy")).name("Test Dataset")
            .description("This is the data resource description.").license(List.of("AGPL-1.0-only")).containsPII(false)
            .copyrightOwnedBy(List.of("did:web:example-organization.eu"))
            .producedBy(new NodeKindIRITypeId("did:web:example-organization.eu"))
            .exposedThrough(new NodeKindIRITypeId("urn:uuid:GENERATED_SERVICE_OFFERING_ID"))
            .id("urn:uuid:GENERATED_DATA_RESOURCE_ID").build();
    }

    String getCreateServiceOfferingTOJsonString() {

        return """
            {
                "serviceOfferingCredentialSubject": {
                    "@context": {
                        "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                        "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
                        "sh": "http://www.w3.org/ns/shacl#",
                        "xsd": "http://www.w3.org/2001/XMLSchema#",
                        "skos": "http://www.w3.org/2004/02/skos/core#",
                        "rdfs": "http://www.w3.org/2000/01/rdf-schema#",
                        "vcard": "http://www.w3.org/2006/vcard/ns#",
                        "schema": "https://schema.org/"
                    },
                    "gx:providedBy": {
                        "@id": "did:web:example-organization.eu"
                    },
                    "schema:name": {
                        "@value": "Test Service Offering",
                        "@type": "xsd:string"
                    },
                    "schema:description": {
                        "@value": "This is the service offering description.",
                        "@type": "xsd:string"
                    },
                    "gx:policy": {
                        "@value": "dummyServiceOfferingPolicy",
                        "@type": "xsd:string"
                    },
                    "gx:dataAccountExport": {
                        "@type": "gx:DataAccountExport",
                        "gx:formatType": {
                            "@value": "application/json",
                            "@type": "xsd:string"
                        },
                        "gx:accessType": {
                            "@value": "digital",
                            "@type": "xsd:string"
                        },
                        "gx:requestType": {
                            "@value": "API",
                            "@type": "xsd:string"
                        }
                    },
                    "gx:termsAndConditions": {
                        "@type": "gx:SOTermsAndConditions",
                        "gx:URL": {
                            "@value": "test.eu/tnc",
                            "@type": "xsd:string"
                        },
                        "gx:hash": {
                            "@value": "hash123",
                            "@type": "xsd:string"
                        }
                    },
                    "id": "urn:uuid:GENERATED_SERVICE_OFFERING_ID",
                    "@type": "gx:ServiceOffering"
                },
                "enforcementPolicies": []
            }""";
    }

    String getCreateDataOfferingTOJsonString() {

        return """
            {
                "serviceOfferingCredentialSubject": {
                    "@context": {
                        "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                        "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
                        "sh": "http://www.w3.org/ns/shacl#",
                        "xsd": "http://www.w3.org/2001/XMLSchema#",
                        "skos": "http://www.w3.org/2004/02/skos/core#",
                        "rdfs": "http://www.w3.org/2000/01/rdf-schema#",
                        "vcard": "http://www.w3.org/2006/vcard/ns#",
                        "schema": "https://schema.org/"
                    },
                    "gx:providedBy": {
                        "@id": "did:web:example-organization.eu"
                    },
                    "schema:name": {
                        "@value": "Test Service Offering",
                        "@type": "xsd:string"
                    },
                    "schema:description": {
                        "@value": "This is the service offering description.",
                        "@type": "xsd:string"
                    },
                    "gx:policy": {
                        "@value": "dummyServiceOfferingPolicy",
                        "@type": "xsd:string"
                    },
                    "gx:dataAccountExport": {
                        "@type": "gx:DataAccountExport",
                        "gx:formatType": {
                            "@value": "application/json",
                            "@type": "xsd:string"
                        },
                        "gx:accessType": {
                            "@value": "digital",
                            "@type": "xsd:string"
                        },
                        "gx:requestType": {
                            "@value": "API",
                            "@type": "xsd:string"
                        }
                    },
                    "gx:termsAndConditions": {
                        "@type": "gx:SOTermsAndConditions",
                        "gx:URL": {
                            "@value": "test.eu/tnc",
                            "@type": "xsd:string"
                        },
                        "gx:hash": {
                            "@value": "hash123",
                            "@type": "xsd:string"
                        }
                    },
                    "id": "urn:uuid:GENERATED_SERVICE_OFFERING_ID",
                    "@type": "gx:ServiceOffering"
                },
                "dataResourceCredentialSubject": {
                    "@context": {
                        "gx": "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                        "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
                        "sh": "http://www.w3.org/ns/shacl#",
                        "xsd": "http://www.w3.org/2001/XMLSchema#",
                        "skos": "http://www.w3.org/2004/02/skos/core#",
                        "rdfs": "http://www.w3.org/2000/01/rdf-schema#",
                        "vcard": "http://www.w3.org/2006/vcard/ns#",
                        "schema": "https://schema.org/"
                    },
                    "gx:copyrightOwnedBy": [
                        "did:web:example-organization.eu"
                    ],
                    "gx:producedBy": {
                        "@id": "did:web:example-organization.eu"
                    },
                    "schema:name": {
                        "@value": "Test Dataset",
                        "@type": "xsd:string"
                    },
                    "schema:description": {
                        "@value": "This is the data resource description.",
                        "@type": "xsd:string"
                    },
                    "gx:license": {
                        "@value": "AGPL-1.0-only",
                        "@type": "xsd:string"
                    },
                    "gx:containsPII": false,
                    "gx:exposedThrough": {
                        "@id": "urn:uuid:GENERATED_SERVICE_OFFERING_ID"
                    },
                    "gx:policy": {
                        "@value": "dummyDataResourcePolicy",
                        "@type": "xsd:string"
                    },
                    "id": "urn:uuid:GENERATED_DATA_RESOURCE_ID",
                    "@type": "gx:DataResource"
                },
                "fileName": "testfile.txt",
                "enforcementPolicies": []
            }""";
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ProviderService providerService() {

            return Mockito.spy(new ProviderServiceFake());
        }

        @Bean
        public ProviderApiMapper providerApiMapper() {

            return Mappers.getMapper(ProviderApiMapper.class);
        }

        @Bean
        public ObjectMapper objectMapper() {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return objectMapper;
        }
    }
}
