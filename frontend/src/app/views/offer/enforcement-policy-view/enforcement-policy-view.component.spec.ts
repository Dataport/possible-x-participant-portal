import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnforcementPolicyViewComponent } from './enforcement-policy-view.component';

describe('EnforcementPolicyViewComponent', () => {
  let component: EnforcementPolicyViewComponent;
  let fixture: ComponentFixture<EnforcementPolicyViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EnforcementPolicyViewComponent]
    });
    fixture = TestBed.createComponent(EnforcementPolicyViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
