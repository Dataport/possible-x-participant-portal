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
    component.checkboxFormGroup.get(component.startDatePolicyCB).setValue(false);
    component.checkboxFormGroup.get(component.endDatePolicyCB).setValue(false);
    component.checkboxFormGroup.get(component.endAgreementOffsetPolicyCB).setValue(false);
    component.checkboxFormGroup.get(component.participantRestrictionPolicyCB).setValue(false);

    const policies = component.getPolicies();

    expect(policies).toEqual([
      { "@type": "EverythingAllowedPolicy" } as IEverythingAllowedPolicy
    ]);
  });

  it('should return the correct policies based on the checked policies - participant restriction, start and end date policy', () => {
    component.checkboxFormGroup.get(component.participantRestrictionPolicyCB).setValue(true);
    component.participantRestrictionPolicyIds = ['validId'];
    component.checkboxFormGroup.get(component.startDatePolicyCB).setValue(true);
    component.startDate = new Date('2024-01-01T00:00:00Z');
    component.checkboxFormGroup.get(component.endDatePolicyCB).setValue(true);
    component.endDate = new Date('2024-12-31T23:59:59Z');
    component.checkboxFormGroup.get(component.endAgreementOffsetPolicyCB).setValue(false);
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
    component.checkboxFormGroup.get(component.participantRestrictionPolicyCB).setValue(true);
    component.participantRestrictionPolicyIds = ['validId'];
    component.checkboxFormGroup.get(component.startDatePolicyCB).setValue(false);
    component.startDate = new Date('2024-01-01T00:00:00Z');
    component.checkboxFormGroup.get(component.endDatePolicyCB).setValue(false);
    component.endDate = new Date('2024-12-31T23:59:59Z');
    component.checkboxFormGroup.get(component.endAgreementOffsetPolicyCB).setValue(true);
    component.endAgreementOffset = 10;
    component.endAgreementOffsetUnit = 'd';

    const policies = component.getPolicies();

    expect(policies).toEqual([
      { "@type": "ParticipantRestrictionPolicy", allowedParticipants: ['validId'] },
      { "@type": "EndAgreementOffsetPolicy", offsetNumber: 10, offsetUnit: 'd' } as any
    ]);
  });

  it('should return true if any policy is invalid', () => {
    component.checkboxFormGroup.get(component.participantRestrictionPolicyCB).setValue(true);
    component.participantRestrictionPolicyIds = [''];
    expect(component.isAnyPolicyInvalid).toBeTrue();

    component.checkboxFormGroup.get(component.participantRestrictionPolicyCB).setValue(false);
    component.checkboxFormGroup.get(component.startDatePolicyCB).setValue(true);
    component.startDate = new Date('invalid date');
    expect(component.isAnyPolicyInvalid).toBeTrue();

    component.checkboxFormGroup.get(component.startDatePolicyCB).setValue(false);
    component.checkboxFormGroup.get(component.endDatePolicyCB).setValue(true);
    component.endDate = new Date('invalid date');
    expect(component.isAnyPolicyInvalid).toBeTrue();

    component.checkboxFormGroup.get(component.endDatePolicyCB).setValue(false);
    component.checkboxFormGroup.get(component.endAgreementOffsetPolicyCB).setValue(true);
    component.endAgreementOffset = -1;
    component.endAgreementOffsetUnit = 'd';
    expect(component.isAnyPolicyInvalid).toBeTrue();

    component.checkboxFormGroup.get(component.participantRestrictionPolicyCB).setValue(true);
    component.checkboxFormGroup.get(component.startDatePolicyCB).setValue(true);
    component.checkboxFormGroup.get(component.endDatePolicyCB).setValue(true);
    expect(component.isAnyPolicyInvalid).toBeTrue();
  });

  it('should return false if no policy is invalid', () => {
    component.checkboxFormGroup.get(component.participantRestrictionPolicyCB).setValue(true);
    component.participantRestrictionPolicyIds = ['validId'];
    expect(component.isAnyPolicyInvalid).toBeFalse();

    component.checkboxFormGroup.get(component.participantRestrictionPolicyCB).setValue(false);
    component.checkboxFormGroup.get(component.startDatePolicyCB).setValue(true);
    component.startDate = new Date();
    expect(component.isAnyPolicyInvalid).toBeFalse();

    component.checkboxFormGroup.get(component.startDatePolicyCB).setValue(false);
    component.checkboxFormGroup.get(component.endDatePolicyCB).setValue(true);
    component.endDate = new Date();
    expect(component.isAnyPolicyInvalid).toBeFalse();

    component.checkboxFormGroup.get(component.endDatePolicyCB).setValue(false);
    component.checkboxFormGroup.get(component.endAgreementOffsetPolicyCB).setValue(true);
    component.endAgreementOffset = 1;
    component.endAgreementOffsetUnit = 'd';
    expect(component.isAnyPolicyInvalid).toBeFalse();

    component.checkboxFormGroup.get(component.participantRestrictionPolicyCB).setValue(true);
    component.checkboxFormGroup.get(component.startDatePolicyCB).setValue(true);
    component.checkboxFormGroup.get(component.endDatePolicyCB).setValue(true);
    expect(component.isAnyPolicyInvalid).toBeFalse();
  });

  it('should return true if some policies are valid and some are not', () => {
    component.checkboxFormGroup.get(component.participantRestrictionPolicyCB).setValue(true);
    component.participantRestrictionPolicyIds = ['validId', ''];
    component.checkboxFormGroup.get(component.startDatePolicyCB).setValue(true);
    component.startDate = new Date();
    component.checkboxFormGroup.get(component.endDatePolicyCB).setValue(true);
    component.endDate = new Date('invalid date');
    component.checkboxFormGroup.get(component.endAgreementOffsetPolicyCB).setValue(true);
    component.endAgreementOffset = 10;
    component.endAgreementOffsetUnit = 'd';

    expect(component.isAnyPolicyInvalid).toBeTrue();
  });

  it('should reset the enforcement policy form - service offering', () => {
    component.isOfferingDataOffering = false;
    fixture.detectChanges();
    component.checkboxFormGroup.get(component.participantRestrictionPolicyCB).setValue(true);
    component.participantRestrictionPolicyIds = ['validId'];
    component.checkboxFormGroup.get(component.startDatePolicyCB).setValue(true);
    component.startDate = new Date('2024-01-01T00:00:00Z');
    component.checkboxFormGroup.get(component.endDatePolicyCB).setValue(true);
    component.endDate = new Date('2024-12-31T23:59:59Z');
    component.checkboxFormGroup.get(component.endAgreementOffsetPolicyCB).setValue(true);
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
    component.checkboxFormGroup.get(component.participantRestrictionPolicyCB).setValue(true);
    component.participantRestrictionPolicyIds = ['validId'];
    component.checkboxFormGroup.get(component.startDatePolicyCB).setValue(true);
    component.startDate = new Date('2024-01-01T00:00:00Z');
    component.checkboxFormGroup.get(component.endDatePolicyCB).setValue(true);
    component.endDate = new Date('2024-12-31T23:59:59Z');
    component.checkboxFormGroup.get(component.endAgreementOffsetPolicyCB).setValue(true);
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
