import {Component, ViewChild} from '@angular/core';
import {
  OfferingWizardExtensionComponent
} from '../../../wizard-extension/offering-wizard-extension/offering-wizard-extension.component';

@Component({
  selector: 'app-provide',
  templateUrl: './provide.component.html',
  styleUrls: ['./provide.component.scss']
})
export class ProvideComponent {
  @ViewChild("wizardExtension") wizardExtension: OfferingWizardExtensionComponent;

  constructor() {
  }
}
