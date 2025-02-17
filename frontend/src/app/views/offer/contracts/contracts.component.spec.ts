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


import {ComponentFixture, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {ContractsComponent} from './contracts.component';
import {ApiService} from '../../../services/mgmt/api/api.service';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {StatusMessageComponent} from '../../common-views/status-message/status-message.component';
import {ContractDetailsExportViewComponent} from '../contract-details-export-view/contract-details-export-view.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {
  IEndDatePolicy,
  IEnforcementPolicyUnion,
  IParticipantRestrictionPolicy
} from "../../../services/mgmt/api/backend";
import {ModalModule} from "@coreui/angular";
import {MatPaginatorModule} from "@angular/material/paginator";

describe('ContractsComponent', () => {
  let component: ContractsComponent;
  let fixture: ComponentFixture<ContractsComponent>;
  let apiService: jasmine.SpyObj<ApiService>;

  beforeEach(async () => {
    const apiServiceSpy = jasmine.createSpyObj('ApiService', ['getContractAgreements', 'transferDataOfferAgain', 'getOfferWithTimestampByContractAgreementId']);

    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MatSnackBarModule, ModalModule, BrowserAnimationsModule, MatPaginatorModule],
      declarations: [ContractsComponent, StatusMessageComponent, ContractDetailsExportViewComponent],
      providers: [{ provide: ApiService, useValue: apiServiceSpy }]
    }).compileComponents();

    fixture = TestBed.createComponent(ContractsComponent);
    component = fixture.componentInstance;
    apiService = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get and sort contract agreements', async () => {
    const mockAgreements = [
      { id: '1', contractSigningDate: new Date() },
      { id: '2', contractSigningDate: new Date() }
    ] as any;

    apiService.getContractAgreements.and.returnValue(Promise.resolve(mockAgreements));

    await component.getContractAgreements();

    expect(component.contractAgreements).toEqual(mockAgreements.reverse());
    expect(component.sortedAgreements).toEqual(mockAgreements.reverse());
  });

  it('should detect invalid policies', () => {
    const policies: IEnforcementPolicyUnion[] = [
      {"@type": "EndDatePolicy", "valid": false, "date": new Date()} as IEndDatePolicy,
      {
        "@type": "ParticipantRestrictionPolicy",
        "valid": true,
        "allowedParticipants": ["org1", "org2"]
      } as IParticipantRestrictionPolicy
    ];

    expect(component.isAnyPolicyInvalid(policies)).toBeTrue();
  });

  it('should detect no invalid policies', () => {
    const policies: IEnforcementPolicyUnion[] = [
      {"@type": "EndDatePolicy", "valid": true, "date": new Date()} as IEndDatePolicy,
      {
        "@type": "ParticipantRestrictionPolicy",
        "valid": true,
        "allowedParticipants": ["org1", "org2"]
      } as IParticipantRestrictionPolicy
    ];

    expect(component.isAnyPolicyInvalid(policies)).toBeFalse();
  });

  it('should disable transfer button if any policy is invalid', () => {
    const contractAgreement = {
      enforcementPolicies: [
        {"@type": "EndDatePolicy", "valid": false, "date": new Date()} as IEndDatePolicy,
        {
          "@type": "ParticipantRestrictionPolicy",
          "valid": true,
          "allowedParticipants": ["org1", "org2"]
        } as IParticipantRestrictionPolicy
      ]
    } as any;

    component.isTransferButtonDisabled = false;

    expect(component.shouldTransferButtonBeDisabled(contractAgreement)).toBeTrue();
  });

  it('should not disable transfer button if all policies are valid', () => {
    const contractAgreement = {
      enforcementPolicies: [
        {"@type": "EndDatePolicy", "valid": true, "date": new Date()} as IEndDatePolicy,
        {
          "@type": "ParticipantRestrictionPolicy",
          "valid": true,
          "allowedParticipants": ["org1", "org2"]
        } as IParticipantRestrictionPolicy
      ]
    } as any;

    component.isTransferButtonDisabled = false;

    expect(component.shouldTransferButtonBeDisabled(contractAgreement)).toBeFalse();
  });
});
