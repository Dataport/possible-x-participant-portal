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

import {Component, ViewChild} from '@angular/core';
import {IAcceptOfferResponseTO, IOfferDetailsTO} from '../../../services/mgmt/api/backend';
import {MatStepper} from "@angular/material/stepper";
import {SelectComponent} from "../select/select.component";
import {AcceptComponent} from "../accept/accept.component";
import {TransferComponent} from "../transfer/transfer.component";

@Component({
  selector: 'app-consume',
  templateUrl: './consume.component.html',
  styleUrls: ['./consume.component.scss']
})
export class ConsumeComponent {
  @ViewChild("stepper") stepper: MatStepper;
  @ViewChild("select") select: SelectComponent;
  @ViewChild("accept") accept: AcceptComponent;
  @ViewChild("transfer") transfer: TransferComponent;
  selectedOffer?: IOfferDetailsTO = undefined;
  negotiatedContract?: IAcceptOfferResponseTO = undefined;

  setSelectedOffer(offer: IOfferDetailsTO): void {
    this.selectedOffer = offer;
    this.stepper.next();
  }

  setNegotiatedContract(contract: IAcceptOfferResponseTO): void {
    this.negotiatedContract = contract;
    this.stepper.next();
  }

  resetSelection() {
    this.selectedOffer = undefined;
    this.negotiatedContract = undefined;
    this.select.queryCatalogStatusMessage.hideAllMessages();
    this.select.selectionForm.reset();
    this.accept.acceptOfferStatusMessage.hideAllMessages();
    this.transfer.dataTransferStatusMessage.hideAllMessages();
    this.stepper.reset();
  }
}
