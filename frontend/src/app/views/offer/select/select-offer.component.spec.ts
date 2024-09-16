import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectOfferComponent } from './select-offer.component';
import { ApiService } from "../../../services/mgmt/api/api.service";
import { IOfferDetailsTO } from "../../../services/mgmt/api/backend";

describe('SelectOfferComponent', () => {
  let component: SelectOfferComponent;
  let fixture: ComponentFixture<SelectOfferComponent>;
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
      declarations: [SelectOfferComponent],
      providers: [
        { provide: ApiService, useValue: apiServiceSpy }
      ],
    })
    .compileComponents();

    fixture = TestBed.createComponent(SelectOfferComponent);
    component = fixture.componentInstance;
    apiService = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should emit selected offer', () => {
    const mockResponse = Promise.resolve(offerDetails);
    apiService.selectContractOffer.and.returnValue(mockResponse);
    spyOn(component.selectedOffer, 'emit');

    component.selectOffer();

    expect(apiService.selectContractOffer).toHaveBeenCalled();
    expect(component.selectedOffer.emit).toHaveBeenCalledWith(offerDetails);
  });

  it('should validate input field', () => {
    const offerIdControl = component.selectionForm.controls.offerId;

    offerIdControl.setValue('');
    expect(offerIdControl.valid).toBeFalsy();

    offerIdControl.setValue('asdf');
    expect(offerIdControl.valid).toBeTruthy();

    offerIdControl.setValue('asdf#');
    expect(offerIdControl.valid).toBeFalsy();
    expect(offerIdControl.hasError('Wrong format')).toBeTruthy();
  });

});
