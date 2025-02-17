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

const TOKEN_NAME: string = 'authToken';

@Injectable({
  providedIn: 'root'
})

export class AuthService {

  private isAuthenticated = !!sessionStorage.getItem(TOKEN_NAME);

  login(username: string, password: string): void {
    const authToken = btoa(username + ':' + password);
    sessionStorage.setItem(TOKEN_NAME, authToken);
    this.isAuthenticated = true;
  }

  logout(): void {
    sessionStorage.removeItem(TOKEN_NAME);
    this.isAuthenticated = false;
  }

  isLoggedIn(): boolean {
    return this.isAuthenticated;
  }

  getToken(): string | null {
    return sessionStorage.getItem(TOKEN_NAME);
  }
}
