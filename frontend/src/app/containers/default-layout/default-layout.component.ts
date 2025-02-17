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

import {Component, OnInit} from '@angular/core';
import {ApiService} from "../../services/mgmt/api/api.service";
import {AuthService} from "../../services/mgmt/auth/auth.service";

@Component({
  selector: 'app-default-layout',
  templateUrl: './default-layout.component.html',
  styleUrls: ['./default-layout.component.scss']
})
export class DefaultLayoutComponent implements OnInit {
  versionNumber: string = '';
  versionDate: string = '';

  constructor(private readonly apiService: ApiService, protected auth: AuthService) {
  }

  ngOnInit() {
    this.apiService.getVersion().then(response => {
      this.versionNumber = response.version;
      this.versionDate = response.date;
    }).catch(e => {
      console.log(e);
    });
  }
}
