import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { ApiService } from '../../../services/mgmt/api/api.service';
import { environment } from '../../../../environments/environment';
import { HttpErrorResponse } from '@angular/common/http';
import { StatusMessageComponent } from '../../common-views/status-message/status-message.component';
import { IOfferDetailsTO } from '../../../services/mgmt/api/backend';

@Component({
  selector: 'app-accept-offer',
  templateUrl: './accept-offer.component.html'
})
export class AcceptOfferComponent {
  @Input() offer?: IOfferDetailsTO = undefined;
  @Output() dismiss: EventEmitter<any> = new EventEmitter();
  @ViewChild('acceptOfferStatusMessage') private acceptOfferStatusMessage!: StatusMessageComponent;

  constructor(private apiService: ApiService) {}

  protected async acceptContractOffer() {
    this.acceptOfferStatusMessage.showInfoMessage();
    console.log("'Accept Contract Offer' button pressed");

    this.apiService.acceptContractOffer({
      counterPartyAddress: environment.counter_party_address,
      offerId: this.offer == undefined ? "" : this.offer.offerId
    }).then(response => {
      console.log(response);
      this.acceptOfferStatusMessage.showSuccessMessage("Check console for details.", 20000);
    }).catch((e: HttpErrorResponse) => {
      this.acceptOfferStatusMessage.showErrorMessage(e.error.detail, 20000);
    });
  };

  cancel(): void {
    this.dismiss.emit();
  }
}
