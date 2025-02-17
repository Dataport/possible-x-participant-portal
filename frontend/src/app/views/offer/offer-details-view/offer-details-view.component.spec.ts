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
import { NameMappingService } from "../../../services/mgmt/name-mapping.service";
import { OfferDetailsViewComponent } from './offer-details-view.component';
import { ServiceOfferDetailsViewComponent } from "./service-offer-details-view/service-offer-details-view.component";
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('OfferDetailsViewComponent', () => {
  let component: OfferDetailsViewComponent;
  let fixture: ComponentFixture<OfferDetailsViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [OfferDetailsViewComponent, ServiceOfferDetailsViewComponent],
      providers: [NameMappingService]
    });
    fixture = TestBed.createComponent(OfferDetailsViewComponent);
    component = fixture.componentInstance;
    component.catalogOffering = {
      "gx:providedBy": {id: "participantId"},
      "gx:dataProtectionRegime": [],
      "gx:aggregationOf": [
        {
          "gx:copyrightOwnedBy": ["participantId"],
          "gx:producedBy": {id: "participantId"},
          "gx:containsPII": true,
          "gx:legitimateInterest": {"gx:dataProtectionContact": "contact",
            "gx:legalBasis": "legalBasis",}

        } as any
      ]
    } as any;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
