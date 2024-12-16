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

}
