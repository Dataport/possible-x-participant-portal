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
import eu.possiblex.participantportal.application.entity.policies.EnforcementPolicy;
import eu.possiblex.participantportal.application.entity.policies.EverythingAllowedPolicy;
import eu.possiblex.participantportal.business.control.ProviderService;
import eu.possiblex.participantportal.business.control.ProviderServiceFake;
import eu.possiblex.participantportal.business.entity.CreateDataOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;
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

@WebMvcTest(ProviderRestApiImpl.class)
@ContextConfiguration(classes = { ProviderRestApiTest.TestConfig.class, ProviderRestApiImpl.class })
class ProviderRestApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnMessageOnCreateServiceOffering() throws Exception {

        reset(providerService);

        //given
        CreateServiceOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingTOJsonString(),
            CreateServiceOfferingRequestTO.class);

        GxServiceOfferingCredentialSubject expectedServiceOfferingCS = getGxServiceOfferingCredentialSubject();

        //when
        //then
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
    }

    @Test
    void shouldReturnMessageOnCreateDataOffering() throws Exception {

        reset(providerService);

        //given
        CreateDataOfferingRequestTO request = objectMapper.readValue(getCreateDataOfferingTOJsonString(),
            CreateDataOfferingRequestTO.class);

        GxServiceOfferingCredentialSubject expectedServiceOfferingCS = getGxServiceOfferingCredentialSubject();
        GxDataResourceCredentialSubject expectedDataResourceCS = getGxDataResourceCredentialSubject();

        //when
        //then
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

        assertThat(expectedDataResourceCS).usingRecursiveComparison()
            .isEqualTo(createDataOfferingRequestBE.getDataResource());
    }

    @Test
    void shouldReturnMessageOnGetParticipantId() throws Exception {
        //when
        //then
        this.mockMvc.perform(get("/provider/id")).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.participantId").value(ProviderServiceFake.PARTICIPANT_ID));
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
                    .build()))
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
                        "vcard": "http://www.w3.org/2006/vcard/ns#"
                    },
                    "gx:providedBy": {
                        "@id": "did:web:example-organization.eu"
                    },
                    "gx:name": {
                        "@value": "Test Service Offering",
                        "@type": "xsd:string"
                    },
                    "gx:description": {
                        "@value": "This is the service offering description.",
                        "@type": "xsd:string"
                    },
                    "gx:policy": {
                        "@value": "{\\n  \\"@type\\": \\"odrl:Set\\",\\n  \\"odrl:permission\\": [\\n    {\\n      \\"odrl:action\\": {\\n        \\"odrl:type\\": \\"http://www.w3.org/ns/odrl/2/use\\"\\n      }\\n    },\\n    {\\n      \\"odrl:action\\": {\\n        \\"odrl:type\\": \\"http://www.w3.org/ns/odrl/2/transfer\\"\\n      }\\n    }\\n  ],\\n  \\"odrl:prohibition\\": [],\\n  \\"odrl:obligation\\": []\\n}",
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
                        "vcard": "http://www.w3.org/2006/vcard/ns#"
                    },
                    "gx:providedBy": {
                        "@id": "did:web:example-organization.eu"
                    },
                    "gx:name": {
                        "@value": "Test Service Offering",
                        "@type": "xsd:string"
                    },
                    "gx:description": {
                        "@value": "This is the service offering description.",
                        "@type": "xsd:string"
                    },
                    "gx:policy": {
                        "@value": "{\\n  \\"@type\\": \\"odrl:Set\\",\\n  \\"odrl:permission\\": [\\n    {\\n      \\"odrl:action\\": {\\n        \\"odrl:type\\": \\"http://www.w3.org/ns/odrl/2/use\\"\\n      }\\n    },\\n    {\\n      \\"odrl:action\\": {\\n        \\"odrl:type\\": \\"http://www.w3.org/ns/odrl/2/transfer\\"\\n      }\\n    }\\n  ],\\n  \\"odrl:prohibition\\": [],\\n  \\"odrl:obligation\\": []\\n}",
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
                        "vcard": "http://www.w3.org/2006/vcard/ns#"
                    },
                    "gx:copyrightOwnedBy": {
                        "@id": "did:web:example-organization.eu"
                    },
                    "gx:producedBy": {
                        "@id": "did:web:example-organization.eu"
                    },
                    "gx:name": {
                        "@value": "Test Dataset",
                        "@type": "xsd:string"
                    },
                    "gx:description": {
                        "@value": "This is the data resource description.",
                        "@type": "xsd:string"
                    },
                    "gx:license": {
                        "@value": "AGPL-1.0-only",
                        "@type": "xsd:string"
                    },
                    "gx:containsPII": true,
                    "gx:exposedThrough": {
                        "@id": "urn:uuid:GENERATED_SERVICE_OFFERING_ID"
                    },
                    "gx:policy": {
                        "@value": "{\\n  \\"@type\\": \\"odrl:Set\\",\\n  \\"odrl:permission\\": [\\n    {\\n      \\"odrl:action\\": {\\n        \\"odrl:type\\": \\"http://www.w3.org/ns/odrl/2/use\\"\\n      }\\n    },\\n    {\\n      \\"odrl:action\\": {\\n        \\"odrl:type\\": \\"http://www.w3.org/ns/odrl/2/transfer\\"\\n      }\\n    }\\n  ],\\n  \\"odrl:prohibition\\": [],\\n  \\"odrl:obligation\\": []\\n}",
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

            return new ObjectMapper();
            // Customize the ObjectMapper if needed
        }
    }
}
