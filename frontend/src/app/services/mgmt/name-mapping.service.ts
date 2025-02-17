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
import {ApiService} from "./api/api.service";

@Injectable({
  providedIn: 'root'
})
export class NameMappingService {
  private idNameMap: { [key: string]: string } = {};
  static readonly UNKNOWN = 'Unknown';
  static readonly EMPTY = '-';

  constructor(private readonly apiService: ApiService) {
  }

  retrieveNameMapping(): Promise<void> {
    console.log("Retrieving name mapping");
    return this.apiService.getNameMapping().then(response => {
      console.log(response);
      this.idNameMap = response;
    }).catch(e => {
      console.log(e);
    });
  }

  getNameById(id: string): string {
    if (!id) {
      return NameMappingService.EMPTY;
    }
    return this.idNameMap[id] || NameMappingService.UNKNOWN;
  }

  getNameMapping(): { [key: string]: string } {
    return this.idNameMap;
  }
}
