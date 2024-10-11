import { Component, ViewChild } from '@angular/core';
import { ApiService } from '../../../services/mgmt/api/api.service';
import { StatusMessageComponent } from '../../common-views/status-message/status-message.component';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-consume',
  templateUrl: './consume.component.html',
  styleUrl: './consume.component.scss'
})
export class ConsumeComponent {
  @ViewChild('acceptOfferStatusMessage') private acceptOfferStatusMessage!: StatusMessageComponent;

  constructor(private apiService: ApiService) {}

  protected async acceptContractOffer() {
    this.acceptOfferStatusMessage.showInfoMessage();
    console.log("'Accept Contract Offer' button pressed");

    this.apiService.acceptContractOffer().then(response => {
      console.log(response);
      this.acceptOfferStatusMessage.showSuccessMessage("Check console for details.", 20000);
    }).catch((e: HttpErrorResponse) => {
      this.acceptOfferStatusMessage.showErrorMessage(e.error.detail);
    });
  }
}
