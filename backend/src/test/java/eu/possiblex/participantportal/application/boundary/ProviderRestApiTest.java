package eu.possiblex.participantportal.application.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.application.control.ProviderApiMapper;
import eu.possiblex.participantportal.application.entity.CreateOfferRequestTO;
import eu.possiblex.participantportal.business.control.ProviderService;
import eu.possiblex.participantportal.business.control.ProviderServiceFake;
import eu.possiblex.participantportal.business.entity.edc.CreateEdcOfferBE;
import eu.possiblex.participantportal.business.entity.fh.CreateFhOfferBE;
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
    void shouldReturnMessageOnCreateOffer() throws Exception {
        //given
        CreateOfferRequestTO request = objectMapper.readValue(getCreateOfferTOJsonString(), CreateOfferRequestTO.class);

        //when
        //then
        this.mockMvc.perform(post("/provider/offer").content(RestApiHelper.asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.edcResponseId").value(ProviderServiceFake.CREATE_OFFER_RESPONSE_ID))
            .andExpect(jsonPath("$.fhResponseId").value(ProviderServiceFake.CREATE_OFFER_RESPONSE_ID));

        ArgumentCaptor<CreateFhOfferBE> createDatasetEntryCaptor = ArgumentCaptor.forClass(CreateFhOfferBE.class);
        ArgumentCaptor<CreateEdcOfferBE> createEdcOfferCaptor = ArgumentCaptor.forClass(CreateEdcOfferBE.class);

        verify(providerService).createOffer(createDatasetEntryCaptor.capture(), createEdcOfferCaptor.capture());

        CreateFhOfferBE createFhOfferBE = createDatasetEntryCaptor.getValue();
        CreateEdcOfferBE createEdcOfferBE = createEdcOfferCaptor.getValue();
        //check if request is mapped correctly
        assertThat(request.getPolicy()).usingRecursiveComparison().isEqualTo(createFhOfferBE.getPolicy());
        assertEquals("Test Service Offering", createFhOfferBE.getOfferName());
        assertEquals("This is the service offering description.", createFhOfferBE.getOfferDescription());
        assertThat(request.getPolicy()).usingRecursiveComparison().isEqualTo(createEdcOfferBE.getPolicy());
        assertEquals(request.getFileName(), createEdcOfferBE.getFileName());
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

    String getCreateOfferTOJsonString() {

        return """
            {
                "credentialSubjectList": [
                    {
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
                            "@value": "default: allow intent",
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
                    {
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
                            "@value": "default: allow intent",
                            "@type": "xsd:string"
                        },
                        "id": "urn:uuid:GENERATED_DATA_RESOURCE_ID",
                        "@type": "gx:DataResource"
                    }
                ],
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
