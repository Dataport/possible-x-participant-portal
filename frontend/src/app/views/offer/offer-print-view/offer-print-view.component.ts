import {Component, ViewChild, ElementRef, Input} from '@angular/core';

import {
  IOfferDetailsTO, IParticipantDetailsTO,
  IPxExtendedServiceOfferingCredentialSubject
} from "../../../services/mgmt/api/backend";
import {DatePipe} from "@angular/common";
import {
  isEverythingAllowedPolicy,
  isParticipantRestrictionPolicy,
  asParticipantRestrictionPolicy
} from '../../../utils/policy-utils';

@Component({
  selector: 'app-offer-print-view',
  templateUrl: './offer-print-view.component.html',
  styleUrls: ['./offer-print-view.component.scss'],
  providers: [DatePipe]
})
export class OfferPrintViewComponent {
  @Input() offer?: IOfferDetailsTO = undefined;
  @Input() providerDetails?: IParticipantDetailsTO = undefined;
  @Input() printTimestamp?: Date = undefined;
  @ViewChild('modalContent') modalContent: ElementRef;

  constructor(private datePipe: DatePipe) {}

  protected isEverythingAllowedPolicy = isEverythingAllowedPolicy;

  protected isParticipantRestrictionPolicy = isParticipantRestrictionPolicy;

  protected asParticipantRestrictionPolicy = asParticipantRestrictionPolicy;

  containsPII(catalogOffering: IPxExtendedServiceOfferingCredentialSubject): boolean {
    return catalogOffering["gx:aggregationOf"][0]["gx:containsPII"];
  }

  isDprUndefinedOrEmpty(catalogOffering: IPxExtendedServiceOfferingCredentialSubject): boolean {
    return !catalogOffering['gx:dataProtectionRegime'] || catalogOffering['gx:dataProtectionRegime'].length === 0;
  }

  getUrnUuid(id: string): string {
    const match = id.match(/(urn:uuid:.*)/);

    if (match) {
      return match[1];
    } else {
      return ""
    }
  }

  getFormattedTimestamp(date: Date): string {
    return this.datePipe.transform(date, 'yyyyMMdd_HHmmss_z') || '';
  }
}
