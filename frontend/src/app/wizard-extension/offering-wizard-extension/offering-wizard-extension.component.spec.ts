/*
 *  Copyright 2024 Dataport. All rights reserved. Developed as part of the MERLOT project.
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

import {ComponentFixture, TestBed} from '@angular/core/testing';

import {OfferingWizardExtensionComponent} from './offering-wizard-extension.component';
import {ApiService} from "../../services/mgmt/api/api.service";
import {ICreateOfferResponseTO} from "../../services/mgmt/api/backend";
import {provideAnimations} from "@angular/platform-browser/animations";
import {Component, Input} from "@angular/core";
import {BaseWizardExtensionComponent} from "../base-wizard-extension/base-wizard-extension.component";
import {MatStepperModule} from "@angular/material/stepper";
import {AccordionModule} from "@coreui/angular";
import {StatusMessageComponent} from "../../views/common-views/status-message/status-message.component";
import {FormsModule} from "@angular/forms";
import { ServiceOfferingPolicyHintsComponent } from './service-offering-policy-hints/service-offering-policy-hints.component';
import { DataResourcePolicyHintsComponent } from './data-resource-policy-hints/data-resource-policy-hints.component';
import { PossibleXEnforcedPolicyHintsComponent } from './possible-x-enforced-policy-hints/possible-x-enforced-policy-hints.component';
import {NameMappingService} from "../../services/mgmt/name-mapping.service";

@Component({
  selector: 'app-status-message',
  template: ''
})
class MockStatusMessageComponent implements Partial<StatusMessageComponent>{
  @Input() successMessage: string;
  @Input() errorMessage: string;
  @Input() infoMessage: string;
  hideAllMessages() {}
  showInfoMessage() {}
  showSuccessMessage(msg: string) {}
  showErrorMessage(msg: string) {}
}

@Component({
  selector: 'app-base-wizard-extension',
  template: ''
})
class MockWizardExtension implements Partial<BaseWizardExtensionComponent>{
  generateJsonCs() { return { id: 'id' };}
  prefillFields(selfDescriptionFields: any, disabledFields: string[]) {}
  isWizardFormInvalid() { return false; }
  ngOnDestroy() {}
}

describe('OfferingWizardExtensionComponent', () => {
  let component: OfferingWizardExtensionComponent;
  let fixture: ComponentFixture<OfferingWizardExtensionComponent>;
  let apiService: jasmine.SpyObj<ApiService>;
  let nameMappingService: jasmine.SpyObj<NameMappingService>;

  const offerCreationResponse = {
    edcResponseId: 'dummy',
    fhResponseId: 'dummy'
  } as ICreateOfferResponseTO;

  beforeEach(async () => {
    const apiServiceSpy = jasmine.createSpyObj('ApiService',
      ['createServiceOffering', 'getGxServiceOfferingShape', 'getGxDataResourceShape', 'createDataOffering', 'getPrefillFields']);
    const nameMappingServiceSpy = jasmine.createSpyObj('NameMappingService', ['getNameById', 'getNameMapping']);

    // Mock return values for nameMappingServiceSpy methods
    nameMappingServiceSpy.getNameById.and.returnValue('Test Name');
    nameMappingServiceSpy.getNameMapping.and.returnValue({ '123': 'Test Name' });

    await TestBed.configureTestingModule({
      declarations: [OfferingWizardExtensionComponent, MockWizardExtension, MockStatusMessageComponent, ServiceOfferingPolicyHintsComponent, DataResourcePolicyHintsComponent, PossibleXEnforcedPolicyHintsComponent],
      providers: [
        { provide: ApiService, useValue: apiServiceSpy },
        { provide: NameMappingService, useValue: nameMappingServiceSpy },
        provideAnimations()
      ],
      imports: [AccordionModule, MatStepperModule, FormsModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(OfferingWizardExtensionComponent);
    component = fixture.componentInstance;
    apiService = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    nameMappingService = TestBed.inject(NameMappingService) as jasmine.SpyObj<NameMappingService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call createDataOffering on apiService when createOffering is called', async () => {
    const mockResponse = Promise.resolve(offerCreationResponse);
    apiService.createDataOffering.and.returnValue(mockResponse);

    component.selectedFileName = 'dummy';
    component.offerType = "data";

    component.createOffer().then(() => {
      expect(apiService.createDataOffering).toHaveBeenCalled();
    });

  });

  it('should call createServiceOffering on apiService when createOffering is called', async () => {
    const mockResponse = Promise.resolve(offerCreationResponse);
    apiService.createServiceOffering.and.returnValue(mockResponse);

    component.offerType = "service";

    component.createOffer().then(() => {
      expect(apiService.createServiceOffering).toHaveBeenCalled();
    });

  });

  it('should call getGxServiceOfferingShape on apiService when retrieveAndAdaptServiceOfferingShape is called', async () => {
    const mockResponse = Promise.resolve("dummy");
    apiService.getGxServiceOfferingShape.and.returnValue(mockResponse);

    component.retrieveAndAdaptServiceOfferingShape().then(() => {
      expect(apiService.getGxServiceOfferingShape).toHaveBeenCalled();
    });

  });

  it('should call getGxDataResourceShape on apiService when retrieveAndAdaptDataResourceShape is called', async () => {
    const mockResponse = Promise.resolve("dummy");
    apiService.getGxDataResourceShape.and.returnValue(mockResponse);

    component.offerType = "service";

    component.retrieveAndAdaptDataResourceShape().then(() => {
      expect(apiService.getGxDataResourceShape).toHaveBeenCalled();
    });

  });

  it('should call getPrefillFields on apiService when retrieveAndSetPrefillFields is called', async () => {
    const mockResponse = Promise.resolve({participantId: "dummy", dataProductPrefillFields: { serviceOfferingName: "dummy", serviceOfferingDescription: "dummy" }});
    apiService.getPrefillFields.and.returnValue(mockResponse);


    component.retrieveAndSetPrefillFields().then(() => {
      expect(apiService.getPrefillFields).toHaveBeenCalled();
    });
  });

  it('should return name and ID string', () => {
    const id = '123';
    const name = 'Test Name';
    nameMappingService.getNameById.and.returnValue(name);

    const result = component.getNameIdStringById(id);
    expect(result).toBe(`${name} (${id})`);
  });
});
