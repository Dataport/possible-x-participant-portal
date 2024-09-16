import { Component } from '@angular/core';
import { IOfferDetailsTO } from '../../../services/mgmt/api/backend';

@Component({
  selector: 'app-consume',
  templateUrl: './consume.component.html',
  styleUrl: './consume.component.scss'
})
export class ConsumeComponent {
  selectedOffer?: IOfferDetailsTO = undefined;

  setSelectedOffer(offer: IOfferDetailsTO | undefined): void {
    this.selectedOffer = offer;
  }
}
