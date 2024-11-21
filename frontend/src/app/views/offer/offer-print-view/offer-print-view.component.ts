import {Component, ViewChild, ElementRef, Input} from '@angular/core';

import {
  IContractPartiesTO,
  IOfferDetailsTO,
  IPxExtendedServiceOfferingCredentialSubject
} from "../../../services/mgmt/api/backend";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-offer-print-view',
  templateUrl: './offer-print-view.component.html',
  styleUrls: ['./offer-print-view.component.scss'],
  providers: [DatePipe]
})
export class OfferPrintViewComponent {
  @Input() offer?: IOfferDetailsTO = undefined;
  @Input() contractParties?: IContractPartiesTO = undefined;
  @Input() printTimestamp?: Date = undefined;
  @ViewChild('modalContent') modalContent: ElementRef;

  constructor(private datePipe: DatePipe) {}

  containsPII(catalogOffering: IPxExtendedServiceOfferingCredentialSubject): boolean {
    return catalogOffering["gx:aggregationOf"][0]["gx:containsPII"];
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
