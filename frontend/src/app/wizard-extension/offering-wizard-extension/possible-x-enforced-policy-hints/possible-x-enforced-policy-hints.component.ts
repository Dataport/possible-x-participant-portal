import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-possible-x-enforced-policy-hints',
  templateUrl: './possible-x-enforced-policy-hints.component.html',
  styleUrls: ['./possible-x-enforced-policy-hints.component.scss']
})
export class PossibleXEnforcedPolicyHintsComponent {
  @Input() isOfferingDataOffering?: boolean = undefined;
}
