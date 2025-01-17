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
  PaginationModule,
  PaginationComponent,
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
