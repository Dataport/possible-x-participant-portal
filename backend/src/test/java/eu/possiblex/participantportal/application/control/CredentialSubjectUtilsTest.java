package eu.possiblex.participantportal.application.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.application.entity.CreateServiceOfferingRequestTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class CredentialSubjectUtilsTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {

        objectMapper = new ObjectMapper();
    }

    @Test
    @Disabled
    void testFindAllCredentialSubjectsByType() throws JsonProcessingException {

        CreateServiceOfferingRequestTO requestTO = objectMapper.readValue(getCreateOfferTOJsonString(),
            CreateServiceOfferingRequestTO.class);
        //List<PojoCredentialSubject> credentialSubjectList = requestTO.getCredentialSubjectList();

        //List<GxServiceOfferingCredentialSubject> serviceOfferingCredentialSubjects = CredentialSubjectUtils.findAllCredentialSubjectsByType(
        //    GxServiceOfferingCredentialSubject.class, credentialSubjectList);
        //assertThat(serviceOfferingCredentialSubjects).isNotNull();
        //assertThat(serviceOfferingCredentialSubjects).isNotEmpty();

    }

    @Test
    @Disabled
    void testFindFirstCredentialSubjectByType() throws JsonProcessingException {

        CreateServiceOfferingRequestTO requestTO = objectMapper.readValue(getCreateOfferTOJsonString(),
            CreateServiceOfferingRequestTO.class);
        //List<PojoCredentialSubject> credentialSubjectList = requestTO.getCredentialSubjectList();

        //GxServiceOfferingCredentialSubject serviceOfferingCredentialSubject = CredentialSubjectUtils.findFirstCredentialSubjectByType(
        //    GxServiceOfferingCredentialSubject.class, credentialSubjectList);
        //assertThat(serviceOfferingCredentialSubject).isNotNull();

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

}