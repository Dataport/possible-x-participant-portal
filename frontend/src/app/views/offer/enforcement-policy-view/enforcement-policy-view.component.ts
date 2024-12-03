import {Component, Input} from '@angular/core';
import {IEnforcementPolicy, IParticipantRestrictionPolicy} from "../../../services/mgmt/api/backend";

@Component({
  selector: 'app-enforcement-policy-view',
  templateUrl: './enforcement-policy-view.component.html',
  styleUrls: ['./enforcement-policy-view.component.scss']
})
export class EnforcementPolicyViewComponent {

  @Input() enforcementPolicies: IEnforcementPolicy[] = [];

  protected isEverythingAllowedPolicy: (policy: IEnforcementPolicy) => boolean
    = policy => (policy['@type'] === 'EverythingAllowedPolicy');

  protected isParticipantRestrictionPolicy: (policy: IEnforcementPolicy) => boolean
    = policy => (policy['@type'] === 'ParticipantRestrictionPolicy');

  protected asParticipantRestrictionPolicy: (policy: IEnforcementPolicy) => IParticipantRestrictionPolicy
    = policy => (policy as IParticipantRestrictionPolicy);

}
