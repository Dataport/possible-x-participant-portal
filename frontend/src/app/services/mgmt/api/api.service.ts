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

import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../../environments/environment';
import {RestApplicationClient} from './backend';
import {AngularHttpClientImpl} from './angular-http-client';

@Injectable({
  providedIn: 'root'
})
export class ApiService extends RestApplicationClient {

  private readonly baseUrl: string = environment.api_url;

  constructor(private readonly http: HttpClient) {
    super(new AngularHttpClientImpl(http, environment.api_url));
  }

}
