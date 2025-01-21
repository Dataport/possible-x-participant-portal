import {AfterViewInit, ChangeDetectorRef, Component, ViewChild} from '@angular/core';
import {AccordionItemComponent} from "@coreui/angular";
import {NameMappingService} from "../../../services/mgmt/name-mapping.service";
import moment from "moment";
import {
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
  isContractBookingPolicyChecked: boolean = false;
  contractBookingPolicyIds: string[] = [''];
  isContractValidityStartPolicyChecked: boolean = false;
  contractValidityStartDate: Date = undefined;
  isContractValidityEndPolicyChecked: boolean = false;
  contractValidityEndDate: Date = undefined;
  nameMapping: { [key: string]: string } = {};
  sortedIds: string[] = [];
  @ViewChild('accordionItem1') accordionItem1!: AccordionItemComponent;
  @ViewChild('accordionItem2') accordionItem2!: AccordionItemComponent;
  @ViewChild('accordionItem3') accordionItem3!: AccordionItemComponent;

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

  protected get isInvalidContractBookingPolicy(): boolean {
    return this.isContractBookingPolicyChecked && this.contractBookingPolicyIds.some(id => !this.isFieldFilled(id));
  }

  protected get isInvalidContractValidityStartPolicy(): boolean {
    return this.isContractValidityStartPolicyChecked && !this.isValidDate(this.contractValidityStartDate);
  }

  protected get isInvalidContractValidityEndPolicy(): boolean {
    return this.isContractValidityEndPolicyChecked && !this.isValidDate(this.contractValidityEndDate);
  }

  public get isAnyPolicyInvalid(): boolean {
    return this.isInvalidContractBookingPolicy || this.isInvalidContractValidityStartPolicy || this.isInvalidContractValidityEndPolicy;
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
    return this.isContractBookingPolicyChecked || this.isContractValidityStartPolicyChecked || this.isContractValidityEndPolicyChecked;
  }

  protected addInput(): void {
    this.contractBookingPolicyIds.push('');
  }

  protected removeInput(index: number): void {
    if (this.contractBookingPolicyIds.length > 1) {
      this.contractBookingPolicyIds.splice(index, 1);
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

  public getPolicies(): IEnforcementPolicyUnion[] {
    let policies: IEnforcementPolicyUnion[] = [];

    if (!this.isAnyPolicyChecked) {
      policies.push({
        "@type": "EverythingAllowedPolicy"
      } as IEverythingAllowedPolicy);
    } else {
      if (this.isContractBookingPolicyChecked) {
        policies.push({
          "@type": "ParticipantRestrictionPolicy",
          allowedParticipants: Array.from(new Set(this.contractBookingPolicyIds))
        } as IParticipantRestrictionPolicy);
      }

      if (this.isContractValidityStartPolicyChecked) {
        policies.push({
          "@type": "StartDatePolicy",
          date: this.contractValidityStartDate.toISOString()
        } as any);
      }

      if (this.isContractValidityEndPolicyChecked) {
        policies.push({
          "@type": "EndDatePolicy",
          date: this.contractValidityEndDate.toISOString()
        } as any);
      }
    }

    return policies;
  }

  private resetAccordion() {
    this.accordionItem1.visible = false;
    this.accordionItem2.visible = false;
    this.accordionItem3.visible = false;
  }

  public resetEnforcementPolicyForm() {
    this.isContractBookingPolicyChecked = false;
    this.contractBookingPolicyIds = [''];
    this.isContractValidityStartPolicyChecked = false;
    this.contractValidityStartDate = undefined;
    this.isContractValidityEndPolicyChecked = false;
    this.contractValidityEndDate = undefined;
    this.resetAccordion();
  }
}
