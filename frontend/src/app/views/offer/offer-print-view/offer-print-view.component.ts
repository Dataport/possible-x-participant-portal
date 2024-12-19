import {Component, Input} from '@angular/core';

import {
  IOfferDetailsTO
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

  constructor(private datePipe: DatePipe) {}

  getUrnUuid(id: string): string {
    const match = RegExp(/(urn:uuid:.*)/).exec(id);

    if (match) {
      return match[1];
    } else {
      return ""
    }
  }

  getFormattedOfferRetrievalTimestamp(): string {
    return this.datePipe.transform(this.offer?.offerRetrievalDate, 'yyyyMMdd_HHmmss_z') || '';
  }
}
