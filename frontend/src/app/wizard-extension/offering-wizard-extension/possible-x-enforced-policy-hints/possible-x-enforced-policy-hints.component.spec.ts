import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AccordionModule } from "@coreui/angular";
import { provideAnimations } from "@angular/platform-browser/animations";
import { PossibleXEnforcedPolicyHintsComponent } from './possible-x-enforced-policy-hints.component';

describe('PossibleXEnforcedPolicyHintsComponent', () => {
  let component: PossibleXEnforcedPolicyHintsComponent;
  let fixture: ComponentFixture<PossibleXEnforcedPolicyHintsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PossibleXEnforcedPolicyHintsComponent],
      providers: [
        provideAnimations()
      ],
      imports: [AccordionModule]
    });
    fixture = TestBed.createComponent(PossibleXEnforcedPolicyHintsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
