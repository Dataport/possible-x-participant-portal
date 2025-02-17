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

import {Component, Input, ViewChild} from '@angular/core';
import {DatePipe} from "@angular/common";
import {IContractDetailsTO} from "../../../services/mgmt/api/backend";
import {StatusMessageComponent} from "../../common-views/status-message/status-message.component";

@Component({
  selector: 'app-contract-details-export-view',
  templateUrl: './contract-details-export-view.component.html',
  styleUrls: ['./contract-details-export-view.component.scss'],
  providers: [DatePipe]
})
export class ContractDetailsExportViewComponent {

  @Input() contractDetails?: IContractDetailsTO = undefined;
  @ViewChild('informationRetrievalStatusMessage') public informationRetrievalStatusMessage!: StatusMessageComponent;

  constructor(private readonly datePipe: DatePipe) {
  }

  getFormattedContractSigningTimestamp(): string {
    return this.datePipe.transform(this.contractDetails?.contractSigningDate, 'yyyyMMdd_HHmmss_z') || '';
  }

}
