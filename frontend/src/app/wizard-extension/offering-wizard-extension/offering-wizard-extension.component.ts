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
  IPojoCredentialSubject, IPrefillFieldsTO,
  ITimeDatePolicy
} from '../../services/mgmt/api/backend';
import {TBR_DATA_RESOURCE_ID, TBR_LEGITIMATE_INTEREST_ID, TBR_SERVICE_OFFERING_ID} from "../../views/offer/offer-data";
import {MatStepper} from "@angular/material/stepper";
import {AccordionItemComponent} from "@coreui/angular";
import {HttpErrorResponse} from "@angular/common/http";
import {NameMappingService} from "../../services/mgmt/name-mapping.service";
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-offering-wizard-extension',
  templateUrl: './offering-wizard-extension.component.html',
  styleUrls: ['./offering-wizard-extension.component.scss']
})
export class OfferingWizardExtensionComponent implements AfterViewInit {
  @ViewChild("offerCreationStatusMessage") public offerCreationStatusMessage!: StatusMessageComponent;
  selectedFileName: string = "";
  isContractBookingPolicyChecked: boolean = false;
  contractBookingPolicyIds: string[] = [''];
  isContractValidityStartPolicyChecked: boolean = false;
  contractValidityStartDate: Date = undefined;
  isContractValidityEndPolicyChecked: boolean = false;
  contractValidityEndDate: Date = undefined;
  nameMapping: { [key: string]: string } = {};
  sortedIds: string[] = [];
  waitingForResponse = true;
  offerType: string = "data";
  prefillFields: IPrefillFieldsTO | undefined = undefined;
  serviceOfferingShapeSource = "";
  dataResourceShapeSource = "";
  legitimateInterestShapeSource = "";
  @ViewChild("stepper") stepper: MatStepper;
  @ViewChild('accordionItem1') accordionItem1!: AccordionItemComponent;
  @ViewChild('accordionItem2') accordionItem2!: AccordionItemComponent;
  @ViewChild('accordionItem3') accordionItem3!: AccordionItemComponent;
  protected containsPII: boolean = false;
  @ViewChild("gxServiceOfferingWizard") private gxServiceOfferingWizard: BaseWizardExtensionComponent;
  @ViewChild("gxDataResourceWizard") private gxDataResourceWizard: BaseWizardExtensionComponent;
  @ViewChild("gxLegitimateInterestWizard") private gxLegitimateInterestWizard: BaseWizardExtensionComponent;
  dateControl = new FormControl(new Date());

  constructor(
    private apiService: ApiService, private nameMappingService: NameMappingService, private cdr: ChangeDetectorRef
  ) {
  }

  get isInvalidFileName(): boolean {
    return !this.isFieldFilled(this.selectedFileName);
  }

  get isInvalidContractBookingPolicy(): boolean {
    return this.isContractBookingPolicyChecked && this.contractBookingPolicyIds.some(id => !this.isFieldFilled(id));
  }

  get isInvalidContractValidityStartPolicy(): boolean {
    return this.isContractValidityStartPolicyChecked && !this.isValidDate(this.contractValidityStartDate);
  }

  get isInvalidContractValidityEndPolicy(): boolean {
    return this.isContractValidityEndPolicyChecked && !this.isValidDate(this.contractValidityEndDate);
  }

  isValidDate(date: any): boolean {
    return date instanceof Date && !isNaN(date.getTime());
  }

  get isAnyPolicyChecked(): boolean {
    return this.isContractBookingPolicyChecked || this.isContractValidityStartPolicyChecked || this.isContractValidityEndPolicyChecked;
  }

  ngAfterViewInit(): void {
      this.retrieveAndAdaptServiceOfferingShape();
      this.retrieveAndAdaptDataResourceShape();
      this.retrieveLegitimateInterestShape();
      this.retrieveAndSetPrefillFields();
      this.resetPossibleSpecificFormValues();
      this.resetAccordion();
      this.setNameMapping();
      this.containsPII = false;
  }

  setNameMapping() {
    this.nameMapping = this.nameMappingService.getNameMapping();
    this.sortedIds = this.getIdsSortedByNames();
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

    let policyList: (ITimeDatePolicy | IParticipantRestrictionPolicy | IEverythingAllowedPolicy)[] = [];

    let gxLegitimateInterestJsonSd: IGxLegitimateInterest;

    if (!this.isAnyPolicyChecked) {
      policyList.push({
        "@type": "EverythingAllowedPolicy"
      } as IEverythingAllowedPolicy);
    } else {
      if (this.isContractBookingPolicyChecked) {
        policyList.push({
          "@type": "ParticipantRestrictionPolicy",
          allowedParticipants: Array.from(new Set(this.contractBookingPolicyIds))
        } as IParticipantRestrictionPolicy);
      }

      if (this.isContractValidityStartPolicyChecked) {
        policyList.push({
          "@type": "StartDatePolicy",
          date: this.contractValidityStartDate.toISOString()
        } as any);
      }

      if (this.isContractValidityEndPolicyChecked) {
        policyList.push({
          "@type": "EndDatePolicy",
          date: this.contractValidityEndDate.toISOString()
        } as any);
      }
    }

    let createOfferTo: any = {
      serviceOfferingCredentialSubject: gxOfferingJsonSd,
      enforcementPolicies: policyList,
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
    this.resetAccordion();
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
    this.isContractBookingPolicyChecked = false;
    this.contractBookingPolicyIds = [''];
    this.isContractValidityStartPolicyChecked = false;
    this.contractValidityStartDate = undefined;
    this.isContractValidityEndPolicyChecked = false;
    this.contractValidityEndDate = undefined;
    this.containsPII = false;
  }

  public resetAccordion() {
    this.accordionItem1.visible = false;
    this.accordionItem2.visible = false;
    this.accordionItem3.visible = false;
  }

  addInput(): void {
    this.contractBookingPolicyIds.push('');
  }

  removeInput(index: number): void {
    if (this.contractBookingPolicyIds.length > 1) {
      this.contractBookingPolicyIds.splice(index, 1);
    }
  }

  public customTrackBy(index: number, obj: any): any {
    return index;
  }

  async prefillWizardNewOffering() {
    this.resetPossibleSpecificFormValues();
    this.resetAccordion();
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

    const participantId = this.prefillFields?.participantId;
    const participantNameIdString = this.getNameIdStringById(participantId);

    let gxServiceOfferingCs = {
      "gx:providedBy": {
        "@id": participantNameIdString,
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
    this.resetAccordion();
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
    return !this.gxServiceOfferingWizard?.isWizardFormInvalid() && !this.isInvalidContractBookingPolicy && !this.isInvalidContractValidityStartPolicy && !this.isInvalidContractValidityEndPolicy;
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

  getNameIdStringById(id: string): string {
    const name = this.nameMappingService.getNameById(id);
    return `${name} (${id})`;
  }

  handleCheckboxClick(event: Event, policyChecked: string, accordionItem: any) {
    event.stopPropagation();

    if (this[policyChecked]) {
      if (accordionItem.visible === false) {
        accordionItem.toggleItem();
      }
    } else {
      if (accordionItem.visible === true) {
        accordionItem.toggleItem();
      }
    }
  }

}
