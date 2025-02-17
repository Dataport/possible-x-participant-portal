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

import {Component, Input} from '@angular/core';
import {
  isEverythingAllowedPolicy,
  isParticipantRestrictionPolicy,
  asParticipantRestrictionPolicy,
  isStartDatePolicy, asStartDatePolicy,
  isEndDatePolicy, asEndDatePolicy
} from '../../../../utils/policy-utils';
import {
  IEnforcementPolicyUnion,
  IPxExtendedServiceOfferingCredentialSubject
} from "../../../../services/mgmt/api/backend";
import {NameMappingService} from "../../../../services/mgmt/name-mapping.service";

@Component({
  selector: 'app-service-offer-details-view',
  templateUrl: './service-offer-details-view.component.html',
  styleUrls: ['./service-offer-details-view.component.scss']
})
export class ServiceOfferDetailsViewComponent {
  @Input() catalogOffering?: IPxExtendedServiceOfferingCredentialSubject = undefined;
  @Input() enforcementPolicies?: IEnforcementPolicyUnion[] = undefined;

  protected isEverythingAllowedPolicy = isEverythingAllowedPolicy;

  protected isParticipantRestrictionPolicy = isParticipantRestrictionPolicy;

  protected asParticipantRestrictionPolicy = asParticipantRestrictionPolicy;

  protected isStartDatePolicy = isStartDatePolicy;

  protected asStartDatePolicy = asStartDatePolicy;

  protected isEndDatePolicy = isEndDatePolicy;

  protected asEndDatePolicy = asEndDatePolicy;

  constructor(private readonly nameMappingService: NameMappingService) {
  }

  getNameById(id: string): string {
    return this.nameMappingService.getNameById(id);
  }

  getNameIdStringById(id: string): string {
    const name = this.getNameById(id);
    return `${name} (${id})`;
  }
}
