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

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfferingWizardExtensionComponent } from './offering-wizard-extension.component';
import {ApiService} from "@services/api.service";
import {WizardAppModule} from "../../sdwizard/wizardapp.module";
import {WizardExtensionModule} from "../wizard-extension.module";
import {ICreateOfferResponseTO} from "../../services/mgmt/api/backend";

describe('OfferingWizardExtensionComponent', () => {
  let component: OfferingWizardExtensionComponent;
  let fixture: ComponentFixture<OfferingWizardExtensionComponent>;
  let apiService: jasmine.SpyObj<ApiService>;

  const offerCreationResponse = {
    edcResponseId: 'dummy',
    fhResponseId: 'dummy'
  } as ICreateOfferResponseTO;

  beforeEach(async () => {
    const apiServiceSpy = jasmine.createSpyObj('ApiService', ['createOffer']);

    await TestBed.configureTestingModule({
      declarations: [ OfferingWizardExtensionComponent ],
      providers: [
        {provide: ApiService, useValue: apiServiceSpy}
      ],
      imports: [
        WizardAppModule,
        WizardExtensionModule
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OfferingWizardExtensionComponent);
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

    component.selectedPolicy = 'Everything is allowed';

    component.prefillDone.subscribe((value) => {
      if (value) {
        component.createOffer().then(() => {
          expect(apiService.createOffer).toHaveBeenCalled();
        });
      }
    });

  });
});
