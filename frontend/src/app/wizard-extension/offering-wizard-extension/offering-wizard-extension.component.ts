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
import { ICredentialSubject, IGxServiceOfferingCs, IGxDataResourceCs } from '../../views/offer/offer-data';
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

  public prefillDone: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  protected fileName: string = "";
  protected policyMap = POLICY_MAP;
  protected policy: string = "";
  protected isDataOffering: boolean = true;

  constructor(
    private apiService: ApiService
  ) { }

  public async loadShape(offerType: string, serviceOfferingId: string, dataResourceId: string): Promise<void> {
    this.isDataOffering = offerType === "data";
    
    this.prefillDone.next(false);
    console.log("Loading service offering shape"); 
    await this.gxServiceOfferingWizard.loadShape(this.apiService.getGxServiceOfferingShape(), serviceOfferingId);

    if(this.isOfferingDataOffering()) {
      console.log("Loading data resource shape"); 
      await this.gxDataResourceWizard.loadShape(this.apiService.getGxDataResourceShape(), dataResourceId);
    }
  }

  public isShapeLoaded(): boolean {
    return this.gxServiceOfferingWizard?.isShapeLoaded() && this.isOfferingDataOffering() ? this.gxDataResourceWizard?.isShapeLoaded() : true;
  }

  private prefillHandleCs(cs: ICredentialSubject) {
    if (isGxServiceOfferingCs(cs)) {
      this.gxServiceOfferingWizard.prefillFields(cs, ["gx:providedBy"]);
    }
    if (isDataResourceCs(cs)) {
      this.gxDataResourceWizard.prefillFields(cs, ["gx:producedBy", "gx:exposedThrough", "gx:copyrightOwnedBy"]);
    }
  }

  public prefillFields(csList: any) {
    for (let cs of csList) {
      this.prefillHandleCs(cs)
    }

    if (!this.isOfferingDataOffering()) {
      this.gxServiceOfferingWizard.prefillDone
      .pipe(
        takeWhile(done => !done, true)
      )
      .subscribe(done => {
        if (done) {
          this.prefillDone.next(true);
        }
      });
    } else {
      this.gxServiceOfferingWizard.prefillDone
      .pipe(
        takeWhile(done => !done, true)
      )
      .subscribe(done => {
        if (done) {
          this.gxDataResourceWizard.prefillDone
          .pipe(
            takeWhile(done => !done, true)
          )
          .subscribe(done => {
            if (done) {
              this.prefillDone.next(true);
            }
          })
        }
      });
    }
  }

  async createOffer() {
    console.log("Create offer.");
    this.offerCreationStatusMessage.hideAllMessages();

    let gxOfferingJsonSd: IGxServiceOfferingCs = this.gxServiceOfferingWizard.generateJsonCs();
    

    let createOfferTo: any = {
        credentialSubjectList: [
          gxOfferingJsonSd,
        ],
        fileName: this.fileName,
        policy: this.policyMap[this.policy].policy
    }

    if (this.isOfferingDataOffering()) {
      let gxDataResourceJsonSd: IGxDataResourceCs = this.gxDataResourceWizard.generateJsonCs();
      createOfferTo.credentialSubjectList.push(gxDataResourceJsonSd);
    }
    
    //this.apiService.createOffer(createOfferTo).then(response => {
    //  console.log(response);
    //  this.offerCreationStatusMessage.showSuccessMessage("", 20000);
    //}).catch((e: HttpErrorResponse) => {
    //  this.offerCreationStatusMessage.showErrorMessage(e.error.detail);
   // }).catch(_ => {
    //  this.offerCreationStatusMessage.showErrorMessage("Unbekannter Fehler");
    //});
    console.log(createOfferTo);
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
      || this.isOfferingDataOffering() ? this.gxDataResourceWizard?.isWizardFormInvalid() : false;
  }

  protected isOfferingDataOffering() {
    return this.isDataOffering;
  }

  protected isPossibleSpecificFormValid() {
    return this.isFieldFilled(this.policy) && this.isFieldFilled(this.fileName)
  }

  public isFieldFilled(str: string){
    if (!str || str.trim().length === 0) {
      return false;
    }

    return true;
  }
}
