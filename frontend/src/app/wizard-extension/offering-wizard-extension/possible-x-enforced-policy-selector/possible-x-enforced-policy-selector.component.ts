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

@Component({
  selector: 'app-possible-x-enforced-policy-selector',
  templateUrl: './possible-x-enforced-policy-selector.component.html',
  styleUrls: ['./possible-x-enforced-policy-selector.component.scss']
})
export class PossibleXEnforcedPolicySelectorComponent implements AfterViewInit {
  nameMapping: { [key: string]: string } = {};
  sortedIds: string[] = [];
  isParticipantRestrictionPolicyChecked: boolean = false;
  participantRestrictionPolicyIds: string[] = [''];
  isStartDatePolicyChecked: boolean = false;
  startDate: Date = undefined;
  isEndDatePolicyChecked: boolean = false;
  endDate: Date = undefined;
  isEndAgreementOffsetPolicyChecked: boolean = false;
  endAgreementOffset: number = undefined;
  endAgreementOffsetUnit: IAgreementOffsetUnit = undefined;
  isStartDatePolicyDisabled: boolean = false;
  isEndDatePolicyDisabled: boolean = false;
  isEndAgreementOffsetPolicyDisabled: boolean = false;
  @ViewChild('participantRestrictionPolicy') participantRestrictionPolicyAccordionItem!: AccordionItemComponent;
  @ViewChild('startDatePolicy') startDatePolicyAccordionItem!: AccordionItemComponent;
  @ViewChild('endDatePolicy') endDatePolicyAccordionItem!: AccordionItemComponent;
  @ViewChild('endAgreementOffsetPolicy') endAgreementOffsetPolicyAccordionItem!: AccordionItemComponent;
  @Input() isOfferingDataOffering?: boolean = undefined;

  constructor(
    private readonly nameMappingService: NameMappingService, private readonly cdr: ChangeDetectorRef
  ) {
  }

  ngAfterViewInit(): void {
    this.setNameMapping();
  }

  protected setNameMapping() {
    this.nameMapping = this.nameMappingService.getNameMapping();
    this.sortedIds = this.getIdsSortedByNames();
    this.cdr.detectChanges();
  }

  protected get isInvalidParticipantRestrictionPolicy(): boolean {
    return this.isParticipantRestrictionPolicyChecked && this.participantRestrictionPolicyIds.some(id => !this.isFieldFilled(id));
  }

  protected get isInvalidStartDatePolicy(): boolean {
    return this.isStartDatePolicyChecked && !this.isValidDate(this.startDate);
  }

  protected get isInvalidEndDatePolicy(): boolean {
    return this.isEndDatePolicyChecked && !this.isValidDate(this.endDate);
  }

  protected get isInvalidEndAgreementOffsetPolicy(): boolean {
    return this.isEndAgreementOffsetPolicyChecked && (!this.isValidOffset(this.endAgreementOffset) || !this.isValidOffsetUnit(this.endAgreementOffsetUnit));
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
    return this.isParticipantRestrictionPolicyChecked || this.isStartDatePolicyChecked || this.isEndDatePolicyChecked
      || this.isEndAgreementOffsetPolicyChecked;
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

  protected getNameIdStringById(id: string): string {
    const name = this.nameMappingService.getNameById(id);
    return `${name} (${id})`;
  }

  protected handleCheckboxClick(event: Event, policyChecked: string, accordionItem: any) {
    event.stopPropagation();

    if (this.isOfferingDataOffering) {
      this.setTimePolicyDisabledFlags(policyChecked);
    }

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

  private setFlagsWhenStartDatePolicyIsChecked(){
    this.isEndAgreementOffsetPolicyDisabled = true;
  }

  private setFlagsWhenEndDatePolicyIsChecked(){
    this.isEndAgreementOffsetPolicyDisabled = true;
  }

  private setFlagsWhenEndAgreementOffsetPolicyIsChecked(){
    this.isStartDatePolicyDisabled = true;
    this.isEndDatePolicyDisabled = true;
  }

  protected setTimePolicyDisabledFlags(policyChecked: string) {
    if (this[policyChecked]) {
      switch (policyChecked) {
        case "isStartDatePolicyChecked":
          this.setFlagsWhenStartDatePolicyIsChecked();
          break;
        case "isEndDatePolicyChecked":
          this.setFlagsWhenEndDatePolicyIsChecked();
          break;
        case "isEndAgreementOffsetPolicyChecked":
          this.setFlagsWhenEndAgreementOffsetPolicyIsChecked();
          break;
      }
    } else {
      this.resetDisabledFlags();

      // Apply the flag settings according to the checked time policies
      if (this.isEndDatePolicyChecked) {
        this.setFlagsWhenEndDatePolicyIsChecked();
      }
      if (this.isStartDatePolicyChecked) {
        this.setFlagsWhenStartDatePolicyIsChecked();
      }
      if (this.isEndAgreementOffsetPolicyChecked) {
        this.setFlagsWhenEndAgreementOffsetPolicyIsChecked();
      }
    }
  }

  public getPolicies(): IEnforcementPolicyUnion[] {
    let policies: IEnforcementPolicyUnion[] = [];

    if (!this.isAnyPolicyChecked) {
      policies.push({
        "@type": "EverythingAllowedPolicy"
      } as IEverythingAllowedPolicy);
    } else {
      if (this.isParticipantRestrictionPolicyChecked) {
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

  private resetDisabledFlags() {
    this.isStartDatePolicyDisabled = false;
    this.isEndDatePolicyDisabled = false;
    this.isEndAgreementOffsetPolicyDisabled = false;
  }

  public resetEnforcementPolicyForm() {
    this.isParticipantRestrictionPolicyChecked = false;
    this.participantRestrictionPolicyIds = [''];
    this.isStartDatePolicyChecked = false;
    this.startDate = undefined;
    this.isEndDatePolicyChecked = false;
    this.endDate = undefined;
    this.isEndAgreementOffsetPolicyChecked = false;
    this.endAgreementOffset = undefined;
    this.endAgreementOffsetUnit = undefined;
    this.resetDisabledFlags()
    this.resetAccordion();
  }
}
