import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ProvideComponent } from './provide/provide.component'
import { ConsumeComponent } from './consume/consume.component'
import {ContractsComponent} from "./contracts/contracts.component";

const routes: Routes = [
    {
        path: '',
        data: {
            title: 'Offer',
        },
        children: [
            {
                path: '',
                pathMatch: 'full',
                redirectTo: 'provide',
            },
            {
                path: 'provide',
                component: ProvideComponent,
                data: {
                    title: 'Provide Offer',
                },
            },
            {
                path: 'consume',
                component: ConsumeComponent,
                data: {
                    title: 'Consume Offer',
                },
            },
            {
                path: 'contracts',
                component: ContractsComponent,
                data: {
                    title: 'Contracts',
                },
            },
        ],
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class OfferRoutingModule {
}
