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

import { OfferPrintViewComponent } from './offer-print-view.component';
import {ModalModule} from "@coreui/angular";
import {provideAnimations} from "@angular/platform-browser/animations";

describe('OfferPrintViewComponent', () => {
  let component: OfferPrintViewComponent;
  let fixture: ComponentFixture<OfferPrintViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OfferPrintViewComponent],
      imports: [ModalModule],
      providers: [provideAnimations()]
    });
    fixture = TestBed.createComponent(OfferPrintViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
