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

import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {WizardExtensionModule} from '../../wizard-extension/wizard-extension.module';

import {
  AccordionButtonDirective,
  AccordionComponent,
  AccordionItemComponent,
  AvatarModule,
  BadgeComponent,
  ButtonGroupModule,
  ButtonModule,
  CardModule,
  FormModule,
  GridModule,
  ModalModule,
  NavModule,
  ProgressModule,
  TableModule,
  TabsModule,
  TemplateIdDirective,
  TooltipModule
} from '@coreui/angular';
import {IconModule} from '@coreui/icons-angular';

import {NgxPrintModule} from 'ngx-print';

import {OfferRoutingModule} from './offer-routing.module';
import {ProvideComponent} from './provide/provide.component';
import {ConsumeComponent} from './consume/consume.component';
import {CommonViewsModule} from '../common-views/common-views.module';
import {AcceptComponent} from './accept/accept.component';
import {SelectComponent} from './select/select.component';
import {ContractsComponent} from "./contracts/contracts.component";
import {TransferComponent} from './transfer/transfer.component';
import {MatStepperModule} from "@angular/material/stepper";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MaterialModule} from "../../sdwizard/material.module";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {OfferPrintViewComponent} from './offer-print-view/offer-print-view.component';
import {
  EnforcementPolicyAccordionViewComponent
} from './enforcement-policy-accordion-view/enforcement-policy-accordion-view.component';
import {MatSortModule} from "@angular/material/sort";
import {
  ContractDetailsExportViewComponent
} from './contract-details-export-view/contract-details-export-view.component';
import {OfferDetailsViewComponent} from './offer-details-view/offer-details-view.component';
import {
  ServiceOfferDetailsViewComponent
} from './offer-details-view/service-offer-details-view/service-offer-details-view.component';
import {
  DataOfferDetailsViewComponent
} from './offer-details-view/data-offer-details-view/data-offer-details-view.component';
import {
  EnforcementPolicyContentComponent
} from './enforcement-policy-accordion-view/enforcement-policy-content/enforcement-policy-content.component';
import {MatPaginatorModule} from "@angular/material/paginator";

@NgModule({
  imports: [
    CommonViewsModule,
    OfferRoutingModule,
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
    FormsModule,
    AccordionComponent,
    AccordionItemComponent,
    TemplateIdDirective,
    AccordionButtonDirective,
    BadgeComponent,
    WizardExtensionModule,
    TooltipModule,
    MatStepperModule,
    MatButtonModule,
    MatIconModule,
    MatCheckboxModule,
    MaterialModule,
    MatSnackBarModule,
    ModalModule,
    NgxPrintModule,
    MatSortModule,
    MatPaginatorModule
  ],
  declarations: [ProvideComponent, ConsumeComponent, AcceptComponent, SelectComponent, ContractsComponent, TransferComponent, OfferPrintViewComponent, EnforcementPolicyAccordionViewComponent, ContractDetailsExportViewComponent, OfferDetailsViewComponent, ServiceOfferDetailsViewComponent, DataOfferDetailsViewComponent, EnforcementPolicyContentComponent],
  exports: [
    TransferComponent
  ]
})
export class OfferModule {
}
