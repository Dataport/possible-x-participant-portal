import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';

import {
  AvatarModule,
  ButtonGroupModule,
  ButtonModule,
  CardModule,
  FormModule,
  GridModule,
  NavModule,
  ProgressModule,
  TableModule,
  TabsModule,
} from '@coreui/angular';
import {IconModule} from '@coreui/icons-angular';

import {LoginComponent} from './login.component';
import {RouterModule} from "@angular/router";
import {MaterialModule} from "../../sdwizard/material.module";

@NgModule({
  imports: [
    CardModule,
    NavModule,
    IconModule,
    TabsModule,
    CommonModule,
    GridModule,
    ProgressModule,
    ReactiveFormsModule,
    ButtonModule,
    FormModule,
    ButtonModule,
    ButtonGroupModule,
    AvatarModule,
    TableModule,
    RouterModule.forChild([
      { path: '', component: LoginComponent }
    ]),
    MaterialModule
  ],
  declarations: [LoginComponent],
})
export class LoginModule {
}
