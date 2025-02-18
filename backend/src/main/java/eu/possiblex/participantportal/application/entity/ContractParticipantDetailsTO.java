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

package eu.possiblex.participantportal.application.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractParticipantDetailsTO {
    @Schema(description = "Name of the participant", example = "Some Organization Ltd.")
    private String name;

    @Schema(description = "(D)ID of the participant", example = "did:web:example.com:participant:someorgltd")
    private String did;

    @Schema(description = "DAPS ID of the participant", example = "06:B2:4A:FD:8D:E9:38:C8:38:88:0C:C5:FE:15:15:BD:3C:DA:47:EE:keyid:06:B2:4A:FD:8D:E9:38:C8:38:88:0C:C5:FE:15:15:BD:3C:DA:47:EE")
    private String dapsId;
}
