/*
 *  Copyright 2024 Dataport. All rights reserved. Developed as part of the MERLOT project.
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

import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {WizardAppModule} from '../sdwizard/wizardapp.module';
import {CommonViewsModule} from '../views/common-views/common-views.module';
import {
  AccordionButtonDirective,
  AccordionComponent,
  AccordionItemComponent,
  ButtonGroupModule,
  ButtonModule,
  FormTextDirective,
  GridModule,
  ModalModule,
  TemplateIdDirective
} from '@coreui/angular';
import {FormsModule} from '@angular/forms';
import {FlexLayoutModule} from '@angular/flex-layout';
import {OfferingWizardExtensionComponent} from './offering-wizard-extension/offering-wizard-extension.component';
import {BaseWizardExtensionComponent} from './base-wizard-extension/base-wizard-extension.component';
import {MaterialModule} from '../sdwizard/material.module'
import {IconModule} from '@coreui/icons-angular';
import {MatStepperModule} from "@angular/material/stepper";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import { DataResourcePolicyHintsComponent } from './offering-wizard-extension/data-resource-policy-hints/data-resource-policy-hints.component';
import { ServiceOfferingPolicyHintsComponent } from './offering-wizard-extension/service-offering-policy-hints/service-offering-policy-hints.component';
import { PossibleXEnforcedPolicyHintsComponent } from './offering-wizard-extension/possible-x-enforced-policy-hints/possible-x-enforced-policy-hints.component';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatNativeDateModule} from '@angular/material/core';
import {
  NgxMatDatetimePickerModule,
  NgxMatDateFormats,
  NGX_MAT_DATE_FORMATS,
} from '@angular-material-components/datetime-picker';
import { NgxMatMomentModule, NGX_MAT_MOMENT_DATE_ADAPTER_OPTIONS } from '@angular-material-components/moment-adapter';
import {MAT_MOMENT_DATE_ADAPTER_OPTIONS} from "@angular/material-moment-adapter";

export const MOMENT_DATETIME_WITH_SECONDS_FORMAT = 'DD/MM/YYYY, HH:mm:ss';

const CUSTOM_MOMENT_FORMATS: NgxMatDateFormats = {
  parse: {
    dateInput: MOMENT_DATETIME_WITH_SECONDS_FORMAT,
  },
  display: {
    dateInput: MOMENT_DATETIME_WITH_SECONDS_FORMAT,
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};

@NgModule({
  declarations: [BaseWizardExtensionComponent, OfferingWizardExtensionComponent, DataResourcePolicyHintsComponent, ServiceOfferingPolicyHintsComponent, PossibleXEnforcedPolicyHintsComponent],
  exports: [
    BaseWizardExtensionComponent, OfferingWizardExtensionComponent
  ],
  imports: [
    CommonModule,
    WizardAppModule,
    CommonViewsModule,
    FlexLayoutModule,
    ButtonModule,
    ButtonGroupModule,
    FormsModule,
    GridModule,
    ModalModule,
    MaterialModule,
    IconModule,
    FormTextDirective,
    MatStepperModule,
    MatButtonModule,
    MatIconModule,
    AccordionComponent,
    AccordionItemComponent,
    TemplateIdDirective,
    AccordionButtonDirective,
    MatFormFieldModule, MatInputModule, MatDatepickerModule, MatNativeDateModule,
    NgxMatDatetimePickerModule,
    NgxMatMomentModule
  ],
  providers: [
    { provide: MAT_MOMENT_DATE_ADAPTER_OPTIONS, useValue: { useUtc: true } },
    { provide: NGX_MAT_DATE_FORMATS, useValue: CUSTOM_MOMENT_FORMATS },
    { provide: NGX_MAT_MOMENT_DATE_ADAPTER_OPTIONS, useValue: { useUtc: true } },
  ],
})
export class WizardExtensionModule {
}
