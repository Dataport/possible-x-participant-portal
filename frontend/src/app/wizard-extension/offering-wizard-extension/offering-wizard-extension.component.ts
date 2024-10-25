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

import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {StatusMessageComponent} from '../../views/common-views/status-message/status-message.component';
import {BaseWizardExtensionComponent} from '../base-wizard-extension/base-wizard-extension.component';
import {isDataResourceCs, isGxServiceOfferingCs} from '../../utils/credential-utils';
import {BehaviorSubject, takeWhile} from 'rxjs';
import {HttpErrorResponse} from '@angular/common/http';
import {ApiService} from '../../services/mgmt/api/api.service';
import {
  IEverythingAllowedPolicy,
  IGxDataResourceCredentialSubject,
  IGxServiceOfferingCredentialSubject,
  INodeKindIRITypeId,
  IParticipantRestrictionPolicy,
  IPojoCredentialSubject
} from '../../services/mgmt/api/backend';
import {TBR_DATA_RESOURCE_ID, TBR_SERVICE_OFFERING_ID} from "../../views/offer/offer-data";
import {MatStepper} from "@angular/material/stepper";

@Component({
  selector: 'app-offering-wizard-extension',
  templateUrl: './offering-wizard-extension.component.html',
  styleUrls: ['./offering-wizard-extension.component.scss']
})
export class OfferingWizardExtensionComponent implements AfterViewInit {
  @ViewChild("offerCreationStatusMessage") public offerCreationStatusMessage!: StatusMessageComponent;
  selectedFileName: string = "";
  public prefillDone: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  isPolicyChecked: boolean = false;
  dapsIDs: string[] = [''];
  protected isDataOffering: boolean = true;
  @ViewChild("gxServiceOfferingWizard") private gxServiceOfferingWizard: BaseWizardExtensionComponent;
  @ViewChild("gxDataResourceWizard") private gxDataResourceWizard: BaseWizardExtensionComponent;
  waitingForResponse = true;
  offerType: string = "data";
  participantId = "";
  @ViewChild("stepper") stepper: MatStepper;

  constructor(
    private apiService: ApiService
  ) {
  }

  ngAfterViewInit(): void {
    this.prefillWizardNewOffering();
  }

  get isInvalidFileName(): boolean {
    return !this.isFieldFilled(this.selectedFileName);
  }

  get isInvalidPolicy(): boolean {
    return this.isPolicyChecked && this.dapsIDs.some(id => !this.isFieldFilled(id));
  }

  public async loadShape(offerType: string, serviceOfferingId: string, dataResourceId: string): Promise<void> {
    this.prefillDone.next(false);
    console.log("Loading service offering shape");
    let serviceOfferingShapeSource = await this.apiService.getGxServiceOfferingShape();
    serviceOfferingShapeSource = this.adaptGxShape(serviceOfferingShapeSource, "ServiceOffering", ["policy", "aggregationOf"]);
    await this.gxServiceOfferingWizard.loadShape(Promise.resolve(serviceOfferingShapeSource), serviceOfferingId);

    if (this.isOfferingDataOffering()) {
      console.log("Loading data resource shape");
      let dataResourceShapeSource = await this.apiService.getGxDataResourceShape();
      dataResourceShapeSource = this.adaptGxShape(dataResourceShapeSource, "DataResource",
        ["name", "description", "policy", "exposedThrough"]);
      await this.gxDataResourceWizard.loadShape(Promise.resolve(dataResourceShapeSource), dataResourceId);
    }
  }

  public isShapeLoaded(): boolean {
    return this.gxServiceOfferingWizard?.isShapeLoaded() && this.isOfferingDataOffering() ? this.gxDataResourceWizard?.isShapeLoaded() : true;
  }

  public prefillFields(csList: IPojoCredentialSubject[]) {
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
    this.waitingForResponse = true;
    this.offerCreationStatusMessage.showInfoMessage();

    let gxOfferingJsonSd: IGxServiceOfferingCredentialSubject = this.gxServiceOfferingWizard.generateJsonCs();
    gxOfferingJsonSd["gx:policy"] = [""];

    let policy: IParticipantRestrictionPolicy | IEverythingAllowedPolicy;

    if (this.isPolicyChecked) {
      policy = {
        "@type": "ParticipantRestrictionPolicy",
        allowedParticipants: this.dapsIDs
      } as IParticipantRestrictionPolicy;
    } else {
      policy = {
        "@type": "EverythingAllowedPolicy"
      } as IEverythingAllowedPolicy;
    }

    let createOfferTo: any = {
      serviceOfferingCredentialSubject: gxOfferingJsonSd,
      enforcementPolicies: [
        policy
      ]
    };

    let createOfferMethod: (offer: any) => Promise<any>;
    createOfferMethod = this.apiService.createServiceOffering.bind(this.apiService)

    if (this.isOfferingDataOffering()) {
      let gxDataResourceJsonSd: IGxDataResourceCredentialSubject = this.gxDataResourceWizard.generateJsonCs();
      gxDataResourceJsonSd["gx:name"] = gxOfferingJsonSd["gx:name"];
      gxDataResourceJsonSd["gx:description"] = gxOfferingJsonSd["gx:description"];
      gxDataResourceJsonSd["gx:policy"] = gxOfferingJsonSd["gx:policy"];
      gxDataResourceJsonSd["gx:exposedThrough"] = {id: gxOfferingJsonSd.id} as INodeKindIRITypeId;

      createOfferTo.dataResourceCredentialSubject = gxDataResourceJsonSd;
      createOfferTo.fileName = this.selectedFileName;

      createOfferMethod = this.apiService.createDataOffering.bind(this.apiService);
    }

    console.log(createOfferTo);

    createOfferMethod(createOfferTo).then(response => {
      console.log(response);
      this.waitingForResponse = false;
      this.offerCreationStatusMessage.showSuccessMessage("");
    }).catch((e: HttpErrorResponse) => {
      this.waitingForResponse = false;
      this.offerCreationStatusMessage.showErrorMessage(e.error.detail || e.error || e.message);
    }).catch(_ => {
      this.waitingForResponse = false;
      this.offerCreationStatusMessage.showErrorMessage("Unbekannter Fehler");
    });

  }

  public ngOnDestroy() {
    this.gxServiceOfferingWizard.ngOnDestroy();
    this.gxDataResourceWizard.ngOnDestroy();
    this.resetPossibleSpecificFormValues();
    this.offerCreationStatusMessage.hideAllMessages();
  }

  public isFieldFilled(str: string) {
    if (!str || str.trim().length === 0) {
      return false;
    }

    return true;
  }

  public resetPossibleSpecificFormValues() {
    this.selectedFileName = "";
    this.isPolicyChecked = false;
    this.dapsIDs = [''];
  }

  addInput(): void {
    this.dapsIDs.push('');
  }

  removeInput(index: number): void {
    if (this.dapsIDs.length > 1) {
      this.dapsIDs.splice(index, 1);
    }
  }

  public customTrackBy(index: number, obj: any): any {
    return index;
  }

  protected isOfferingDataOffering() {
    return this.offerType === "data";
  }

  protected isDataResourceValid(): boolean {
    return !this.gxDataResourceWizard?.isWizardFormInvalid() && !this.isInvalidFileName;
  }

  protected isServiceOfferingValid(): boolean {
    return !this.gxServiceOfferingWizard?.isWizardFormInvalid() && !this.isInvalidPolicy;
  }

  protected adaptGxShape(shapeSource: any, shapeName: string, excludedFields: string[]) {
    if (typeof shapeSource !== 'object' || shapeSource === null) {
      console.error("Invalid input: shape is not of expected type.");
      return null;
    }

    shapeSource.shapes.forEach((shape: any) => {
      if (shape.targetClassName === shapeName) {
        shape.constraints = shape.constraints.filter((constraint: any) => {
          return !(constraint.path.prefix === "gx" && excludedFields.includes(constraint.path.value));
        });
      }
    });

    console.log(shapeSource);
    return shapeSource;
  }

  private prefillHandleCs(cs: IPojoCredentialSubject) {
    if (isGxServiceOfferingCs(cs)) {
      this.gxServiceOfferingWizard.prefillFields(cs, ["gx:providedBy"]);
    }
    if (isDataResourceCs(cs)) {
      this.gxDataResourceWizard.prefillFields(cs, []);
    }

  }

  async prefillWizardNewOffering() {
    await this.retrieveAndSetParticipantId();
    this.resetPossibleSpecificFormValues();

    let gxServiceOfferingCs = {
      "gx:providedBy": {
        "@id": this.participantId
      },
      "@type": "gx:ServiceOffering"
    }

    let prefillSd: any[] = [gxServiceOfferingCs];

    if (!this.isOfferingDataOffering()) {
      this.loadShape(this.offerType, TBR_SERVICE_OFFERING_ID, null).then(_ => {
        this.prefillFields(prefillSd);
      });
    } else {
      let gxDataResourceCs = {
        "gx:producedBy": {
          "@id": this.participantId
        },
        "gx:copyrightOwnedBy": [this.participantId],
        "gx:containsPII": false,
        "@type": "gx:DataResource"
      }

      prefillSd.push(gxDataResourceCs);

      this.loadShape(this.offerType, TBR_SERVICE_OFFERING_ID, TBR_DATA_RESOURCE_ID).then(_ => {
        this.prefillFields(prefillSd);
      });
    }
  }

  async retrieveAndSetParticipantId() {
    try {
      const response = await this.apiService.getParticipantId();
      console.log(response);
      this.participantId = response.participantId;
    } catch (e) {
      console.error(e);
    }
  }

  reset() {
    this.stepper.reset();
    this.prefillWizardNewOffering();
  }

}
