import {Component, Input} from '@angular/core';
import {
  isEverythingAllowedPolicy,
  isParticipantRestrictionPolicy,
  asParticipantRestrictionPolicy
} from '../../../../utils/policy-utils';
import {
  IEnforcementPolicyUnion,
  IPxExtendedServiceOfferingCredentialSubject
} from "../../../../services/mgmt/api/backend";
import {NameMappingService} from "../../../../services/mgmt/name-mapping.service";

@Component({
  selector: 'app-data-offer-details-view',
  templateUrl: './data-offer-details-view.component.html',
  styleUrls: ['./data-offer-details-view.component.scss']
})
export class DataOfferDetailsViewComponent {
  @Input() catalogOffering?: IPxExtendedServiceOfferingCredentialSubject = undefined;
  @Input() enforcementPolicies?: IEnforcementPolicyUnion[] = undefined;

  protected isEverythingAllowedPolicy = isEverythingAllowedPolicy;

  protected isParticipantRestrictionPolicy = isParticipantRestrictionPolicy;

  protected asParticipantRestrictionPolicy = asParticipantRestrictionPolicy;

  constructor(private readonly nameMappingService: NameMappingService) {
  }

  async getNameById(id: string): Promise<string> {
    return this.nameMappingService.getNameById(id);
  }

  async getNameIdStringById(id: string): Promise<string> {
    const name = await this.getNameById(id);
    return `${name} (${id})`;
  }

}
