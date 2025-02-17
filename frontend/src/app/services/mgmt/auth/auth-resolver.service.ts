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

import {ActivatedRouteSnapshot, ResolveFn, Router, RouterStateSnapshot} from '@angular/router';
import {ApiService} from "../api/api.service";
import {inject} from "@angular/core";
import {AuthService} from "./auth.service";

export const isAuthenticated: ResolveFn<any> =
  async (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> => {
    const router = inject(Router);
    const apiService = inject(ApiService);
    const authService = inject(AuthService);

    try {
      await apiService.getNameMapping();
      return true;
    } catch (e) {
      console.log(e);
      if (e.status === 401 || e.status === 403) {
        authService.logout();
        switch (e.status) {
          case 401:
            alert(`Invalid Credentials used. Please log in again.`);
            break;
          case 403:
            alert(`Unauthorized Credentials used. Please log in with the correct Role.`);
            break
        }
        router.navigate(["/login"]).then(() => {
          window.location.reload();
        });
      }
      return false;
    }
  }
