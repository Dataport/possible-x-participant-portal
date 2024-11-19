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
import {AccordionItemComponent} from "@coreui/angular";

@Component({
  selector: 'app-offering-wizard-extension',
  templateUrl: './offering-wizard-extension.component.html',
  styleUrls: ['./offering-wizard-extension.component.scss']
})
export class OfferingWizardExtensionComponent implements AfterViewInit {
  @ViewChild("offerCreationStatusMessage") public offerCreationStatusMessage!: StatusMessageComponent;
  selectedFileName: string = "";
  isPolicyChecked: boolean = false;
  dapsIDs: string[] = [''];
  waitingForResponse = true;
  offerType: string = "data";
  participantId = "";
  serviceOfferingShapeSource = "";
  dataResourceShapeSource = "";
  @ViewChild("stepper") stepper: MatStepper;
  @ViewChild('accordionItem') accordionItem!: AccordionItemComponent;
  protected isDataOffering: boolean = true;
  @ViewChild("gxServiceOfferingWizard") private gxServiceOfferingWizard: BaseWizardExtensionComponent;
  @ViewChild("gxDataResourceWizard") private gxDataResourceWizard: BaseWizardExtensionComponent;

  constructor(
    private apiService: ApiService
  ) {
  }

  get isInvalidFileName(): boolean {
    return !this.isFieldFilled(this.selectedFileName);
  }

  get isInvalidPolicy(): boolean {
    return this.isPolicyChecked && this.dapsIDs.some(id => !this.isFieldFilled(id));
  }

  ngAfterViewInit(): void {
    this.retrieveAndAdaptServiceOfferingShape();
    this.retrieveAndAdaptDataResourceShape();
    this.retrieveAndSetParticipantId();
    this.resetPossibleSpecificFormValues();
    this.resetAccordionItem();
  }

  async retrieveAndAdaptServiceOfferingShape() {
    try {
      console.log("Retrieving service offering shape");
      const serviceOfferingShapeSource = await this.apiService.getGxServiceOfferingShape();
      this.serviceOfferingShapeSource = this.adaptGxShape(serviceOfferingShapeSource, "ServiceOffering", ["aggregationOf"]);
    } catch (e) {
      console.error(e);
    }
  }

  async retrieveAndAdaptDataResourceShape() {
    try {
      console.log("Retrieving data resource shape");
      const dataResourceShapeSource = await this.apiService.getGxDataResourceShape();
      this.dataResourceShapeSource = this.adaptGxShape(dataResourceShapeSource, "DataResource",
        ["exposedThrough"]);
    } catch (e) {
      console.error(e);
    }
  }

  public async loadShape(wizard: BaseWizardExtensionComponent, shapeSource: string, id: string): Promise<void> {
    console.log("Loading shape");
    await wizard.loadShape(Promise.resolve(shapeSource), id);
  }

  async createOffer() {
    console.log("Create offer.");
    this.waitingForResponse = true;
    this.offerCreationStatusMessage.showInfoMessage();

    let gxOfferingJsonSd: IGxServiceOfferingCredentialSubject = this.gxServiceOfferingWizard.generateJsonCs();

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
    this.resetAccordionItem();
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

  public resetAccordionItem() {
    this.accordionItem.visible = false;
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

  async prefillWizardNewOffering() {
    this.resetPossibleSpecificFormValues();
    if (this.isOfferingDataOffering()) {
      this.prefillDataResourceWizard();
    } else {
      this.prefillServiceOfferingWizard();
    }
  }

  async prefillDataResourceWizard() {

    let gxDataResourceCs = {
      "gx:producedBy": {
        "@id": this.participantId
      },
      "gx:copyrightOwnedBy": [this.participantId],
      "gx:containsPII": false,
      "@type": "gx:DataResource"
    } as any;

    this.loadShape(this.gxDataResourceWizard, this.dataResourceShapeSource, TBR_DATA_RESOURCE_ID).then(_ => {
      this.prefillHandleCs(gxDataResourceCs);
    });
  }

  async prefillServiceOfferingWizard() {

    let gxServiceOfferingCs = {
      "gx:providedBy": {
        "@id": this.participantId
      },
      "@type": "gx:ServiceOffering",
    } as any;

    if (this.isOfferingDataOffering()) {
      let gxDataResourceJsonSd: IGxDataResourceCredentialSubject = this.gxDataResourceWizard.generateJsonCs();
      gxServiceOfferingCs["schema:name"] = "Data Offering Service - " + (gxDataResourceJsonSd["schema:name"] ? gxDataResourceJsonSd["schema:name"]["@value"] : "data resource name not available");
      gxServiceOfferingCs["schema:description"] = " ";//"Data Offering Service provides data (" + (gxDataResourceJsonSd["schema:name"] ? gxDataResourceJsonSd["schema:name"]["@value"] : "data resource name not available") + ") securely through the Possible Dataspace software solution. The Data Offering Service enables secure and sovereign data exchange between different organizations using the Eclipse Dataspace Connector (EDC). The service seamlessly integrates with IONOS S3 buckets to ensure reliable and scalable data storage and transfer.";
      gxServiceOfferingCs["gx:dataAccountExport"] = {
        "@type": "gx:DataAccountExport",
        "gx:formatType": "text/plain",
        "gx:accessType": "digital",
        "gx:requestType": "email"
      };
      gxServiceOfferingCs["gx:dataProtectionRegime"] = ["GDPR2016"];
    }

    this.loadShape(this.gxServiceOfferingWizard, this.serviceOfferingShapeSource, TBR_SERVICE_OFFERING_ID).then(_ => {
      this.prefillHandleCs(gxServiceOfferingCs);
    });

  }

  async retrieveAndSetParticipantId() {
    try {
      console.log("Retrieving participant id");
      const response = await this.apiService.getParticipantId();
      console.log(response);
      this.participantId = response.participantId;
    } catch (e) {
      console.error(e);
    }
  }

  reset() {
    this.stepper.reset();
    this.resetPossibleSpecificFormValues();
    this.resetAccordionItem();
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

}
