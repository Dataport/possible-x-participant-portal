import {Component, Input} from '@angular/core';
import {IEnforcementPolicy} from "../../../services/mgmt/api/backend";

import {
  isEverythingAllowedPolicy,
  isParticipantRestrictionPolicy,
  asParticipantRestrictionPolicy
} from '../../../utils/policy-utils';
import {NameMappingService} from "../../../services/mgmt/name-mapping.service";

@Component({
  selector: 'app-enforcement-policy-view',
  templateUrl: './enforcement-policy-view.component.html',
  styleUrls: ['./enforcement-policy-view.component.scss']
})
export class EnforcementPolicyViewComponent {

  @Input() enforcementPolicies: IEnforcementPolicy[] = [];

  protected isEverythingAllowedPolicy = isEverythingAllowedPolicy;

  protected isParticipantRestrictionPolicy = isParticipantRestrictionPolicy;

  protected asParticipantRestrictionPolicy = asParticipantRestrictionPolicy;

  constructor(private readonly nameMappingService: NameMappingService) {
  }

  async getNameIdStringById(id: string): Promise<string> {
    const name = await this.nameMappingService.getNameById(id);
    return `${name} (${id})`;
  }

}
