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

import { AttributionDocumentComponent } from './attribution-document.component';
import {RouterTestingModule} from "@angular/router/testing";

describe('AttributionDocumentComponent', () => {
  let component: AttributionDocumentComponent;
  let fixture: ComponentFixture<AttributionDocumentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AttributionDocumentComponent],
      imports: [RouterTestingModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AttributionDocumentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
