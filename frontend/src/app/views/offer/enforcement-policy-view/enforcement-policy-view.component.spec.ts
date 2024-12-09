import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnforcementPolicyViewComponent } from './enforcement-policy-view.component';

import { MatExpansionModule } from '@angular/material/expansion';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('EnforcementPolicyViewComponent', () => {
  let component: EnforcementPolicyViewComponent;
  let fixture: ComponentFixture<EnforcementPolicyViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EnforcementPolicyViewComponent],
      imports: [MatExpansionModule, BrowserAnimationsModule]
    });
    fixture = TestBed.createComponent(EnforcementPolicyViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
