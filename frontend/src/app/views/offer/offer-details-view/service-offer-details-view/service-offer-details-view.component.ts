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
  selector: 'app-service-offer-details-view',
  templateUrl: './service-offer-details-view.component.html',
  styleUrls: ['./service-offer-details-view.component.scss']
})
export class ServiceOfferDetailsViewComponent {
  @Input() catalogOffering?: IPxExtendedServiceOfferingCredentialSubject = undefined;
  @Input() enforcementPolicies?: IEnforcementPolicyUnion[] = undefined;

  protected isEverythingAllowedPolicy = isEverythingAllowedPolicy;

  protected isParticipantRestrictionPolicy = isParticipantRestrictionPolicy;

  protected asParticipantRestrictionPolicy = asParticipantRestrictionPolicy;

  constructor(private readonly nameMappingService: NameMappingService) {
  }

  getNameById(id: string): string {
    return this.nameMappingService.getNameById(id);
  }

  getNameIdStringById(id: string): string {
    const name = this.getNameById(id);
    return `${name} (${id})`;
  }
}
