import {ComponentFixture, TestBed} from '@angular/core/testing';

import {HttpClientTestingModule} from '@angular/common/http/testing';

import {EnforcementPolicyAccordionViewComponent} from './enforcement-policy-accordion-view.component';

import {MatExpansionModule} from '@angular/material/expansion';

import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import {
  IEndDatePolicy,
  IEnforcementPolicy,
  IParticipantRestrictionPolicy
} from "../../../services/mgmt/api/backend";
import {EnforcementPolicyContentComponent} from "./enforcement-policy-content/enforcement-policy-content.component";

describe('EnforcementPolicyViewComponent', () => {
  let component: EnforcementPolicyAccordionViewComponent;
  let fixture: ComponentFixture<EnforcementPolicyAccordionViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EnforcementPolicyAccordionViewComponent, EnforcementPolicyContentComponent],
      imports: [MatExpansionModule, BrowserAnimationsModule, HttpClientTestingModule]
    });
    fixture = TestBed.createComponent(EnforcementPolicyAccordionViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set input properties correctly', () => {
    const policies: IEnforcementPolicy[] = [{ "@type": "EverythingAllowedPolicy", "valid": true }];
    component.enforcementPolicies = policies;
    component.showValidity = true;
    fixture.detectChanges();

    expect(component.enforcementPolicies).toBe(policies);
    expect(component.showValidity).toBeTrue();
  });

  it('should detect invalid policies', () => {
    component.enforcementPolicies = [
      {"@type": "EndDatePolicy", "valid": false, "date": new Date()} as IEndDatePolicy,
      {
        "@type": "ParticipantRestrictionPolicy",
        "valid": true,
        "allowedParticipants": ["org1", "org2"]
      } as IParticipantRestrictionPolicy
    ];
    fixture.detectChanges();

    expect(component.isAnyPolicyInvalid).toBeTrue();
  });

  it('should detect no invalid policies', () => {
    component.enforcementPolicies = [
      {"@type": "EndDatePolicy", "valid": true, "date": new Date()} as IEndDatePolicy,
      {
        "@type": "ParticipantRestrictionPolicy",
        "valid": true,
        "allowedParticipants": ["org1", "org2"]
      } as IParticipantRestrictionPolicy
    ];
    fixture.detectChanges();

    expect(component.isAnyPolicyInvalid).toBeFalse();
  });
});
