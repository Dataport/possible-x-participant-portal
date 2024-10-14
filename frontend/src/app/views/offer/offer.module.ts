import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule , FormsModule } from '@angular/forms';
import { WizardExtensionModule } from '../../wizard-extension/wizard-extension.module';

import {
    AccordionButtonDirective,
    AccordionComponent, AccordionItemComponent,
    AvatarModule, BadgeComponent,
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
import { SelectOfferComponent } from './select/select-offer.component';
import {ContractsComponent} from "./contracts/contracts.component";

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
        WizardExtensionModule
    ],
  declarations: [ProvideComponent, ConsumeComponent, AcceptOfferComponent, SelectOfferComponent, ContractsComponent],
})
export class OfferModule {}
