package eu.possiblex.participantportal.application.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.application.control.ProviderApiMapper;
import eu.possiblex.participantportal.application.entity.CreateDataOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.CreateServiceOfferingRequestTO;
import eu.possiblex.participantportal.business.control.ProviderService;
import eu.possiblex.participantportal.business.control.ProviderServiceFake;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.edc.policy.Policy;
import eu.possiblex.participantportal.business.entity.fh.CreateFhDataOfferingBE;
import eu.possiblex.participantportal.business.entity.fh.CreateFhServiceOfferingBE;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.datatypes.GxDataAccountExport;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.datatypes.GxSOTermsAndConditions;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.resources.GxDataResourceCredentialSubject;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.serviceofferings.GxServiceOfferingCredentialSubject;
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

        ArgumentCaptor<CreateFhServiceOfferingBE> createServiceOfferingCaptor = ArgumentCaptor.forClass(
            CreateFhServiceOfferingBE.class);
        ArgumentCaptor<CreateEdcOfferBE> createEdcOfferCaptor = ArgumentCaptor.forClass(CreateEdcOfferBE.class);

        verify(providerService).createServiceOffering(createServiceOfferingCaptor.capture(),
            createEdcOfferCaptor.capture());

        CreateFhServiceOfferingBE createFhServiceOfferingBE = createServiceOfferingCaptor.getValue();
        CreateEdcOfferBE createEdcOfferBE = createEdcOfferCaptor.getValue();

        Policy serviceOfferingPolicy = objectMapper.readValue(
            createFhServiceOfferingBE.getServiceOfferingCredentialSubject().getPolicy().get(0), Policy.class);

        //check if request is mapped correctly
        assertThat(request.getPolicy()).usingRecursiveComparison().isEqualTo(serviceOfferingPolicy);
        assertThat(expectedServiceOfferingCS).usingRecursiveComparison()
            .isEqualTo(createFhServiceOfferingBE.getServiceOfferingCredentialSubject());
        assertThat(request.getPolicy()).usingRecursiveComparison().isEqualTo(createEdcOfferBE.getPolicy());
        assertEquals("Test Service Offering", createEdcOfferBE.getAssetName());
        assertEquals("This is the service offering description.", createEdcOfferBE.getAssetDescription());
    }

    @Test
    void shouldReturnMessageOnCreateDataOffering() throws Exception {
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

        ArgumentCaptor<CreateFhDataOfferingBE> createDataOfferingCaptor = ArgumentCaptor.forClass(
            CreateFhDataOfferingBE.class);
        ArgumentCaptor<CreateEdcOfferBE> createEdcOfferCaptor = ArgumentCaptor.forClass(CreateEdcOfferBE.class);

        verify(providerService).createDataOffering(createDataOfferingCaptor.capture(), createEdcOfferCaptor.capture());

        CreateFhDataOfferingBE createFhDataOfferingBE = createDataOfferingCaptor.getValue();
        CreateEdcOfferBE createEdcOfferBE = createEdcOfferCaptor.getValue();

        Policy serviceOfferingPolicy = objectMapper.readValue(
            createFhDataOfferingBE.getServiceOfferingCredentialSubject().getPolicy().get(0), Policy.class);

        Policy dataOfferingPolicy = objectMapper.readValue(
            createFhDataOfferingBE.getDataResourceCredentialSubject().getPolicy().get(0), Policy.class);

        //check if request is mapped correctly
        assertThat(request.getPolicy()).usingRecursiveComparison().isEqualTo(serviceOfferingPolicy);
        assertThat(request.getPolicy()).usingRecursiveComparison().isEqualTo(dataOfferingPolicy);
        assertThat(expectedServiceOfferingCS).usingRecursiveComparison()
            .isEqualTo(createFhDataOfferingBE.getServiceOfferingCredentialSubject());
        assertThat(expectedDataResourceCS).usingRecursiveComparison()
            .isEqualTo(createFhDataOfferingBE.getDataResourceCredentialSubject());
        assertThat(request.getPolicy()).usingRecursiveComparison().isEqualTo(createEdcOfferBE.getPolicy());
        assertEquals("Test Service Offering", createEdcOfferBE.getAssetName());
        assertEquals("This is the service offering description.", createEdcOfferBE.getAssetDescription());
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
                "policy": {
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
                }
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
                "policy": {
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
                }
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
