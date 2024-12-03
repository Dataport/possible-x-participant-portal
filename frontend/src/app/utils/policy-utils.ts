import {IEnforcementPolicy, IParticipantRestrictionPolicy} from "../services/mgmt/api/backend";

export const isEverythingAllowedPolicy: (policy: IEnforcementPolicy) => boolean
  = policy => (policy['@type'] === 'EverythingAllowedPolicy');

export const isParticipantRestrictionPolicy: (policy: IEnforcementPolicy) => boolean
  = policy => (policy['@type'] === 'ParticipantRestrictionPolicy');

export const asParticipantRestrictionPolicy: (policy: IEnforcementPolicy) => IParticipantRestrictionPolicy
  = policy => (policy as IParticipantRestrictionPolicy);
