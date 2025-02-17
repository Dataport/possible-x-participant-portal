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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = { SdCreationWizardApiServiceTest.TestConfig.class,
    SdCreationWizardApiServiceImpl.class })
class SdCreationWizardApiServiceTest {
    @Autowired
    private SdCreationWizardApiService sut;

    @Test
    void getShapesByExistingEcosystem() {

        Map<String, List<String>> shapes = sut.getShapesByEcosystem("ecosystem1");
        assertNotNull(shapes);
    }

    @Test
    void getShapesByNonExistentEcosystem() {

        WebClientResponseException e = assertThrows(WebClientResponseException.class,
            () -> sut.getShapesByEcosystem("missing"));
        assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
    }

    @Test
    void getOfferingShapesByExistingEcosystem() {

        List<String> shapes = sut.getServiceOfferingShapesByEcosystem("ecosystem1");
        assertNotNull(shapes);
        assertEquals(2, shapes.size());
    }

    @Test
    void getOfferingShapesByNonExistentEcosystem() {

        WebClientResponseException e = assertThrows(WebClientResponseException.class,
            () -> sut.getServiceOfferingShapesByEcosystem("missing"));
        assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
    }

    @Test
    void getResourceShapesByExistingEcosystem() {

        List<String> shapes = sut.getResourceShapesByEcosystem("ecosystem1");
        assertNotNull(shapes);
        assertEquals(1, shapes.size());
    }

    @Test
    void getParticipantShapesByNonExistentEcosystem() {

        WebClientResponseException e = assertThrows(WebClientResponseException.class,
            () -> sut.getResourceShapesByEcosystem("missing"));
        assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
    }

    @Test
    void getShapeFileExisting() {

        String json = sut.getShapeByName("ecosystem", "Resource1.json");
        assertNotNull(json);
    }

    @Test
    void getShapeFileNonExistent() {

        WebClientResponseException e = assertThrows(WebClientResponseException.class,
            () -> sut.getShapeByName("ecosystem", "missing"));
        assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
    }

    // Test-specific configuration to provide a fake implementation of SdCreationWizardApiClient
    @TestConfiguration
    static class TestConfig {
        @Bean
        public SdCreationWizardApiClient sdCreationWizardApiClient() {

            return Mockito.spy(new SdCreationWizardApiClientFake());
        }
    }
}