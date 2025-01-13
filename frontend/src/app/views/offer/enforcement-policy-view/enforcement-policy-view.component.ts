import {Component, Input} from '@angular/core';
import {IEnforcementPolicy} from "../../../services/mgmt/api/backend";

import {
  isEverythingAllowedPolicy,
  isParticipantRestrictionPolicy,
  asParticipantRestrictionPolicy,
  isStartDatePolicy, asStartDatePolicy,
  isEndDatePolicy, asEndDatePolicy
} from '../../../utils/policy-utils';
import {NameMappingService} from "../../../services/mgmt/name-mapping.service";

@Component({
  selector: 'app-enforcement-policy-view',
  templateUrl: './enforcement-policy-view.component.html',
  styleUrls: ['./enforcement-policy-view.component.scss']
})
export class EnforcementPolicyViewComponent {

  @Input() enforcementPolicies: IEnforcementPolicy[] = [];

  @Input() showValidity: boolean = false;

  protected isEverythingAllowedPolicy = isEverythingAllowedPolicy;

  protected isParticipantRestrictionPolicy = isParticipantRestrictionPolicy;

  protected asParticipantRestrictionPolicy = asParticipantRestrictionPolicy;

  protected isStartDatePolicy = isStartDatePolicy;

  protected asStartDatePolicy = asStartDatePolicy;

  protected isEndDatePolicy = isEndDatePolicy;

  protected asEndDatePolicy = asEndDatePolicy;

  constructor(private readonly nameMappingService: NameMappingService) {
  }

  getNameIdStringById(id: string): string {
    const name = this.nameMappingService.getNameById(id);
    return `${name} (${id})`;
  }

  get isAnyPolicyInvalid(): boolean {
    return this.enforcementPolicies.some(policy => !policy.valid)
  }

}
