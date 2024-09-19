/*
 *  Copyright 2024 Dataport. All rights reserved. Developed as part of the MERLOT project.
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

package eu.possiblex.participantportal.business.entity.selfdescriptions;

import com.fasterxml.jackson.annotation.*;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.resources.GxDataResourceCredentialSubject;
import eu.possiblex.participantportal.business.entity.selfdescriptions.gx.serviceofferings.GxServiceOfferingCredentialSubject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = UnknownCredentialSubject.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = GxDataResourceCredentialSubject.class, name = GxDataResourceCredentialSubject.TYPE),
    @JsonSubTypes.Type(value = GxServiceOfferingCredentialSubject.class, name = GxServiceOfferingCredentialSubject.TYPE),
})
public abstract class PojoCredentialSubject {
    // base fields
    private String id;
}

