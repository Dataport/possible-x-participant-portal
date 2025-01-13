import {ComponentFixture, TestBed} from '@angular/core/testing';

import {HttpClientTestingModule} from '@angular/common/http/testing';

import {EnforcementPolicyContentComponent} from './enforcement-policy-content.component';

describe('EnforcementPolicyContentComponent', () => {
  let component: EnforcementPolicyContentComponent;
  let fixture: ComponentFixture<EnforcementPolicyContentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EnforcementPolicyContentComponent],
      imports: [HttpClientTestingModule]
    });
    fixture = TestBed.createComponent(EnforcementPolicyContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
