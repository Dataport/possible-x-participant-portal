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

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { authGuard } from './services/mgmt/auth/auth.guard';
import { DefaultLayoutComponent } from './containers';
import {isAuthenticated} from "./services/mgmt/auth/auth-resolver.service";

const routes: Routes = [
  {
    path: '',
    redirectTo: 'offer/provide',
    pathMatch: 'full'
  },
  {
    path: '',
    component: DefaultLayoutComponent,
    data: {
      title: 'Home'
    },
    children: [
      {
        path: 'offer',
        loadChildren: () =>
          import('./views/offer/offer.module').then((m) => m.OfferModule),
        canActivate: [authGuard],
        resolve: { isAuthenticated: isAuthenticated}
      },
      {
        path: 'login',
        loadChildren: () =>
          import('./views/login/login.module').then((m) => m.LoginModule)
      },
      {
        path: 'licenses',
        loadChildren: () =>
          import('./views/attribution-document/attribution-document.module').then((m) => m.AttributionDocumentModule)
      },
      ]
  },
  {
    path: '**',
    redirectTo: 'offer/provide'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes), RouterModule.forRoot(routes, { anchorScrolling: 'enabled'})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
