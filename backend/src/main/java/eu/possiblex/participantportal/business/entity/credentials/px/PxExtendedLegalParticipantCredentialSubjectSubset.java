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

package eu.possiblex.participantportal.business.entity.credentials.px;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PxExtendedLegalParticipantCredentialSubjectSubset {

    @Getter(AccessLevel.NONE)
    public static final List<String> TYPE = List.of("gx:LegalParticipant",
        "px:PossibleXLegalParticipantExtension");

    @Getter(AccessLevel.NONE)
    public static final Map<String, String> CONTEXT = Map.of("gx",
        "https://w3id.org/gaia-x/development#", "vcard", "http://www.w3.org/2006/vcard/ns#", "xsd",
        "http://www.w3.org/2001/XMLSchema#", "px", "http://w3id.org/gaia-x/possible-x#", "schema",
        "https://schema.org/");

    @JsonProperty("@id")
    private String id;

    @JsonProperty("schema:name")
    private String name;

    @JsonProperty("schema:description")
    private String description;

    @JsonProperty("px:mailAddress")
    private String mailAddress;

}
