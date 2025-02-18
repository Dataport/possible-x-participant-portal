/*
 *  Copyright 2024-2025 Dataport. All rights reserved. Developed as part of the POSSIBLE project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.possiblex.participantportal.business.control;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.business.entity.PrefillFieldsBE;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = { PrefillFieldsTest.TestConfig.class, ProviderServiceImpl.class })
class PrefillFieldsTest {
    @Autowired
    ProviderService providerService;

    @Test
    void testGetPrefillFields() {
        //when
        PrefillFieldsBE prefillFields = providerService.getPrefillFields();

        //then
        String expectedId = "did:web:test.eu";
        String expectedServiceOfferingName = "Data Product Service for Data Resource <Data resource name>";
        String expectedServiceOfferingDescription = "Data Product Service provides data (<Data resource name>).";
        assertEquals(expectedId, prefillFields.getParticipantId());
        assertEquals(expectedServiceOfferingName, prefillFields.getDataServiceOfferingPrefillFields().getServiceOfferingName());
        assertEquals(expectedServiceOfferingDescription,
            prefillFields.getDataServiceOfferingPrefillFields().getServiceOfferingDescription());
    }

    @Nested
    @TestPropertySource(properties = "prefill-fields.data-product.json-file-path=")
    class PrefillFieldsEmptyPathTest {
        @Test
        void testGetPrefillFields() {
            // the only difference is the path to the json file from the properties
            PrefillFieldsTest.this.testGetPrefillFields();
        }
    }

    @Nested
    @TestPropertySource(properties = "prefill-fields.data-product.json-file-path=non_existent.json")
    class PrefillFieldsNonExistingPathTest {
        @Test
        void testGetPrefillFields() {
            // the only difference is the path to the json file from the properties
            PrefillFieldsTest.this.testGetPrefillFields();
        }
    }

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
        public EnforcementPolicyParserService enforcementPolicyParserService() {

            return Mockito.spy(new EnforcementPolicyParserServiceFake());
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
