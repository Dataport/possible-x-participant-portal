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
import { NavbarModule, GridModule, CollapseModule, NavModule, FooterModule } from '@coreui/angular';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { DefaultLayoutComponent } from './default-layout.component';
import { of } from 'rxjs';
import {ApiService} from "../../services/mgmt/api/api.service";
import {IVersionTO} from "../../services/mgmt/api/backend";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('DefaultLayoutComponent', () => {
  let component: DefaultLayoutComponent;
  let fixture: ComponentFixture<DefaultLayoutComponent>;
  let mockVersion: IVersionTO = {version: 'hash234', date: '2021-01-01'};

  beforeEach(async () => {
    const apiServiceSpy = jasmine.createSpyObj('ApiService', ['getVersion']);
    apiServiceSpy.getVersion.and.returnValue(Promise.resolve(mockVersion));

    await TestBed.configureTestingModule({
      declarations: [DefaultLayoutComponent],
      imports: [ NavbarModule, GridModule, CollapseModule, NoopAnimationsModule, NavModule, RouterModule, FooterModule,
        HttpClientTestingModule ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({}), // Mock route parameters
            snapshot: { paramMap: { get: () => null } } // Mock snapshot
          }
        },
        {provide: ApiService, useValue: apiServiceSpy}
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DefaultLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set version from apiService', async () => {
    expect(component.versionDate).toEqual(mockVersion.date);
    expect(component.versionNumber).toEqual(mockVersion.version);
  });
});
