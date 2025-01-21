import {AfterViewInit, ChangeDetectorRef, Component, ViewChild} from '@angular/core';
import {AccordionItemComponent} from "@coreui/angular";
import {NameMappingService} from "../../../services/mgmt/name-mapping.service";
import moment from "moment";
import {
  IAgreementOffsetUnit,
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
  isStartDatePolicyDisabled: boolean = false;
  isStartDatePolicyChecked: boolean = false;
  startDate: Date = undefined;
  isEndDatePolicyDisabled: boolean = false;
  isEndDatePolicyChecked: boolean = false;
  endDate: Date = undefined;
  isStartAgreementOffsetPolicyDisabled: boolean = false;
  isStartAgreementOffsetPolicyChecked: boolean = false;
  startAgreementOffset: number = undefined;
  startAgreementOffsetUnit: IAgreementOffsetUnit = undefined;
  isEndAgreementOffsetPolicyDisabled: boolean = false;
  isEndAgreementOffsetPolicyChecked: boolean = false;
  endAgreementOffset: number = undefined;
  endAgreementOffsetUnit: IAgreementOffsetUnit = undefined;
  @ViewChild('accordionItem1') accordionItem1!: AccordionItemComponent;
  @ViewChild('accordionItem2') accordionItem2!: AccordionItemComponent;
  @ViewChild('accordionItem3') accordionItem3!: AccordionItemComponent;
  @ViewChild('accordionItem4') accordionItem4!: AccordionItemComponent;
  @ViewChild('accordionItem5') accordionItem5!: AccordionItemComponent;

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

  protected get isInvalidStartAgreementOffsetPolicy(): boolean {
    return this.isStartAgreementOffsetPolicyChecked && (!this.isValidOffset(this.startAgreementOffset) || !this.isValidOffsetUnit(this.startAgreementOffsetUnit));
  }

  protected get isInvalidEndAgreementOffsetPolicy(): boolean {
    return this.isEndAgreementOffsetPolicyChecked && (!this.isValidOffset(this.endAgreementOffset) || !this.isValidOffsetUnit(this.endAgreementOffsetUnit));
  }

  public get isAnyPolicyInvalid(): boolean {
    return this.isInvalidParticipantRestrictionPolicy || this.isInvalidStartDatePolicy || this.isInvalidEndDatePolicy
      || this.isInvalidStartAgreementOffsetPolicy || this.isInvalidEndAgreementOffsetPolicy;
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
      || this.isStartAgreementOffsetPolicyChecked || this.isEndAgreementOffsetPolicyChecked;
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

  protected computeCheckBoxStates() {
    if (this.isStartDatePolicyChecked) {
      this.isStartDatePolicyDisabled = false;
      this.isEndDatePolicyDisabled = false;
      this.isEndAgreementOffsetPolicyDisabled = true; // Term of Contract
      this.isStartAgreementOffsetPolicyDisabled = true; // Term until Valid
    }

    if (this.isEndDatePolicyChecked) {
      this.isStartDatePolicyDisabled = false;
      this.isEndDatePolicyDisabled = false;
      this.isEndAgreementOffsetPolicyDisabled = true; // Term of Contract
      this.isStartAgreementOffsetPolicyDisabled = false; // Term until Valid
    }

    if (this.isEndAgreementOffsetPolicyChecked) {
      this.isStartDatePolicyDisabled = true;
      this.isEndDatePolicyDisabled = true;
      this.isEndAgreementOffsetPolicyDisabled = false; // Term of Contract
      this.isStartAgreementOffsetPolicyDisabled = true; // Term until Valid
    }

    if (this.isStartAgreementOffsetPolicyChecked) {
      this.isStartDatePolicyDisabled = true;
      this.isEndDatePolicyDisabled = false;
      this.isEndAgreementOffsetPolicyDisabled = true; // Term of Contract
      this.isStartAgreementOffsetPolicyDisabled = false; // Term until Valid
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

      if (this.isStartDatePolicyChecked) {
        policies.push({
          "@type": "StartDatePolicy",
          date: this.startDate.toISOString()
        } as any);
      }

      if (this.isEndDatePolicyChecked) {
        policies.push({
          "@type": "EndDatePolicy",
          date: this.endDate.toISOString()
        } as any);
      }
    }

    return policies;
  }

  private resetAccordion() {
    this.accordionItem1.visible = false;
    this.accordionItem2.visible = false;
    this.accordionItem3.visible = false;
    this.accordionItem4.visible = false;
    this.accordionItem5.visible = false;
  }

  public resetEnforcementPolicyForm() {
    this.isParticipantRestrictionPolicyChecked = false;
    this.participantRestrictionPolicyIds = [''];
    this.isStartDatePolicyDisabled = false;
    this.isStartDatePolicyChecked = false;
    this.startDate = undefined;
    this.isEndDatePolicyDisabled = false;
    this.isEndDatePolicyChecked = false;
    this.endDate = undefined;
    this.isStartAgreementOffsetPolicyDisabled = false;
    this.isStartAgreementOffsetPolicyChecked = false;
    this.startAgreementOffset = undefined;
    this.startAgreementOffsetUnit = undefined;
    this.isEndAgreementOffsetPolicyDisabled = false;
    this.isEndAgreementOffsetPolicyChecked = false;
    this.endAgreementOffset = undefined;
    this.endAgreementOffsetUnit = undefined;
    this.resetAccordion();
  }
}
