import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ConsumeComponent} from './consume.component';
import {
  IAcceptOfferResponseTO,
  INegotiationState,
  IOfferDetailsTO,
  IPxExtendedServiceOfferingCredentialSubject
} from '../../../services/mgmt/api/backend';
import {Component, NO_ERRORS_SCHEMA} from "@angular/core";
import {StatusMessageComponent} from "../../common-views/status-message/status-message.component";
import {SelectComponent} from "../select/select.component";
import {FormGroup} from "@angular/forms";
import {MatStepperModule} from "@angular/material/stepper";
import {AcceptComponent} from "../accept/accept.component";
import {TransferComponent} from "../transfer/transfer.component";
import {provideAnimations} from "@angular/platform-browser/animations";

describe('ConsumeComponent', () => {
  let component: ConsumeComponent;
  let fixture: ComponentFixture<ConsumeComponent>;

  const offerDetails = {
    edcOfferId: 'dummy',
    catalogOffering: {
      id: "catalogOfferingId",
      "gx:providedBy": {id: "providedBy"},
      "gx:aggregationOf": [],
      "gx:termsAndConditions": [],
      "gx:policy": ["policy"],
      "gx:dataProtectionRegime": [],
      "gx:dataAccountExport": [],
      "schema:name": "name",
      "schema:description": "description",
      "px:assetId": "assetId",
      "px:providerUrl": "providerUrl",
      "@context": {},
      "@type": []
    } as IPxExtendedServiceOfferingCredentialSubject,
    dataOffering: false
  } as IOfferDetailsTO;

  const negotiatedContract = {
    negotiationState: "ACCEPTED" as INegotiationState,
    contractAgreementId: "contractAgreementId",
    dataOffering: false
  } as IAcceptOfferResponseTO;

  @Component({
    selector: 'app-select-offer',
    template: ''
  })
  class MockSelectComponent implements Partial<SelectComponent>{
    selectionForm = {
      reset() {}
    } as FormGroup;
    queryCatalogStatusMessage = {
      hideAllMessages: () => {}
    } as StatusMessageComponent;
  }

  @Component({
    selector: 'app-accept-offer',
    template: ''
  })
  class MockAcceptComponent implements Partial<AcceptComponent>{
    acceptOfferStatusMessage = {
      hideAllMessages: () => {}
    } as StatusMessageComponent;
  }

  @Component({
    selector: 'app-transfer',
    template: ''
  })
  class MockTransferComponent implements Partial<TransferComponent>{
    dataTransferStatusMessage = {
      hideAllMessages: () => {}
    } as StatusMessageComponent;
  }

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      declarations: [ConsumeComponent, MockSelectComponent, MockAcceptComponent, MockTransferComponent],
      imports: [MatStepperModule],
      providers: [provideAnimations()],
      schemas: [NO_ERRORS_SCHEMA],
    })
      .compileComponents();

    fixture = TestBed.createComponent(ConsumeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set selected offer', () => {
    component.setSelectedOffer(offerDetails);

    expect(component.selectedOffer).toEqual(offerDetails);
  });

  it('should set negotiated contract', () => {
    component.setNegotiatedContract(negotiatedContract);

    expect(component.negotiatedContract).toEqual(negotiatedContract);
  });

  it('should reset', () => {
    component.selectedOffer = offerDetails;
    component.negotiatedContract = negotiatedContract;

    component.resetSelection();

    expect(component.selectedOffer).toBeUndefined();
    expect(component.negotiatedContract).toBeUndefined();
  });
});
