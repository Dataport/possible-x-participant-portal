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

package eu.possiblex.participantportal;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "POSSIBLE-X Participant Portal", description = "Service for enabling a participant of the POSSIBLE-X Dataspace to provide and consume offerings, and to look up their contracts"), tags = {
    @Tag(name = "Common", description = "API for accessing version information of the POSSIBLE-X Participant Portal and the ID to name mapping of the POSSIBLE-X Dataspace participants"),
    @Tag(name = "Shapes", description = "API for accessing the Gaia-X and POSSIBLE-X shapes for offering creation"),
    @Tag(name = "Contract", description = "API for accessing the contracts of the POSSIBLE-X Dataspace participant"),
    @Tag(name = "ProvideOffer", description = "API for providing offerings in the POSSIBLE-X Dataspace"),
    @Tag(name = "ConsumeOffer", description = "API for consuming offerings in the POSSIBLE-X Dataspace") })

@SpringBootApplication
public class ParticipantPortalApplication {

    public static void main(String[] args) {

        SpringApplication.run(ParticipantPortalApplication.class, args);
    }

}
