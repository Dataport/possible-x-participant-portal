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

import {
  IEndAgreementOffsetPolicy,
  IEndDatePolicy,
  IEnforcementPolicy,
  IParticipantRestrictionPolicy,
  IStartDatePolicy
} from "../services/mgmt/api/backend";

export const isEverythingAllowedPolicy: (policy: IEnforcementPolicy) => boolean
  = policy => (policy['@type'] === 'EverythingAllowedPolicy');

export const isParticipantRestrictionPolicy: (policy: IEnforcementPolicy) => boolean
  = policy => (policy['@type'] === 'ParticipantRestrictionPolicy');

export const asParticipantRestrictionPolicy: (policy: IEnforcementPolicy) => IParticipantRestrictionPolicy
  = policy => (policy as IParticipantRestrictionPolicy);

export const isStartDatePolicy: (policy: IEnforcementPolicy) => boolean
  = policy => (policy['@type'] === 'StartDatePolicy');

export const asStartDatePolicy: (policy: IEnforcementPolicy) => IStartDatePolicy
  = policy => (policy as IStartDatePolicy);

export const isEndDatePolicy: (policy: IEnforcementPolicy) => boolean
  = policy => (policy['@type'] === 'EndDatePolicy');

export const asEndDatePolicy: (policy: IEnforcementPolicy) => IEndDatePolicy
  = policy => (policy as IEndDatePolicy);

export const isEndAgreementOffsetPolicy: (policy: IEnforcementPolicy) => boolean
  = policy => (policy['@type'] === 'EndAgreementOffsetPolicy');

export const asEndAgreementOffsetPolicy: (policy: IEnforcementPolicy) => IEndAgreementOffsetPolicy
  = policy => (policy as IEndAgreementOffsetPolicy);
