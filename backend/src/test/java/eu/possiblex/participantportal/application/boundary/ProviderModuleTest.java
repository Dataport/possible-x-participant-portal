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

package eu.possiblex.participantportal.application.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.possiblex.participantportal.application.entity.CreateDataOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.CreateServiceOfferingRequestTO;
import eu.possiblex.participantportal.business.control.EdcClientFake;
import eu.possiblex.participantportal.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This is an integration test that tests as much of the backend as possible. Here, all real components are used from
 * all layers. Only the interface components which connect to other systems are mocked.
 */

class ProviderModuleTest extends GeneralModuleTest {

    private static final String TEST_FILES_PATH = "unit_tests/ModuleTestsCommon/";

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin")
    void shouldReturnMessageOnCreateServiceOfferingWithoutData() throws Exception {

        CreateServiceOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingTOJsonString(),
            CreateServiceOfferingRequestTO.class);

        this.mockMvc.perform(post("/provider/offer/service").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldReturnMessageOnCreateServiceOfferingWithData() throws Exception {

        CreateDataOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingWithDataTOJsonString(),
            CreateDataOfferingRequestTO.class);

        this.mockMvc.perform(post("/provider/offer/data").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldDeleteOfferInFhCatalogOnEdcErrorWhenCreatingServiceOfferingWithoutData() throws Exception {

        CreateServiceOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingTOJsonString(),
            CreateServiceOfferingRequestTO.class);

        request.getServiceOfferingCredentialSubject().setName(EdcClientFake.BAD_GATEWAY_ASSET_ID);

        this.mockMvc.perform(post("/provider/offer/service").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON));

        verify(technicalFhCatalogClient).deleteServiceOfferingFromFhCatalog(any());

    }

    @Test
    @WithMockUser(username = "admin")
    void shouldDeleteOfferInFhCatalogOnEdcErrorWhenCreatingServiceOfferingWithData() throws Exception {

        CreateDataOfferingRequestTO request = objectMapper.readValue(getCreateServiceOfferingWithDataTOJsonString(),
            CreateDataOfferingRequestTO.class);

        request.getServiceOfferingCredentialSubject().setName(EdcClientFake.BAD_GATEWAY_ASSET_ID);

        this.mockMvc.perform(post("/provider/offer/data").content(RestApiHelper.asJsonString(request))
            .contentType(MediaType.APPLICATION_JSON));

        verify(technicalFhCatalogClient).deleteServiceOfferingWithDataFromFhCatalog(any());
    }

    private String getCreateServiceOfferingTOJsonString() {

        return TestUtils.loadTextFile(TEST_FILES_PATH + "serviceOfferingPayload.json");
    }

    private String getCreateServiceOfferingWithDataTOJsonString() {

        return TestUtils.loadTextFile(TEST_FILES_PATH + "serviceOfferingPayloadWithData.json");
    }

}
