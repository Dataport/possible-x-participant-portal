import {Component, Input} from '@angular/core';
import {IEnforcementPolicy} from "../../../services/mgmt/api/backend";

@Component({
  selector: 'app-enforcement-policy-accordion-view',
  templateUrl: './enforcement-policy-accordion-view.component.html',
  styleUrls: ['./enforcement-policy-accordion-view.component.scss']
})
export class EnforcementPolicyAccordionViewComponent {

  @Input() enforcementPolicies: IEnforcementPolicy[] = [];

  @Input() showValidity: boolean = false;

  constructor() {
  }

  get isAnyPolicyInvalid(): boolean {
    return this.enforcementPolicies.some(policy => !policy.valid)
  }

}
