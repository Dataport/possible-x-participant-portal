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
        path: 'attribution',
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
