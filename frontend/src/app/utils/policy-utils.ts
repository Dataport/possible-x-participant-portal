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
