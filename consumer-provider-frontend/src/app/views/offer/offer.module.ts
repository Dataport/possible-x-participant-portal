import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule , FormsModule } from '@angular/forms';

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
import { IconModule } from '@coreui/icons-angular';

import { OfferRoutingModule } from './offer-routing.module';
import { ProvideComponent } from './provide/provide.component';
import { ConsumeComponent } from './consume/consume.component';
import { CommonViewsModule } from '../common-views/common-views.module';

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
  ],
  declarations: [ProvideComponent, ConsumeComponent],
})
export class OfferModule {}
