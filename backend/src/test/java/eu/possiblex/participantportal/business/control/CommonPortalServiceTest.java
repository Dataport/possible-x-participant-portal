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
import org.springframework.test.context.ContextConfiguration;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;

@SpringBootTest
@ContextConfiguration(classes = {CommonPortalServiceTest.TestConfig.class, CommonPortalServiceImpl.class})
class CommonPortalServiceTest {
    @Autowired
    private CommonPortalService sut;

    @Autowired
    private FhCatalogClient fhCatalogClient;

    @Test
    void getNameMapping() {
        reset(fhCatalogClient);

        // WHEN

        Map<String, String> response = sut.getNameMapping();

        // THEN

        Mockito.verify(fhCatalogClient).getParticipantDetails();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertNotNull(response.get(OmejdnConnectorApiClientFake.PARTICIPANT_ID));
        assertEquals(OmejdnConnectorApiClientFake.PARTICIPANT_NAME, response.get(OmejdnConnectorApiClientFake.PARTICIPANT_ID));
    }

    // Test-specific configuration to provide mocks
    @TestConfiguration
    static class TestConfig {
        @Bean
        public FhCatalogClient fhCatalogClient() {
            return Mockito.spy(new FhCatalogClientFake());
        }
    }

}