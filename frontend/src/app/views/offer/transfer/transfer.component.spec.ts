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

import { TransferComponent } from './transfer.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {StatusMessageComponent} from "../../common-views/status-message/status-message.component";
import {Component, Input} from "@angular/core";
import {IContractDetailsTO, IOfferDetailsTO, IParticipantDetailsTO} from "../../../services/mgmt/api/backend";
import {
  ContractDetailsExportViewComponent
} from "../contract-details-export-view/contract-details-export-view.component";

describe('TransferComponent', () => {
  let component: TransferComponent;
  let fixture: ComponentFixture<TransferComponent>;

  @Component({
    selector: 'app-contract-details-export-view',
    template: ''
  })
  class MockExportView implements Partial<ContractDetailsExportViewComponent>{
    @Input() contractDetails?: IContractDetailsTO;
  }

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

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TransferComponent, MockStatusMessageComponent, MockExportView],
      imports: [HttpClientTestingModule]
    });
    fixture = TestBed.createComponent(TransferComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
