import {ComponentFixture, TestBed} from '@angular/core/testing';

import {HttpClientTestingModule} from '@angular/common/http/testing';

import {EnforcementPolicyAccordionViewComponent} from './enforcement-policy-accordion-view.component';

import {MatExpansionModule} from '@angular/material/expansion';

import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import {NameMappingService} from "../../../services/mgmt/name-mapping.service";

describe('EnforcementPolicyViewComponent', () => {
  let component: EnforcementPolicyAccordionViewComponent;
  let fixture: ComponentFixture<EnforcementPolicyAccordionViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EnforcementPolicyAccordionViewComponent],
      imports: [MatExpansionModule, BrowserAnimationsModule, HttpClientTestingModule],
      providers: [NameMappingService]
    });
    fixture = TestBed.createComponent(EnforcementPolicyAccordionViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
