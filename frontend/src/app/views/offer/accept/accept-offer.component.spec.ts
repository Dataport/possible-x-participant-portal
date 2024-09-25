import { AcceptOfferComponent } from './accept-offer.component';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ApiService } from '../../../services/mgmt/api/api.service';
import { IAcceptOfferResponseTO } from '../../../services/mgmt/api/backend';
import { BadgeComponent , AccordionComponent, AccordionItemComponent } from '@coreui/angular';
import { CommonViewsModule } from '../../common-views/common-views.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('AcceptOfferComponent', () => {
  let component: AcceptOfferComponent;
  let fixture: ComponentFixture<AcceptOfferComponent>;
  let apiService: jasmine.SpyObj<ApiService>;

  beforeEach(async () => {
    const apiServiceSpy = jasmine.createSpyObj('ApiService', ['acceptContractOffer']);

    await TestBed.configureTestingModule({
      declarations: [AcceptOfferComponent],
      providers: [
        { provide: ApiService, useValue: apiServiceSpy }
      ],
      imports: [ BadgeComponent, AccordionComponent, AccordionItemComponent, CommonViewsModule, BrowserAnimationsModule ]
    }).compileComponents();

    fixture = TestBed.createComponent(AcceptOfferComponent);
    component = fixture.componentInstance;
    apiService = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call apiService on acceptContractOffer', () => {
    const mockResponse = Promise.resolve({ transferProcessState: "COMPLETED" } as IAcceptOfferResponseTO);
    apiService.acceptContractOffer.and.returnValue(mockResponse);

    component.acceptContractOffer();

    expect(apiService.acceptContractOffer).toHaveBeenCalled();
  });

  it('should emit event on Cancel button', () => {
    const spy = spyOn(component.dismiss, 'emit');

    component.cancel();

    expect(spy).toHaveBeenCalled();
  });
});
