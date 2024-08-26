import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule , FormsModule } from '@angular/forms';

import {
  AccordionButtonDirective,
  AccordionComponent, AccordionItemComponent,
  AvatarModule,
  ButtonGroupModule,
  ButtonModule,
  CardModule,
  FormModule,
  GridModule,
  NavModule,
  ProgressModule,
  TableModule,
  TabsModule, TemplateIdDirective,
} from '@coreui/angular';
import { IconModule } from '@coreui/icons-angular';

import { OfferRoutingModule } from './offer-routing.module';
import { ProvideComponent } from './provide/provide.component';
import { ConsumeComponent } from './consume/consume.component';
import { CommonViewsModule } from '../common-views/common-views.module';
import { AcceptOfferComponent } from './accept/accept-offer.component';

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
  ],
  declarations: [ProvideComponent, ConsumeComponent, AcceptOfferComponent],
})
export class OfferModule {}
