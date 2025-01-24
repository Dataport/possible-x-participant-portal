import { ComponentFixture, TestBed } from '@angular/core/testing';
import {AccordionModule} from "@coreui/angular";
import {provideAnimations} from "@angular/platform-browser/animations";
import { PossibleXEnforcedPolicySelectorComponent } from './possible-x-enforced-policy-selector.component';
import {NameMappingService} from "../../../services/mgmt/name-mapping.service";
import {IEverythingAllowedPolicy} from "../../../services/mgmt/api/backend";

describe('PossibleXEnforcedPolicySelectorComponent', () => {
  let component: PossibleXEnforcedPolicySelectorComponent;
  let fixture: ComponentFixture<PossibleXEnforcedPolicySelectorComponent>;
  let nameMappingService: jasmine.SpyObj<NameMappingService>;

  beforeEach(() => {

    const nameMappingServiceSpy = jasmine.createSpyObj('NameMappingService', ['getNameById', 'getNameMapping']);

    // Mock return values for nameMappingServiceSpy methods
    nameMappingServiceSpy.getNameById.and.returnValue('Test Name');
    nameMappingServiceSpy.getNameMapping.and.returnValue({ '123': 'Test Name' });

    TestBed.configureTestingModule({
      declarations: [PossibleXEnforcedPolicySelectorComponent],
      providers: [
        { provide: NameMappingService, useValue: nameMappingServiceSpy },
        provideAnimations()
      ],
      imports: [AccordionModule]
    });
    fixture = TestBed.createComponent(PossibleXEnforcedPolicySelectorComponent);
    component = fixture.componentInstance;
    nameMappingService = TestBed.inject(NameMappingService) as jasmine.SpyObj<NameMappingService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return the correct policies based on the checked policies - none', () => {
    component.isParticipantRestrictionPolicyChecked = false;
    component.isStartDatePolicyChecked = false;
    component.isEndDatePolicyChecked = false;
    component.isEndAgreementOffsetPolicyChecked = false;

    const policies = component.getPolicies();

    expect(policies).toEqual([
      { "@type": "EverythingAllowedPolicy" } as IEverythingAllowedPolicy
    ]);
  });

  it('should return the correct policies based on the checked policies - participant restriction, start and end date policy', () => {
    component.isParticipantRestrictionPolicyChecked = true;
    component.participantRestrictionPolicyIds = ['validId'];
    component.isStartDatePolicyChecked = true;
    component.startDate = new Date('2024-01-01T00:00:00Z');
    component.isEndDatePolicyChecked = true;
    component.endDate = new Date('2024-12-31T23:59:59Z');
    component.isEndAgreementOffsetPolicyChecked = false;
    component.endAgreementOffset = 10;
    component.endAgreementOffsetUnit = 'd';

    const policies = component.getPolicies();

    expect(policies).toEqual([
      { "@type": "ParticipantRestrictionPolicy", allowedParticipants: ['validId'] },
      { "@type": "StartDatePolicy", date: '2024-01-01T00:00:00.000Z' } as any,
      { "@type": "EndDatePolicy", date: '2024-12-31T23:59:59.000Z' } as any
    ]);
  });

  it('should return the correct policies based on the checked policies - participant restriction and transfer period policy', () => {
    component.isParticipantRestrictionPolicyChecked = true;
    component.participantRestrictionPolicyIds = ['validId'];
    component.isStartDatePolicyChecked = false;
    component.startDate = new Date('2024-01-01T00:00:00Z');
    component.isEndDatePolicyChecked = false;
    component.endDate = new Date('2024-12-31T23:59:59Z');
    component.isEndAgreementOffsetPolicyChecked = true;
    component.endAgreementOffset = 10;
    component.endAgreementOffsetUnit = 'd';

    const policies = component.getPolicies();

    expect(policies).toEqual([
      { "@type": "ParticipantRestrictionPolicy", allowedParticipants: ['validId'] },
      { "@type": "EndAgreementOffsetPolicy", offsetNumber: 10, offsetUnit: 'd' } as any
    ]);
  });

  it('should return true if any policy is invalid', () => {
      component.isParticipantRestrictionPolicyChecked = true;
      component.participantRestrictionPolicyIds = [''];
      expect(component.isAnyPolicyInvalid).toBeTrue();

      component.isParticipantRestrictionPolicyChecked = false;
      component.isStartDatePolicyChecked = true;
      component.startDate = new Date('invalid date');
      expect(component.isAnyPolicyInvalid).toBeTrue();

      component.isStartDatePolicyChecked = false;
      component.isEndDatePolicyChecked = true;
      component.endDate = new Date('invalid date');
      expect(component.isAnyPolicyInvalid).toBeTrue();

      component.isEndDatePolicyChecked = false;
      component.isEndAgreementOffsetPolicyChecked = true;
      component.endAgreementOffset = -1;
      component.endAgreementOffsetUnit = 'd';
      expect(component.isAnyPolicyInvalid).toBeTrue();

      component.isParticipantRestrictionPolicyChecked = true;
      component.isStartDatePolicyChecked = true;
      component.isEndDatePolicyChecked = true;
      expect(component.isAnyPolicyInvalid).toBeTrue();
    });

  it('should return false if no policy is invalid', () => {
    component.isParticipantRestrictionPolicyChecked = true;
    component.participantRestrictionPolicyIds = ['validId'];
    expect(component.isAnyPolicyInvalid).toBeFalse();

    component.isParticipantRestrictionPolicyChecked = false;
    component.isStartDatePolicyChecked = true;
    component.startDate = new Date();
    expect(component.isAnyPolicyInvalid).toBeFalse();

    component.isStartDatePolicyChecked = false;
    component.isEndDatePolicyChecked = true;
    component.endDate = new Date();
    expect(component.isAnyPolicyInvalid).toBeFalse();

    component.isEndDatePolicyChecked = false;
    component.isEndAgreementOffsetPolicyChecked = true;
    component.endAgreementOffset = 1;
    component.endAgreementOffsetUnit = 'd';
    expect(component.isAnyPolicyInvalid).toBeFalse();

    component.isParticipantRestrictionPolicyChecked = true;
    component.isStartDatePolicyChecked = true;
    component.isEndDatePolicyChecked = true;
    expect(component.isAnyPolicyInvalid).toBeFalse();
  });

  it('should return true if some policies are valid and some are not', () => {
    component.isParticipantRestrictionPolicyChecked = true;
    component.participantRestrictionPolicyIds = ['validId', ''];
    component.isStartDatePolicyChecked = true;
    component.startDate = new Date();
    component.isEndDatePolicyChecked = true;
    component.endDate = new Date('invalid date');
    component.isEndAgreementOffsetPolicyChecked = true;
    component.endAgreementOffset = 10;
    component.endAgreementOffsetUnit = 'd';

    expect(component.isAnyPolicyInvalid).toBeTrue();
  });

  it('should reset the enforcement policy form - service offering', () => {
    component.isOfferingDataOffering = false;
    fixture.detectChanges();
    component.isParticipantRestrictionPolicyChecked = true;
    component.participantRestrictionPolicyIds = ['validId'];
    component.isStartDatePolicyChecked = true;
    component.startDate = new Date('2024-01-01T00:00:00Z');
    component.isEndDatePolicyChecked = true;
    component.endDate = new Date('2024-12-31T23:59:59Z');
    component.isEndAgreementOffsetPolicyChecked = true;
    component.endAgreementOffset = 10;
    component.endAgreementOffsetUnit = 'd';

    component.resetEnforcementPolicyForm();

    expect(component.isParticipantRestrictionPolicyChecked).toBeFalse();
    expect(component.participantRestrictionPolicyIds).toEqual(['']);
    expect(component.isStartDatePolicyChecked).toBeFalse();
    expect(component.startDate).toBeUndefined();
    expect(component.isEndDatePolicyChecked).toBeFalse();
    expect(component.endDate).toBeUndefined();
    expect(component.isEndAgreementOffsetPolicyChecked).toBeFalse();
    expect(component.endAgreementOffset).toBeUndefined();
    expect(component.endAgreementOffsetUnit).toBeUndefined();
    expect(component.isStartDatePolicyDisabled).toBeFalse();
    expect(component.isEndDatePolicyDisabled).toBeFalse();
    expect(component.isEndAgreementOffsetPolicyDisabled).toBeFalse();
    expect(component.participantRestrictionPolicyAccordionItem.visible).toBeFalse();
    expect(component.startDatePolicyAccordionItem.visible).toBeFalse();
    expect(component.endDatePolicyAccordionItem.visible).toBeFalse();
  });

  it('should reset the enforcement policy form - data service offering', () => {
    component.isOfferingDataOffering = true;
    fixture.detectChanges();
    component.isParticipantRestrictionPolicyChecked = true;
    component.participantRestrictionPolicyIds = ['validId'];
    component.isStartDatePolicyChecked = true;
    component.startDate = new Date('2024-01-01T00:00:00Z');
    component.isEndDatePolicyChecked = true;
    component.endDate = new Date('2024-12-31T23:59:59Z');
    component.isEndAgreementOffsetPolicyChecked = true;
    component.endAgreementOffset = 10;
    component.endAgreementOffsetUnit = 'd';

    component.resetEnforcementPolicyForm();

    expect(component.isParticipantRestrictionPolicyChecked).toBeFalse();
    expect(component.participantRestrictionPolicyIds).toEqual(['']);
    expect(component.isStartDatePolicyChecked).toBeFalse();
    expect(component.startDate).toBeUndefined();
    expect(component.isEndDatePolicyChecked).toBeFalse();
    expect(component.endDate).toBeUndefined();
    expect(component.isEndAgreementOffsetPolicyChecked).toBeFalse();
    expect(component.endAgreementOffset).toBeUndefined();
    expect(component.endAgreementOffsetUnit).toBeUndefined();
    expect(component.isStartDatePolicyDisabled).toBeFalse();
    expect(component.isEndDatePolicyDisabled).toBeFalse();
    expect(component.isEndAgreementOffsetPolicyDisabled).toBeFalse();
    expect(component.participantRestrictionPolicyAccordionItem.visible).toBeFalse();
    expect(component.startDatePolicyAccordionItem.visible).toBeFalse();
    expect(component.endDatePolicyAccordionItem.visible).toBeFalse();
    expect(component.endAgreementOffsetPolicyAccordionItem.visible).toBeFalse();
  });

  it('should return name and ID string', () => {
    const id = '123';
    const name = 'Test Name';
    nameMappingService.getNameById.and.returnValue(name);

    const result = component.getNameIdStringById(id);
    expect(result).toBe(`${name} (${id})`);
  });
});
