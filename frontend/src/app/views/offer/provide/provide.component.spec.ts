import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ProvideComponent} from './provide.component';
import {
  OfferingWizardExtensionComponent
} from "../../../wizard-extension/offering-wizard-extension/offering-wizard-extension.component";
import {GridModule} from "@coreui/angular";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {Component} from "@angular/core";

@Component({
  selector: 'app-offering-wizard-extension',
  template: ''
})
class MockWizardExtension implements Partial<OfferingWizardExtensionComponent>{}

describe('ProvideComponent', () => {
  let component: ProvideComponent;
  let fixture: ComponentFixture<ProvideComponent>;

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      declarations: [ProvideComponent, MockWizardExtension],
      imports: [GridModule, HttpClientTestingModule]
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
