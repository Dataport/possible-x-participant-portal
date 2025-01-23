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
