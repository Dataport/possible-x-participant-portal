import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HttpClientTestingModule } from '@angular/common/http/testing';

import { EnforcementPolicyViewComponent } from './enforcement-policy-view.component';

import { MatExpansionModule } from '@angular/material/expansion';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { NameMappingService } from "../../../services/mgmt/name-mapping.service";

describe('EnforcementPolicyViewComponent', () => {
  let component: EnforcementPolicyViewComponent;
  let fixture: ComponentFixture<EnforcementPolicyViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EnforcementPolicyViewComponent],
      imports: [MatExpansionModule, BrowserAnimationsModule, HttpClientTestingModule],
      providers: [NameMappingService]
    });
    fixture = TestBed.createComponent(EnforcementPolicyViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
