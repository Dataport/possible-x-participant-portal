import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsumeComponent } from './consume.component';
import { ApiService } from '../../../services/mgmt/api/api.service';
import { IOfferDetailsTO, RestResponse } from '../../../services/mgmt/api/backend';

describe('ConsumeComponent', () => {
  let component: ConsumeComponent;
  let fixture: ComponentFixture<ConsumeComponent>;
  let apiService: jasmine.SpyObj<ApiService>;

  const offerDetails = {
    offerId: 'dummy',
    offerType: 'dummy',
    creationDate: Date.now(),
    name: 'dummy',
    description: 'dummy',
    contentType: 'dummy'
  } as IOfferDetailsTO;

  beforeEach(async () => {
    const apiServiceSpy = jasmine.createSpyObj('ApiService', ['acceptContractOffer']);

    await TestBed.configureTestingModule({
      declarations: [ConsumeComponent],
      providers: [
        { provide: ApiService, useValue: apiServiceSpy }
      ]
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
    apiService.selectContractOffer.and.returnValue(offerDetails as RestResponse<IOfferDetailsTO>);

    component.selectOffer();

    expect(apiService.selectContractOffer).toHaveBeenCalled();
  });

  it('should deselect offer', () => {
    component.selectedOffer = offerDetails;

    component.deselectOffer();

    expect(component.selectedOffer).toBeUndefined();
  });
});
