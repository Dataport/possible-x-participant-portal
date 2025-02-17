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

import {AfterViewInit, ChangeDetectorRef, Component, Input, ViewChild} from '@angular/core';
import {AccordionItemComponent} from "@coreui/angular";
import {NameMappingService} from "../../../services/mgmt/name-mapping.service";
import moment from "moment";
import {
  IAgreementOffsetUnit, IEndAgreementOffsetPolicy,
  IEnforcementPolicyUnion,
  IEverythingAllowedPolicy,
  IParticipantRestrictionPolicy
} from "../../../services/mgmt/api/backend";
import {FormGroup, FormBuilder} from "@angular/forms";

@Component({
  selector: 'app-possible-x-enforced-policy-selector',
  templateUrl: './possible-x-enforced-policy-selector.component.html',
  styleUrls: ['./possible-x-enforced-policy-selector.component.scss']
})
export class PossibleXEnforcedPolicySelectorComponent implements AfterViewInit {
  nameMapping: { [key: string]: string } = {};
  sortedIds: string[] = [];
  @ViewChild('participantRestrictionPolicyAccItem') participantRestrictionPolicyAccordionItem!: AccordionItemComponent;
  @ViewChild('startDatePolicyAccItem') startDatePolicyAccordionItem!: AccordionItemComponent;
  @ViewChild('endDatePolicyAccItem') endDatePolicyAccordionItem!: AccordionItemComponent;
  @ViewChild('endAgreementOffsetPolicyAccItem') endAgreementOffsetPolicyAccordionItem!: AccordionItemComponent;
  @Input() isOfferingDataOffering?: boolean = undefined;
  checkboxFormGroup: FormGroup;
  participantRestrictionPolicyCB = 'participantRestrictionPolicyCB';
  participantRestrictionPolicyIds: string[] = [''];
  startDatePolicyCB = 'startDatePolicyCB';
  startDate: Date = undefined;
  endDatePolicyCB = 'endDatePolicyCB';
  endDate: Date = undefined;
  endAgreementOffsetPolicyCB = 'endAgreementOffsetPolicyCB';
  endAgreementOffset: number = undefined;
  endAgreementOffsetUnit: IAgreementOffsetUnit = undefined;

  // Define matrix for enabling/disabling policy checkboxes
  disableMatrix: { [key: string]: string[] } = {
    [this.startDatePolicyCB]: [this.endAgreementOffsetPolicyCB],
    [this.endDatePolicyCB]: [this.endAgreementOffsetPolicyCB],
    [this.endAgreementOffsetPolicyCB]: [this.startDatePolicyCB, this.endDatePolicyCB],
    [this.participantRestrictionPolicyCB]: []
  };

  constructor(
    private readonly nameMappingService: NameMappingService,
    private readonly cdr: ChangeDetectorRef,
    private readonly fb: FormBuilder
  ) {
    this.checkboxFormGroup = this.fb.group({
      [this.startDatePolicyCB]: [false],
      [this.endDatePolicyCB]: [false],
      [this.endAgreementOffsetPolicyCB]: [false],
      [this.participantRestrictionPolicyCB]: [false]
    });
  }

  ngAfterViewInit(): void {
    this.setNameMapping();
  }

  protected setNameMapping() {
    this.nameMapping = this.nameMappingService.getNameMapping();
    this.sortedIds = this.getIdsSortedByNames();
    this.cdr.detectChanges();
  }

  get isStartDatePolicyDisabled(): boolean {
    return this.checkboxFormGroup.get(this.startDatePolicyCB)?.disabled;
  }

  get isEndDatePolicyDisabled(): boolean {
      return this.checkboxFormGroup.get(this.endDatePolicyCB)?.disabled;
  }

  get isEndAgreementOffsetPolicyDisabled(): boolean {
      return this.checkboxFormGroup.get(this.endAgreementOffsetPolicyCB)?.disabled;
  }

  get isParticipantRestrictionPolicyDisabled(): boolean {
      return this.checkboxFormGroup.get(this.participantRestrictionPolicyCB)?.disabled;
  }

  protected get isInvalidParticipantRestrictionPolicy(): boolean {
    return this.isParticipantRestrictionPolicyChecked
        && this.participantRestrictionPolicyIds.some(id => !this.isFieldFilled(id));
  }

  protected get isInvalidStartDatePolicy(): boolean {
    return this.isStartDatePolicyChecked && !this.isValidDate(this.startDate);
  }

  protected get isInvalidEndDatePolicy(): boolean {
    return this.isEndDatePolicyChecked && !this.isValidDate(this.endDate);
  }

  protected get isInvalidEndAgreementOffsetPolicy(): boolean {
    return this.isEndAgreementOffsetPolicyChecked && (!this.isValidOffset(this.endAgreementOffset)
        || !this.isValidOffsetUnit(this.endAgreementOffsetUnit));
  }

  public get isAnyPolicyInvalid(): boolean {
    return this.isInvalidParticipantRestrictionPolicy || this.isInvalidStartDatePolicy || this.isInvalidEndDatePolicy
      || this.isInvalidEndAgreementOffsetPolicy;
  }

  protected isValidOffset(offset: number): boolean {
    return offset != null && Number.isFinite(offset) && offset >= 0;
  }

  protected isValidOffsetUnit(unit: IAgreementOffsetUnit): boolean {
    return unit != null && ["s", "m", "h", "d"].includes(unit);
  }

  protected isValidDate(date: Date): boolean {
    if (!date) {
      return false;
    }

    const momentDate = moment(date, moment.ISO_8601, true);
    const isValid = momentDate.isValid();

    return isValid && momentDate.toISOString() === date.toISOString();
  }

  protected isFieldFilled(str: string) {
    return str && str.trim().length > 0;
  }

  get isStartDatePolicyChecked(): boolean {
    return this.checkboxFormGroup.get(this.startDatePolicyCB)?.value
  }

  get isEndDatePolicyChecked(): boolean {
      return this.checkboxFormGroup.get(this.endDatePolicyCB)?.value
  }

  get isEndAgreementOffsetPolicyChecked(): boolean {
      return this.checkboxFormGroup.get(this.endAgreementOffsetPolicyCB)?.value
  }

  get isParticipantRestrictionPolicyChecked(): boolean {
      return this.checkboxFormGroup.get(this.participantRestrictionPolicyCB)?.value
  }

  protected get isAnyPolicyChecked(): boolean {
    return this.isStartDatePolicyChecked || this.isEndDatePolicyChecked || this.isEndAgreementOffsetPolicyChecked
      || this.isParticipantRestrictionPolicyChecked
  }

  protected addInput(): void {
    this.participantRestrictionPolicyIds.push('');
  }

  protected removeInput(index: number): void {
    if (this.participantRestrictionPolicyIds.length > 1) {
      this.participantRestrictionPolicyIds.splice(index, 1);
    }
  }

  protected customTrackBy(index: number, obj: any): any {
    return index;
  }

  protected getIdsSortedByNames(): string[] {
    return Object.keys(this.nameMapping).sort((a, b) => {
      return this.nameMapping[a].localeCompare(this.nameMapping[b]);
    });
  }

  public getNameIdStringById(id: string): string {
    const name = this.nameMappingService.getNameById(id);
    return `${name} (${id})`;
  }

  protected handleCheckboxChange(checkedPolicy: string, accordionItem: any) {
    // Set the visibility of the accordion item based on the checkbox value
    accordionItem.visible = this.checkboxFormGroup.get(this[checkedPolicy])?.value;

    // Get all form controls
    const formControls = this.checkboxFormGroup.controls;

    // Get keys of checked checkboxes
    const selectedCheckboxes = Object.keys(formControls).filter(key => formControls[key].value);

    // Enable all form controls
    Object.keys(formControls).forEach(key => formControls[key].enable());

    // Disable form controls based on the selected checkboxes
    selectedCheckboxes.forEach(selectedKey => {
      this.disableMatrix[selectedKey].forEach(key => formControls[key].disable());
    });
  }

  public getPolicies(): IEnforcementPolicyUnion[] {
    let policies: IEnforcementPolicyUnion[] = [];

    if (!this.isAnyPolicyChecked) {
      policies.push({
        "@type": "EverythingAllowedPolicy"
      } as IEverythingAllowedPolicy);
    } else {
      if (this.isParticipantRestrictionPolicyChecked && !this.isParticipantRestrictionPolicyDisabled) {
        policies.push({
          "@type": "ParticipantRestrictionPolicy",
          allowedParticipants: Array.from(new Set(this.participantRestrictionPolicyIds))
        } as IParticipantRestrictionPolicy);
      }

      if (this.isStartDatePolicyChecked && !this.isStartDatePolicyDisabled) {
        policies.push({
          "@type": "StartDatePolicy",
          date: this.startDate.toISOString()
        } as any);
      }

      if (this.isEndDatePolicyChecked && !this.isEndDatePolicyDisabled) {
        policies.push({
          "@type": "EndDatePolicy",
          date: this.endDate.toISOString()
        } as any);
      }

      if (this.isEndAgreementOffsetPolicyChecked && !this.isEndAgreementOffsetPolicyDisabled) {
        policies.push({
          "@type": "EndAgreementOffsetPolicy",
          offsetNumber: this.endAgreementOffset,
          offsetUnit: this.endAgreementOffsetUnit
        } as IEndAgreementOffsetPolicy);
      }
    }

    return policies;
  }

  private resetAccordion() {
    this.participantRestrictionPolicyAccordionItem.visible = false;
    this.startDatePolicyAccordionItem.visible = false;
    this.endDatePolicyAccordionItem.visible = false;

    if (this.endAgreementOffsetPolicyAccordionItem){
      this.endAgreementOffsetPolicyAccordionItem.visible = false;
    }
  }

  private resetCheckboxes() {
    // Set the value of all checkboxes to false
    this.checkboxFormGroup.reset({
      [this.startDatePolicyCB]: false,
      [this.endDatePolicyCB]: false,
      [this.endAgreementOffsetPolicyCB]: false,
      [this.participantRestrictionPolicyCB]: false
    });

    // Enable all checkboxes
    Object.keys(this.checkboxFormGroup.controls).forEach(key => {
      this.checkboxFormGroup.get(key).enable();
    });
  }

  public resetEnforcementPolicyForm() {
    this.participantRestrictionPolicyIds = [''];
    this.startDate = undefined;
    this.endDate = undefined;
    this.endAgreementOffset = undefined;
    this.endAgreementOffsetUnit = undefined;
    this.resetCheckboxes();
    this.resetAccordion();
  }
}
