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

package eu.possiblex.participantportal.business.entity.edc.asset.possible;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.possiblex.participantportal.application.entity.credentials.gx.datatypes.NodeKindIRITypeId;
import eu.possiblex.participantportal.business.entity.edc.asset.AssetProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PossibleAssetProperties extends AssetProperties {

    private String offerId;

    @JsonProperty("https://w3id.org/gaia-x/development#providedBy")
    private NodeKindIRITypeId providedBy;

    @JsonProperty("https://w3id.org/gaia-x/development#termsAndConditions")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<PossibleAssetTnC> termsAndConditions;

    @JsonProperty("https://w3id.org/gaia-x/development#dataProtectionRegime")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> dataProtectionRegime;

    @JsonProperty("https://w3id.org/gaia-x/development#dataAccountExport")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<PossibleAssetDataAccountExport> dataAccountExport;

    @JsonProperty("https://w3id.org/gaia-x/development#license")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> license;

    @JsonProperty("https://w3id.org/gaia-x/development#copyrightOwnedBy")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> copyrightOwnedBy;

    @JsonProperty("https://w3id.org/gaia-x/development#producedBy")
    private NodeKindIRITypeId producedBy;

    @JsonProperty("https://w3id.org/gaia-x/development#exposedThrough")
    private NodeKindIRITypeId exposedThrough;

    @JsonProperty("https://w3id.org/gaia-x/development#containsPII")
    private boolean containsPII;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> offeringPolicy;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> dataPolicy;
}
