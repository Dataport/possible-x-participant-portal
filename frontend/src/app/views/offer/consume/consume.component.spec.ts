import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsumeComponent } from './consume.component';
import { AcceptOfferComponent } from '../accept/accept-offer.component';
import { ApiService } from '../../../services/mgmt/api/api.service';
import { IOfferDetailsTO } from '../../../services/mgmt/api/backend';
import { CommonViewsModule } from '../../common-views/common-views.module';
import { BadgeComponent, AccordionComponent, AccordionItemComponent } from '@coreui/angular';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('ConsumeComponent', () => {
  let component: ConsumeComponent;
  let fixture: ComponentFixture<ConsumeComponent>;
  let apiService: jasmine.SpyObj<ApiService>;

  const offerDetails = {
    offerId: 'dummy',
    offerType: 'dummy',
    creationDate: new Date(Date.now()),
    name: 'dummy',
    description: 'dummy',
    contentType: 'dummy'
  } as IOfferDetailsTO;

  beforeEach(async () => {
    const apiServiceSpy = jasmine.createSpyObj('ApiService', ['selectContractOffer']);

    await TestBed.configureTestingModule({
      declarations: [ConsumeComponent, AcceptOfferComponent ],
      providers: [
        { provide: ApiService, useValue: apiServiceSpy }
      ],
      imports: [ CommonViewsModule , BadgeComponent, AccordionComponent, AccordionItemComponent, BrowserAnimationsModule ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConsumeComponent);
    component = fixture.componentInstance;
    apiService = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call apiService on acceptContractOffer', () => {
    const mockResponse = Promise.resolve(offerDetails);
    apiService.selectContractOffer.and.returnValue(mockResponse);

    component.selectOffer();

    expect(apiService.selectContractOffer).toHaveBeenCalled();
  });

  it('should deselect offer', () => {
    component.selectedOffer = offerDetails;

    component.deselectOffer();

    expect(component.selectedOffer).toBeUndefined();
  });
});
