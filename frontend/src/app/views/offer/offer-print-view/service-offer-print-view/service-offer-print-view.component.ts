import {Component, Input} from '@angular/core';
import {IOfferDetailsTO, IPxExtendedServiceOfferingCredentialSubject} from "../../../../services/mgmt/api/backend";
import {
  isEverythingAllowedPolicy,
  isParticipantRestrictionPolicy,
  asParticipantRestrictionPolicy
} from '../../../../utils/policy-utils';

@Component({
  selector: 'app-service-offer-print-view',
  templateUrl: './service-offer-print-view.component.html',
  styleUrls: ['./service-offer-print-view.component.scss']
})
export class ServiceOfferPrintViewComponent {
  @Input() offer?: IOfferDetailsTO = undefined;

  protected isEverythingAllowedPolicy = isEverythingAllowedPolicy;

  protected isParticipantRestrictionPolicy = isParticipantRestrictionPolicy;

  protected asParticipantRestrictionPolicy = asParticipantRestrictionPolicy;

  isDprUndefinedOrEmpty(catalogOffering: IPxExtendedServiceOfferingCredentialSubject): boolean {
    return !catalogOffering['gx:dataProtectionRegime'] || catalogOffering['gx:dataProtectionRegime'].length === 0;
  }

  getParticipantName(id: string) {
    return this.offer?.participantNames[id].participantName || '';
  }

}
