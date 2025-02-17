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
import {IAgreementOffsetUnit, IEnforcementPolicy} from "../../../../services/mgmt/api/backend";
import {
  asEndDatePolicy,
  asParticipantRestrictionPolicy,
  asStartDatePolicy,
  asEndAgreementOffsetPolicy,
  isEndAgreementOffsetPolicy,
  isEndDatePolicy,
  isEverythingAllowedPolicy,
  isParticipantRestrictionPolicy,
  isStartDatePolicy
} from "../../../../utils/policy-utils";
import {NameMappingService} from "../../../../services/mgmt/name-mapping.service";

@Component({
  selector: 'app-enforcement-policy-content',
  templateUrl: './enforcement-policy-content.component.html',
  styleUrls: ['./enforcement-policy-content.component.scss']
})
export class EnforcementPolicyContentComponent {

  @Input() enforcementPolicy: IEnforcementPolicy = undefined;

  protected readonly asEndDatePolicy = asEndDatePolicy;
  protected readonly asStartDatePolicy = asStartDatePolicy;
  protected readonly isParticipantRestrictionPolicy = isParticipantRestrictionPolicy;
  protected readonly isStartDatePolicy = isStartDatePolicy;
  protected readonly isEverythingAllowedPolicy = isEverythingAllowedPolicy;
  protected readonly isEndDatePolicy = isEndDatePolicy;
  protected readonly asParticipantRestrictionPolicy = asParticipantRestrictionPolicy;
  protected readonly isEndAgreementOffsetPolicy = isEndAgreementOffsetPolicy;
  protected readonly asEndAgreementOffsetPolicy = asEndAgreementOffsetPolicy;

  constructor(private readonly nameMappingService: NameMappingService) {
  }

  getNameIdStringById(id: string): string {
    const name = this.nameMappingService.getNameById(id);
    return `${name} (${id})`;
  }

  getTimeUnitString(timeUnit: IAgreementOffsetUnit): string {
    const timeUnitString = timeUnit as string;
    switch (timeUnitString) {
      case "s":
        return "second(s)";
      case "m":
        return "minute(s)";
      case "h":
        return "hour(s)";
      case "d":
        return "day(s)";
      default:
        return timeUnitString;
    }
  }
}
