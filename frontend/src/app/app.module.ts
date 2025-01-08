import { NgModule, APP_INITIALIZER } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { WizardExtensionModule } from './wizard-extension/wizard-extension.module';
import { WizardAppModule } from './sdwizard/wizardapp.module';
import { NameMappingService} from "./services/mgmt/name-mapping.service";

import {
  AvatarModule,
  BadgeModule,
  BreadcrumbModule,
  ButtonGroupModule,
  ButtonModule,
  CardModule,
  DropdownModule,
  FooterModule,
  FormModule,
  GridModule,
  HeaderModule,
  ListGroupModule,
  NavModule,
  NavbarModule,
  CollapseModule,
  ProgressModule,
  SharedModule,
  SidebarModule,
  TabsModule,
  UtilitiesModule,
} from '@coreui/angular';
import { DefaultLayoutComponent } from './containers/default-layout/default-layout.component';

export function initApp(nameMappingService: NameMappingService) {
  return () => nameMappingService.retrieveNameMapping();
}

@NgModule({
  declarations: [
    AppComponent,
    DefaultLayoutComponent
  ],
  imports: [
  BrowserModule,
  AppRoutingModule,
  AvatarModule,
  BadgeModule,
  BreadcrumbModule,
  ButtonGroupModule,
  ButtonModule,
  CardModule,
  DropdownModule,
  FooterModule,
  FormModule,
  GridModule,
  HeaderModule,
  ListGroupModule,
  NavModule,
  NavbarModule,
  CollapseModule,
  ProgressModule,
  SharedModule,
  SidebarModule,
  TabsModule,
  UtilitiesModule,
  BrowserAnimationsModule,
  WizardAppModule,
  WizardExtensionModule
  ],
  providers: [
    provideHttpClient(withInterceptorsFromDi()),
    NameMappingService,
    {
      provide: APP_INITIALIZER,
      useFactory: initApp,
      deps: [NameMappingService],
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
