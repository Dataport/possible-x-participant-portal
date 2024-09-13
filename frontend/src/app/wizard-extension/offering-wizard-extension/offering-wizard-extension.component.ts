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

import { Component, forwardRef, ViewChild } from '@angular/core';
import { StatusMessageComponent } from '../../views/common-views/status-message/status-message.component';
import { HttpErrorResponse } from '@angular/common/http';
import { BaseWizardExtensionComponent } from '../base-wizard-extension/base-wizard-extension.component';
import { ICredentialSubject, IGxServiceOfferingCs, IGxDataResourceCs } from '../../views/offer/offer-data';
import { isGxServiceOfferingCs, isDataResourceCs } from '../../utils/credential-utils';
import { BehaviorSubject, takeWhile } from 'rxjs';
import { ApiService } from '../../services/mgmt/api/api.service';
import { POLICY_MAP } from '../../constants';

import {
  AbstractControl,
  ControlValueAccessor,
  FormBuilder,
  FormControl,
  FormGroup,
  NG_VALUE_ACCESSOR, ValidationErrors,
  Validators
} from "@angular/forms";

interface PossibleSpecificFieldsFormModel {
  fileName: FormControl<string>;
  policy: FormControl<string>;
}

@Component({
  selector: 'app-offering-wizard-extension',
  templateUrl: './offering-wizard-extension.component.html',
  styleUrls: ['./offering-wizard-extension.component.scss'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => OfferingWizardExtensionComponent),
    multi: true
  }]
})
export class OfferingWizardExtensionComponent implements ControlValueAccessor {
  @ViewChild("gxServiceOfferingWizard") private gxServiceOfferingWizard: BaseWizardExtensionComponent;
  @ViewChild("gxDataResourceWizard") private gxDataResourceWizard: BaseWizardExtensionComponent;
  @ViewChild("offerCreationStatusMessage") public offerCreationStatusMessage!: StatusMessageComponent;

  possibleSpecificFieldsForm: FormGroup<PossibleSpecificFieldsFormModel>;
  selectedFileName: string = "";
  policyMap = POLICY_MAP;
  selectedPolicy: string = "";

  private onChange = (value: any) => {};
  private onTouched = () => {};

  public prefillDone: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  protected isDataOffering: boolean = true;

  constructor(
    private apiService: ApiService,
    private fb: FormBuilder
  ) { 
    this.possibleSpecificFieldsForm = this.fb.group({
      fileName: this.fb.nonNullable.control<string>('', [Validators.required, this.validateStringField]),
      policy: this.fb.nonNullable.control<string>('', [Validators.required, this.validateStringField])
    });
  }

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
        fileName: this.selectedFileName,
        policy: this.policyMap[this.selectedPolicy].policy
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
    this.gxDataResourceWizard.ngOnDestroy();
    this.possibleSpecificFieldsForm = null;
    this.offerCreationStatusMessage.hideAllMessages();
  }

  protected isWizardFormInvalid(): boolean {
    let serviceOfferingWizardInvalid = this.gxServiceOfferingWizard?.isWizardFormInvalid();
    let dataResourceWizardInvalid = this.isOfferingDataOffering() ? this.gxDataResourceWizard?.isWizardFormInvalid() : false;
  
    return serviceOfferingWizardInvalid || dataResourceWizardInvalid;
  }

  protected isOfferingDataOffering() {
    return this.isDataOffering;
  }

  public isFieldFilled(str: string){
    if (!str || str.trim().length === 0) {
      return false;
    }

    return true;
  }

  writeValue(value: any): void {
    if (value) {
      this.selectedFileName = value.fileName || '';
      this.selectedPolicy = value.policy || '';
      this.possibleSpecificFieldsForm.setValue({
        fileName: this.selectedFileName,
        policy: this.selectedPolicy
      });
    }
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    if (isDisabled) {
      this.possibleSpecificFieldsForm.disable();
    } else {
      this.possibleSpecificFieldsForm.enable();
    }
  }

  get isInvalidFileName(): boolean {
    return this.possibleSpecificFieldsForm.controls.fileName.hasError('Empty');
  }

  get isInvalidPolicy(): boolean {
    return this.possibleSpecificFieldsForm.controls.policy.hasError('Empty');
  }

  validateStringField(control: AbstractControl): ValidationErrors | null {
    if (control.value && control.value.trim() !== '') {
      return null;
    }
    return {
      'Empty': true
    };
  }
}
