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

  // Matrix for enabling/disabling policy checkboxes
  disableMatrix: { [key: string]: string[] } = {
    [this.startDatePolicyCB]: [this.endAgreementOffsetPolicyCB],
    [this.endDatePolicyCB]: [this.endAgreementOffsetPolicyCB],
    [this.endAgreementOffsetPolicyCB]: [this.startDatePolicyCB, this.endDatePolicyCB],
    [this.participantRestrictionPolicyCB]: []
  };

  constructor(
    private readonly nameMappingService: NameMappingService, private readonly cdr: ChangeDetectorRef, private readonly fb: FormBuilder
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

  protected get isStartDatePolicyDisabled(): boolean {
    return this.checkboxFormGroup.get(this.startDatePolicyCB)?.disabled;
  }

  protected get isEndDatePolicyDisabled(): boolean {
      return this.checkboxFormGroup.get(this.endDatePolicyCB)?.disabled;
  }

  protected get isEndAgreementOffsetPolicyDisabled(): boolean {
      return this.checkboxFormGroup.get(this.endAgreementOffsetPolicyCB)?.disabled;
  }

  protected get isParticipantRestrictionPolicyDisabled(): boolean {
      return this.checkboxFormGroup.get(this.participantRestrictionPolicyCB)?.disabled;
  }

  protected get isInvalidParticipantRestrictionPolicy(): boolean {
    return this.checkboxFormGroup.get(this.participantRestrictionPolicyCB)?.value
      && this.participantRestrictionPolicyIds.some(id => !this.isFieldFilled(id));
  }

  protected get isInvalidStartDatePolicy(): boolean {
    return this.checkboxFormGroup.get(this.startDatePolicyCB)?.value && !this.isValidDate(this.startDate);
  }

  protected get isInvalidEndDatePolicy(): boolean {
    return this.checkboxFormGroup.get(this.endDatePolicyCB)?.value && !this.isValidDate(this.endDate);
  }

  protected get isInvalidEndAgreementOffsetPolicy(): boolean {
    return this.checkboxFormGroup.get(this.endAgreementOffsetPolicyCB)?.value
      && (!this.isValidOffset(this.endAgreementOffset) || !this.isValidOffsetUnit(this.endAgreementOffsetUnit));
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

  protected get isAnyPolicyChecked(): boolean {
    return this.checkboxFormGroup.get(this.startDatePolicyCB)?.value
      || this.checkboxFormGroup.get(this.endDatePolicyCB)?.value
      || this.checkboxFormGroup.get(this.endAgreementOffsetPolicyCB)?.value
      || this.checkboxFormGroup.get(this.participantRestrictionPolicyCB)?.value;
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

  protected handleCheckboxChange(policyChecked: string, accordionItem: any) {
    // Open accordion item if policy is checked, close it otherwise
    accordionItem.visible = this.checkboxFormGroup.get(this[policyChecked])?.value;

    const formControls = this.checkboxFormGroup.controls;
    const selectedCheckboxes = Object.keys(formControls).filter(key => formControls[key].value);

    // Enable all checkboxes initially
    Object.keys(formControls).forEach(key => formControls[key].enable());

    // Apply the disable matrix
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
      let participantRestrictionPolicyCBControl = this.checkboxFormGroup.get(this.participantRestrictionPolicyCB);
      if (participantRestrictionPolicyCBControl?.value && participantRestrictionPolicyCBControl?.disabled === false) {
        policies.push({
          "@type": "ParticipantRestrictionPolicy",
          allowedParticipants: Array.from(new Set(this.participantRestrictionPolicyIds))
        } as IParticipantRestrictionPolicy);
      }

      let startDatePolicyCBControl = this.checkboxFormGroup.get(this.startDatePolicyCB);
      if (startDatePolicyCBControl?.value && startDatePolicyCBControl?.disabled === false) {
        policies.push({
          "@type": "StartDatePolicy",
          date: this.startDate.toISOString()
        } as any);
      }

      let endDatePolicyCBControl = this.checkboxFormGroup.get(this.endDatePolicyCB);
      if (endDatePolicyCBControl?.value && endDatePolicyCBControl?.disabled === false) {
        policies.push({
          "@type": "EndDatePolicy",
          date: this.endDate.toISOString()
        } as any);
      }

      let endAgreementOffsetPolicyCBControl = this.checkboxFormGroup.get(this.endAgreementOffsetPolicyCB);
      if (endAgreementOffsetPolicyCBControl?.value && endAgreementOffsetPolicyCBControl?.disabled === false) {
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
