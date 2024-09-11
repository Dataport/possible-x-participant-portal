/*
 *  Copyright 2024 Dataport. All rights reserved. Developed as part of the MERLOT project.
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

import { Component, EventEmitter, ViewChild } from '@angular/core';
import { StatusMessageComponent } from '../../views/common-views/status-message/status-message.component';
import { HttpErrorResponse } from '@angular/common/http';
import { BaseWizardExtensionComponent } from '../base-wizard-extension/base-wizard-extension.component';
import { ICredentialSubject, IGxServiceOfferingCs, TBR_ID } from '../../views/offer/offer-data';
import { isGxServiceOfferingCs, isDataResourceCs } from '../../utils/credential-utils';
import { BehaviorSubject, takeWhile } from 'rxjs';
import { ApiService } from '../../services/mgmt/api/api.service';
import { POLICY_MAP } from '../../constants';


@Component({
  selector: 'app-offering-wizard-extension',
  templateUrl: './offering-wizard-extension.component.html',
  styleUrls: ['./offering-wizard-extension.component.scss']
})
export class OfferingWizardExtensionComponent {
  @ViewChild("gxServiceOfferingWizard") private gxServiceOfferingWizard: BaseWizardExtensionComponent;
  @ViewChild("gxDataResourceWizard") private gxDataResourceWizard: BaseWizardExtensionComponent;
  @ViewChild("offerCreationStatusMessage") public offerCreationStatusMessage!: StatusMessageComponent;

  public submitCompleteEvent: EventEmitter<any> = new EventEmitter();

  public prefillDone: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  fileName: string = "";
  policyMap = POLICY_MAP;
  policy: string = "";
  isDataOffering: boolean;

  constructor(
    private apiService: ApiService
  ) { }


  public async loadShape(offerType: string, id: string): Promise<void> {
    this.isDataOffering = offerType === "data";
    
    this.prefillDone.next(false);
    console.log("Loading shape"); 
    await this.gxServiceOfferingWizard.loadShape(this.apiService.getGxServiceOfferingShape(), id);
    if(this.isDataOffering) {
      await this.gxDataResourceWizard.loadShape(this.apiService.getGxDataResourceShape(), id);
    }
  }

  public isShapeLoaded(): boolean {
    return this.gxServiceOfferingWizard?.isShapeLoaded() && this.isDataOffering ? this.gxDataResourceWizard?.isShapeLoaded() : true;
  }

  private prefillHandleCs(cs: ICredentialSubject) {
    if (isGxServiceOfferingCs(cs)) {
      this.gxServiceOfferingWizard.prefillFields(cs, ["gx:providedBy"]);
    }
    if (isDataResourceCs(cs)) {
      this.gxDataResourceWizard.prefillFields(cs, []);
    }
  }

  public prefillFields(offering: any) {
    for (let vc of offering.selfDescription.verifiableCredential) {
      this.prefillHandleCs(vc.credentialSubject)
    }

    this.gxServiceOfferingWizard.prefillDone
      .pipe(
        takeWhile(done => !done, true)
      )
      .subscribe(done => {
        if (done) {
          this.prefillDone.next(true);
        }
      });
  }

  async createOffer() {
    console.log("Create offer.");
    this.offerCreationStatusMessage.hideAllMessages();

    let gxOfferingJsonSd: IGxServiceOfferingCs = this.gxServiceOfferingWizard.generateJsonCs();

    let offeringDto: any = {
      selfDescription: {
        verifiableCredential: [
          { credentialSubject: gxOfferingJsonSd },
        ],
        id: ''
      }
    }

    if (gxOfferingJsonSd.id === TBR_ID) {
      this.apiService.createOffer(offeringDto).then(response => {
        console.log(response);
        this.offerCreationStatusMessage.showSuccessMessage("", 20000);
        this.submitCompleteEvent.emit(null);
      }).catch((e: HttpErrorResponse) => {
        this.offerCreationStatusMessage.showErrorMessage(e.error.detail);
      }).catch(_ => {
        this.offerCreationStatusMessage.showErrorMessage("Unbekannter Fehler");
      });
    }
  }

  protected getPolicyNames() {
    return Object.keys(this.policyMap);
  }

  protected getPolicyDetails(policy: string): string {
    const policyDetails = this.policyMap[policy];
    return policyDetails ? JSON.stringify(policyDetails, null, 2) : '';
  }

  public ngOnDestroy() {
    this.gxServiceOfferingWizard.ngOnDestroy();
    this.offerCreationStatusMessage.hideAllMessages();
  }

  protected isWizardFormInvalid(): boolean {
    return this.gxServiceOfferingWizard?.isWizardFormInvalid()
  }
}
