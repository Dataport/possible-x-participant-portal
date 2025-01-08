import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AccordionModule } from "@coreui/angular";
import { provideAnimations } from "@angular/platform-browser/animations";
import { ServiceOfferingPolicyHintsComponent } from './service-offering-policy-hints.component';

describe('ServiceOfferingPolicyHintsComponent', () => {
  let component: ServiceOfferingPolicyHintsComponent;
  let fixture: ComponentFixture<ServiceOfferingPolicyHintsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ServiceOfferingPolicyHintsComponent],
      providers: [
        provideAnimations()
      ],
      imports: [AccordionModule]
    });
    fixture = TestBed.createComponent(ServiceOfferingPolicyHintsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
