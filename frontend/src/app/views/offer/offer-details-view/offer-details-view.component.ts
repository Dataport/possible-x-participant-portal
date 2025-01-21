import {Component, Input} from '@angular/core';
import {IEnforcementPolicyUnion, IPxExtendedServiceOfferingCredentialSubject} from "../../../services/mgmt/api/backend";

@Component({
  selector: 'app-offer-details-view',
  templateUrl: './offer-details-view.component.html',
  styleUrls: ['./offer-details-view.component.scss']
})
export class OfferDetailsViewComponent {
  @Input() isDataOffering?: boolean = undefined;
  @Input() catalogOffering?: IPxExtendedServiceOfferingCredentialSubject = undefined;
  @Input() enforcementPolicies?: IEnforcementPolicyUnion[] = undefined;
}
