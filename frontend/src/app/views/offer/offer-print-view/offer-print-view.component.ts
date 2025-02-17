/*
 *  Copyright 2024-2025 Dataport. All rights reserved. Developed as part of the POSSIBLE project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import {Component, Input} from '@angular/core';

import {IOfferDetailsTO} from "../../../services/mgmt/api/backend";
import {DatePipe} from "@angular/common";


@Component({
  selector: 'app-offer-print-view',
  templateUrl: './offer-print-view.component.html',
  styleUrls: ['./offer-print-view.component.scss'],
  providers: [DatePipe]
})
export class OfferPrintViewComponent {
  @Input() offer?: IOfferDetailsTO = undefined;

  constructor(private readonly datePipe: DatePipe) {
  }


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
