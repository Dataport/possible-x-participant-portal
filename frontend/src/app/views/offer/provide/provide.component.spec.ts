import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ProvideComponent} from './provide.component';
import {
  OfferingWizardExtensionComponent
} from "../../../wizard-extension/offering-wizard-extension/offering-wizard-extension.component";


describe('ProvideComponent', () => {
  let component: ProvideComponent;
  let fixture: ComponentFixture<ProvideComponent>;

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      declarations: [ProvideComponent],
      imports: [OfferingWizardExtensionComponent],
    })
      .compileComponents();


    fixture = TestBed.createComponent(ProvideComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });


  it('should create', () => {
    expect(component).toBeTruthy();
  });

});
