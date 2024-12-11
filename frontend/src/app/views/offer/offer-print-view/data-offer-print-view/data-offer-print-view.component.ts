import {Component, Input} from '@angular/core';
import {IOfferDetailsTO, IPxExtendedServiceOfferingCredentialSubject} from "../../../../services/mgmt/api/backend";
import {
  isEverythingAllowedPolicy,
  isParticipantRestrictionPolicy,
  asParticipantRestrictionPolicy
} from '../../../../utils/policy-utils';

@Component({
  selector: 'app-data-offer-print-view',
  templateUrl: './data-offer-print-view.component.html',
  styleUrls: ['./data-offer-print-view.component.scss']
})
export class DataOfferPrintViewComponent {
  @Input() offer?: IOfferDetailsTO = undefined;

  protected isEverythingAllowedPolicy = isEverythingAllowedPolicy;

  protected isParticipantRestrictionPolicy = isParticipantRestrictionPolicy;

  protected asParticipantRestrictionPolicy = asParticipantRestrictionPolicy;

  containsPII(catalogOffering: IPxExtendedServiceOfferingCredentialSubject): boolean {
    return catalogOffering["gx:aggregationOf"][0]["gx:containsPII"];
  }

  isDprUndefinedOrEmpty(catalogOffering: IPxExtendedServiceOfferingCredentialSubject): boolean {
    return !catalogOffering['gx:dataProtectionRegime'] || catalogOffering['gx:dataProtectionRegime'].length === 0;
  }

  getParticipantName(id: string) {
    return this.offer?.participantNames[id].participantName || '';
  }

}
