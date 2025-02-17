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

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommonPortalModuleTest extends GeneralModuleTest {

    @Test
    @WithMockUser(username = "admin")
    void getVersionSucceeds() throws Exception {

        this.mockMvc.perform(get("/common/version").contentType(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().isOk()).andExpect(jsonPath("$.version").value("1.0.0"))
            .andExpect(jsonPath("$.date").value("2024-12-31"));
    }

    @Test
    @WithMockUser(username = "admin")
    void getNameMappingSucceeds() throws Exception {

        this.mockMvc.perform(get("/common/participant/name-mapping")).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1)).andExpect(jsonPath("$",
                Matchers.hasEntry("did:web:portal.dev.possible-x.de:participant:df15587a-0760-32b5-9c42-bb7be66e8076",
                    "EXPECTED_NAME_VALUE")));
    }

}