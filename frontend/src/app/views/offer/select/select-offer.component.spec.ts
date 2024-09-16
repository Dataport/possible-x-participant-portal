import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectOfferComponent } from './select-offer.component';
import { ApiService } from "../../../services/mgmt/api/api.service";
import { IOfferDetailsTO } from "../../../services/mgmt/api/backend";
import { StatusMessageComponent } from "../../common-views/status-message/status-message.component";
import { Component, NO_ERRORS_SCHEMA } from "@angular/core";
import { first } from "rxjs";

describe('SelectOfferComponent', () => {
  let component: SelectOfferComponent;
  let fixture: ComponentFixture<SelectOfferComponent>;
  let apiService: jasmine.SpyObj<ApiService>;

  const offerDetails = {
    edcOfferId: 'dummy',
    counterPartyAddress: 'dummy',
    offerType: 'dummy',
    creationDate: new Date(Date.now()),
    name: 'dummy',
    description: 'dummy',
    contentType: 'dummy'
  } as IOfferDetailsTO;

  @Component({
    selector: 'app-status-message',
    template: ''
  })
  class MockStatusMessageComponent implements Partial<StatusMessageComponent>{
    public showInfoMessage() {}
    public showSuccessMessage() {}
    public showErrorMessage() {}
  }

  beforeEach(async () => {
    const apiServiceSpy = jasmine.createSpyObj('ApiService', ['selectContractOffer']);

    await TestBed.configureTestingModule({
      declarations: [SelectOfferComponent, MockStatusMessageComponent],
      providers: [
        { provide: ApiService, useValue: apiServiceSpy }
      ],
      schemas: [NO_ERRORS_SCHEMA],
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
    component.selectedOffer.pipe(first())
      .subscribe((offer) => expect(offer).toEqual(offerDetails));

    component.selectOffer();

    expect(apiService.selectContractOffer).toHaveBeenCalled();
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
