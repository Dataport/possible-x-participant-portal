import {Component} from '@angular/core';
import {IAcceptOfferResponseTO, IOfferDetailsTO} from '../../../services/mgmt/api/backend';

@Component({
  selector: 'app-consume',
  templateUrl: './consume.component.html',
  styleUrls: ['./consume.component.scss']
})
export class ConsumeComponent {
  selectedOffer?: IOfferDetailsTO = undefined;
  negotiatedContract?: IAcceptOfferResponseTO = undefined;

  setSelectedOffer(offer: IOfferDetailsTO): void {
    this.selectedOffer = offer;
  }

  setNegotiatedContract(contract: IAcceptOfferResponseTO): void {
    this.negotiatedContract = contract;
  }

  resetSelection() {
    this.selectedOffer = undefined;
    this.negotiatedContract = undefined;
  }
}
