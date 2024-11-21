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
  NavModule,
  ProgressModule,
  TableModule,
  TabsModule,
  TemplateIdDirective,
  TooltipModule,
  ModalModule
} from '@coreui/angular';
import {IconModule} from '@coreui/icons-angular';

import { NgxPrintModule } from 'ngx-print';

import {OfferRoutingModule} from './offer-routing.module';
import {ProvideComponent} from './provide/provide.component';
import {ConsumeComponent} from './consume/consume.component';
import {CommonViewsModule} from '../common-views/common-views.module';
import {AcceptComponent} from './accept/accept.component';
import {SelectComponent} from './select/select.component';
import {ContractsComponent} from "./contracts/contracts.component";
import { TransferComponent } from './transfer/transfer.component';
import {MatStepperModule} from "@angular/material/stepper";
import {MatButtonModule} from "@angular/material/button";
import { OfferPrintViewComponent } from './offer-print-view/offer-print-view.component';

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
    ModalModule,
    NgxPrintModule
  ],
  declarations: [ProvideComponent, ConsumeComponent, AcceptComponent, SelectComponent, ContractsComponent, TransferComponent, OfferPrintViewComponent],
  exports: [
    TransferComponent
  ]
})
export class OfferModule {
}
