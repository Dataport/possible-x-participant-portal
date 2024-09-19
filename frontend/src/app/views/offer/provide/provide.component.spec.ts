import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ProvideComponent} from './provide.component';
import {ApiService} from '../../../services/mgmt/api/api.service';
import {GridModule} from '@coreui/angular';
import {FormsModule} from '@angular/forms';
import {CommonViewsModule} from '../../common-views/common-views.module';
import {ICreateOfferResponseTO} from "../../../services/mgmt/api/backend";
import {WizardAppModule} from "../../../sdwizard/wizardapp.module";
import {AppModule} from "../../../app.module";
import {WizardExtensionModule} from "../../../wizard-extension/wizard-extension.module";


describe('ProvideComponent', () => {
  let component: ProvideComponent;
  let fixture: ComponentFixture<ProvideComponent>;
  let apiService: jasmine.SpyObj<ApiService>;

  const offerCreationResponse = {
    edcResponseId: 'dummy',
    fhResponseId: 'dummy'
  } as ICreateOfferResponseTO;

  beforeEach(async () => {
    const apiServiceSpy = jasmine.createSpyObj('ApiService', ['createOffer']);


    await TestBed.configureTestingModule({
      declarations: [ProvideComponent],
      providers: [
        {provide: ApiService, useValue: apiServiceSpy}
      ],
      imports: [FormsModule, GridModule, CommonViewsModule, WizardAppModule, WizardExtensionModule],
    })
      .compileComponents();


    fixture = TestBed.createComponent(ProvideComponent);
    component = fixture.componentInstance;
    apiService = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    fixture.detectChanges();
  });


  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call createOffer on apiService when createOffer is called', async () => {
    const mockResponse = Promise.resolve(offerCreationResponse);
    apiService.createOffer.and.returnValue(mockResponse);

    component.policy = 'Everything is allowed';

    component.wizardExtension.prefillDone.subscribe((value) => {
      if (value) {
        component.wizardExtension.createOffer().then(() => {
          expect(apiService.createOffer).toHaveBeenCalled();
        });
      }
    });

  });

});
