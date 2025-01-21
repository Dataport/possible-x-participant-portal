import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AccordionModule } from "@coreui/angular";
import { provideAnimations } from "@angular/platform-browser/animations";
import { DataResourcePolicyHintsComponent } from './data-resource-policy-hints.component';

describe('DataResourcePolicyHintsComponent', () => {
  let component: DataResourcePolicyHintsComponent;
  let fixture: ComponentFixture<DataResourcePolicyHintsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DataResourcePolicyHintsComponent],
      providers: [
        provideAnimations()
      ],
      imports: [AccordionModule]
    });
    fixture = TestBed.createComponent(DataResourcePolicyHintsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
