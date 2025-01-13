import {Component, Input} from '@angular/core';
import {IEnforcementPolicy} from "../../../../services/mgmt/api/backend";
import {
  asEndDatePolicy,
  asParticipantRestrictionPolicy,
  asStartDatePolicy,
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

  constructor(private readonly nameMappingService: NameMappingService) {
  }

  getNameIdStringById(id: string): string {
    const name = this.nameMappingService.getNameById(id);
    return `${name} (${id})`;
  }
}
