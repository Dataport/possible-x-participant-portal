import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PossibleXEnforcedPolicySelectorComponent } from './possible-x-enforced-policy-selector.component';

describe('PossibleXEnforcedPolicySelectorComponent', () => {
  let component: PossibleXEnforcedPolicySelectorComponent;
  let fixture: ComponentFixture<PossibleXEnforcedPolicySelectorComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PossibleXEnforcedPolicySelectorComponent]
    });
    fixture = TestBed.createComponent(PossibleXEnforcedPolicySelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
