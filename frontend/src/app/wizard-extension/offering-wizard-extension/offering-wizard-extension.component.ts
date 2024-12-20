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

import {AfterViewInit, Component, ViewChild, ChangeDetectorRef} from '@angular/core';
import {StatusMessageComponent} from '../../views/common-views/status-message/status-message.component';
import {BaseWizardExtensionComponent} from '../base-wizard-extension/base-wizard-extension.component';
import {isDataResourceCs, isGxServiceOfferingCs} from '../../utils/credential-utils';
import {ApiService} from '../../services/mgmt/api/api.service';
import {
  IEverythingAllowedPolicy,
  IGxDataResourceCredentialSubject, IGxLegitimateInterest,
  IGxServiceOfferingCredentialSubject,
  INodeKindIRITypeId,
  IParticipantRestrictionPolicy,
  IPojoCredentialSubject, IPrefillFieldsTO
} from '../../services/mgmt/api/backend';
import {TBR_DATA_RESOURCE_ID, TBR_LEGITIMATE_INTEREST_ID, TBR_SERVICE_OFFERING_ID} from "../../views/offer/offer-data";
import {MatStepper} from "@angular/material/stepper";
import {AccordionItemComponent} from "@coreui/angular";
import {HttpErrorResponse} from "@angular/common/http";
import {NameMappingService} from "../../services/mgmt/name-mapping.service";

@Component({
  selector: 'app-offering-wizard-extension',
  templateUrl: './offering-wizard-extension.component.html',
  styleUrls: ['./offering-wizard-extension.component.scss']
})
export class OfferingWizardExtensionComponent implements AfterViewInit {
  @ViewChild("offerCreationStatusMessage") public offerCreationStatusMessage!: StatusMessageComponent;
  selectedFileName: string = "";
  isPolicyChecked: boolean = false;
  ids: string[] = [''];
  nameMapping: { [key: string]: string } = {};
  waitingForResponse = true;
  offerType: string = "data";
  prefillFields: IPrefillFieldsTO | undefined = undefined;
  serviceOfferingShapeSource = "";
  dataResourceShapeSource = "";
  legitimateInterestShapeSource = "";
  @ViewChild("stepper") stepper: MatStepper;
  @ViewChild('accordionItem') accordionItem!: AccordionItemComponent;
  protected containsPII: boolean = false;
  @ViewChild("gxServiceOfferingWizard") private gxServiceOfferingWizard: BaseWizardExtensionComponent;
  @ViewChild("gxDataResourceWizard") private gxDataResourceWizard: BaseWizardExtensionComponent;
  @ViewChild("gxLegitimateInterestWizard") private gxLegitimateInterestWizard: BaseWizardExtensionComponent;

  constructor(
    private apiService: ApiService, private nameMappingService: NameMappingService, private cdr: ChangeDetectorRef
  ) {
  }

  get isInvalidFileName(): boolean {
    return !this.isFieldFilled(this.selectedFileName);
  }

  get isInvalidPolicy(): boolean {
    return this.isPolicyChecked && this.ids.some(id => !this.isFieldFilled(id));
  }

  ngAfterViewInit(): void {
      this.retrieveAndAdaptServiceOfferingShape();
      this.retrieveAndAdaptDataResourceShape();
      this.retrieveLegitimateInterestShape();
      this.retrieveAndSetPrefillFields();
      this.resetPossibleSpecificFormValues();
      this.resetAccordionItem();
      this.setNameMapping();
      this.containsPII = false;
  }

  setNameMapping() {
    this.nameMapping = this.nameMappingService.getNameMapping();
    this.cdr.detectChanges();
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

  async retrieveLegitimateInterestShape() {
    try {
      console.log("Retrieving legitimate interest shape");
      this.legitimateInterestShapeSource = await this.apiService.getGxLegitimateInterestShape();
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

    let gxLegitimateInterestJsonSd: IGxLegitimateInterest;

    if (this.isPolicyChecked) {
      policy = {
        "@type": "ParticipantRestrictionPolicy",
        allowedParticipants: Array.from(new Set(this.ids))
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
      ],
      legitimateInterest: gxLegitimateInterestJsonSd,
    };

    let createOfferMethod: (offer: any) => Promise<any>;
    createOfferMethod = this.apiService.createServiceOffering.bind(this.apiService)

    if (this.isOfferingDataOffering()) {
      let gxDataResourceJsonSd: IGxDataResourceCredentialSubject = this.gxDataResourceWizard.generateJsonCs();
      gxDataResourceJsonSd["gx:exposedThrough"] = {id: gxOfferingJsonSd.id} as INodeKindIRITypeId;

      createOfferTo.dataResourceCredentialSubject = gxDataResourceJsonSd;
      createOfferTo.fileName = this.selectedFileName;

      if (this.isContainingPII()) {
        createOfferTo.legitimateInterest = this.gxLegitimateInterestWizard.generateJsonCs();
      }

      createOfferMethod = this.apiService.createDataOffering.bind(this.apiService);
    }

    let trimmedCreateOfferTo = this.trimStringsInDataStructure(createOfferTo);
    console.log(trimmedCreateOfferTo);

     createOfferMethod(trimmedCreateOfferTo).then(response => {
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
    this.gxServiceOfferingWizard?.ngOnDestroy();
    this.gxDataResourceWizard?.ngOnDestroy();
    this.gxLegitimateInterestWizard?.ngOnDestroy();
    this.resetPossibleSpecificFormValues();
    this.resetAccordionItem();
    this.offerCreationStatusMessage.hideAllMessages();
    this.containsPII = false;
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
    this.ids = [''];
    this.containsPII = false;
  }

  public resetAccordionItem() {
    this.accordionItem.visible = false;
  }

  addInput(): void {
    this.ids.push('');
  }

  removeInput(index: number): void {
    if (this.ids.length > 1) {
      this.ids.splice(index, 1);
    }
  }

  public customTrackBy(index: number, obj: any): any {
    return index;
  }

  async prefillWizardNewOffering() {
    this.resetPossibleSpecificFormValues();
    this.resetAccordionItem();
    if (this.isOfferingDataOffering()) {
      this.prefillDataResourceWizard();
    } else {
      this.prefillServiceOfferingWizard();
    }
  }

  async prefillDataResourceWizard() {

    let gxDataResourceCs = {
      "gx:producedBy": {
        "@id": this.prefillFields.participantId
      },
      "gx:copyrightOwnedBy": [this.prefillFields.participantId],
      "gx:containsPII": false,
      "@type": "gx:DataResource"
    } as any;

    this.loadShape(this.gxDataResourceWizard, this.dataResourceShapeSource, TBR_DATA_RESOURCE_ID).then(_ => {
      this.prefillHandleCs(gxDataResourceCs);
    });
  }

  async prefillLegitimateInterestWizard() {
    let gxLegitimateInterestCs = {"@type": "gx:LegitimateInterest"} as any;

    this.loadShape(this.gxLegitimateInterestWizard, this.legitimateInterestShapeSource, TBR_LEGITIMATE_INTEREST_ID).then(_ => {
      this.prefillHandleCs(gxLegitimateInterestCs);
    });
  }

  async prefillServiceOfferingWizard() {

    let gxServiceOfferingCs = {
      "gx:providedBy": {
        "@id": this.prefillFields?.participantId
      },
      "@type": "gx:ServiceOffering",
    } as any;

    if (this.isOfferingDataOffering()) {
      let gxDataResourceJsonSd: IGxDataResourceCredentialSubject = this.trimStringsInDataStructure(this.gxDataResourceWizard.generateJsonCs());
      let dataResourceName = gxDataResourceJsonSd["schema:name"] ? gxDataResourceJsonSd["schema:name"]["@value"] : "data resource name not available";
      gxServiceOfferingCs["schema:name"] = this.prefillFields.dataProductPrefillFields.serviceOfferingName.replace("<Data resource name>", dataResourceName);
      gxServiceOfferingCs["schema:description"] = this.prefillFields.dataProductPrefillFields.serviceOfferingDescription.replace("<Data resource name>", dataResourceName);
    }

    this.loadShape(this.gxServiceOfferingWizard, this.serviceOfferingShapeSource, TBR_SERVICE_OFFERING_ID).then(_ => {
      this.prefillHandleCs(gxServiceOfferingCs);
    });

  }

  async retrieveAndSetPrefillFields() {
    try {
      console.log("Retrieving prefill fields");
      const response = await this.apiService.getPrefillFields();
      console.log(response);
      this.prefillFields = response;
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

  protected isContainingPII(): boolean {
    return this.containsPII;
  }

  protected isDataResourceValid(): boolean {
    return !this.gxDataResourceWizard?.isWizardFormInvalid() && !this.isInvalidFileName;
  }

  protected isLegitimateInterestValid(): boolean {
    return !this.gxLegitimateInterestWizard?.isWizardFormInvalid();
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

  protected prepareStepAfterDataResource() {
    this.setContainsPII();

    setTimeout(() => {
      if (this.isContainingPII()) {
        this.prefillLegitimateInterestWizard();
      } else {
        this.prefillServiceOfferingWizard();
      }
    }, 50);
  }

  protected setContainsPII() {
    let gxDataResourceJsonSd = this.gxDataResourceWizard.generateJsonCs();
    this.containsPII = gxDataResourceJsonSd["gx:containsPII"];
  }

  private prefillHandleCs(cs: IPojoCredentialSubject) {
    if (isGxServiceOfferingCs(cs)) {
      this.gxServiceOfferingWizard.prefillFields(cs, ["gx:providedBy"]);
    }
    if (isDataResourceCs(cs)) {
      this.gxDataResourceWizard.prefillFields(cs, []);
    }

  }

  trimStringsInDataStructure = (obj: any): any => {
    if (typeof obj === 'string') {
      return obj.trim();
    } else if (Array.isArray(obj)) {
      return obj.map(this.trimStringsInDataStructure);
    } else if (typeof obj === 'object' && obj !== null) {
      return Object.keys(obj).reduce((acc, key) => {
        acc[key] = this.trimStringsInDataStructure(obj[key]);
        return acc;
      }, {} as any);
    }
    return obj;
  }

  prepareStepBeforeDataResource() {
    this.containsPII = false;
  }

  getIdsSortedByNames(): string[] {
    return Object.keys(this.nameMapping).sort((a, b) => {
      return this.nameMapping[a].localeCompare(this.nameMapping[b]);
    });
  }

}
