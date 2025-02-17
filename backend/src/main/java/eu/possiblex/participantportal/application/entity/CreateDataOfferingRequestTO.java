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

import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxLegitimateInterest;
import eu.possiblex.participantportal.application.entity.credentials.gx.resources.GxDataResourceCredentialSubject;
import eu.possiblex.participantportal.application.entity.validation.ValidLegitimateInterestForPII;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString(callSuper = true)
@ValidLegitimateInterestForPII
public class CreateDataOfferingRequestTO extends CreateServiceOfferingRequestTO {
    @Valid
    @NotNull(message = "Data resource credential subject is required")
    private GxDataResourceCredentialSubject dataResourceCredentialSubject;

    @NotBlank(message = "File name is required")
    private String fileName;

    @Valid
    private GxLegitimateInterest legitimateInterest;
}
