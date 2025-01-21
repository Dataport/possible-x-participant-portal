import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-service-offering-policy-hints',
  templateUrl: './service-offering-policy-hints.component.html',
  styleUrls: ['./service-offering-policy-hints.component.scss']
})
export class ServiceOfferingPolicyHintsComponent {
  @Input() isOfferingDataOffering?: boolean = undefined;
}
