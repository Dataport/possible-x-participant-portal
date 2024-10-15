import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges, ViewChild} from '@angular/core';
import {ApiService} from '../../../services/mgmt/api/api.service';
import {HttpErrorResponse} from '@angular/common/http';
import {StatusMessageComponent} from '../../common-views/status-message/status-message.component';
import {IOfferDetailsTO} from '../../../services/mgmt/api/backend';

@Component({
  selector: 'app-accept-offer',
  templateUrl: './accept-offer.component.html',
  styleUrls: ['./accept-offer.component.scss']
})
export class AcceptOfferComponent implements OnChanges {
  @Input() offer?: IOfferDetailsTO = undefined;
  @Output() dismiss: EventEmitter<any> = new EventEmitter();
  buttonLabel: string;
  @ViewChild('acceptOfferStatusMessage') private acceptOfferStatusMessage!: StatusMessageComponent;

  constructor(private apiService: ApiService) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    const offerChanges = changes['offer'].currentValue;
    this.buttonLabel = offerChanges.dataOffering ? "Accept and transfer" : "Accept";
  }

  async acceptContractOffer() {
    this.acceptOfferStatusMessage.showInfoMessage();
    console.log("'Accept Contract Offer' button pressed");

    this.apiService.acceptContractOffer({
      counterPartyAddress: this.offer == undefined ? "" : this.offer.catalogOffering["px:providerUrl"],
      edcOfferId: this.offer == undefined ? "" : this.offer.edcOfferId,
      dataOffering: this.offer == undefined ? false : this.offer.dataOffering
    }).then(response => {
      console.log(response);
      this.acceptOfferStatusMessage.showSuccessMessage("Contract Agreement ID: " + response.contractAgreementId);
    }).catch((e: HttpErrorResponse) => {
      this.acceptOfferStatusMessage.showErrorMessage(e.error.detail || e.error || e.message);
    });
  };

  cancel(): void {
    this.dismiss.emit();
  }
}
