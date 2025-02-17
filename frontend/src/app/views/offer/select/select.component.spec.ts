/*
 *  Copyright 2024-2025 Dataport. All rights reserved. Developed as part of the POSSIBLE project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectComponent } from './select.component';
import { ApiService } from "../../../services/mgmt/api/api.service";
import {
  IOfferDetailsTO,
  IPxExtendedServiceOfferingCredentialSubject
} from "../../../services/mgmt/api/backend";
import { StatusMessageComponent } from "../../common-views/status-message/status-message.component";
import { Component, NO_ERRORS_SCHEMA } from "@angular/core";
import { first } from "rxjs";

describe('SelectOfferComponent', () => {
  let component: SelectComponent;
  let fixture: ComponentFixture<SelectComponent>;
  let apiService: jasmine.SpyObj<ApiService>;

  const offerDetails = {
    edcOfferId: 'dummy',
    catalogOffering: {
      id: "catalogOfferingId",
      "gx:providedBy": { id: "providedBy" },
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
      declarations: [SelectComponent, MockStatusMessageComponent],
      providers: [
        { provide: ApiService, useValue: apiServiceSpy }
      ],
      schemas: [NO_ERRORS_SCHEMA],
    })
    .compileComponents();

    fixture = TestBed.createComponent(SelectComponent);
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

    component.selectOffer("offerId");

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
