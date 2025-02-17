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
import {IEnforcementPolicy} from "../../../services/mgmt/api/backend";

@Component({
  selector: 'app-enforcement-policy-accordion-view',
  templateUrl: './enforcement-policy-accordion-view.component.html',
  styleUrls: ['./enforcement-policy-accordion-view.component.scss']
})
export class EnforcementPolicyAccordionViewComponent {

  @Input() enforcementPolicies: IEnforcementPolicy[] = [];

  @Input() showValidity: boolean = false;

  constructor() {
  }

  get isAnyPolicyInvalid(): boolean {
    return this.enforcementPolicies.some(policy => !policy.valid)
  }

}
