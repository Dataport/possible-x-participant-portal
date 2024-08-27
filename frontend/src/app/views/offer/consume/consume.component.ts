import { Component, ViewChild } from '@angular/core';
import { ApiService } from '../../../services/mgmt/api/api.service';
import { StatusMessageComponent } from '../../common-views/status-message/status-message.component';
import { HttpErrorResponse } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { IOfferDetailsTO } from '../../../services/mgmt/api/backend';

@Component({
  selector: 'app-consume',
  templateUrl: './consume.component.html',
  styleUrl: './consume.component.scss'
})
export class ConsumeComponent {
  @ViewChild('queryCatalogStatusMessage') private queryCatalogStatusMessage!: StatusMessageComponent;
  selectedOffer?: IOfferDetailsTO = undefined;

  constructor(private apiService: ApiService) {}

  async selectOffer() {
    this.queryCatalogStatusMessage.showInfoMessage();
    this.apiService.selectContractOffer({
      counterPartyAddress: environment.counter_party_address,
      offerId: 'dummy' // TODO pass actual data
    }).then(response => {
      console.log(response);
      this.queryCatalogStatusMessage.showSuccessMessage("Check console for details.", 20000);
      this.selectedOffer = response;
    }).catch((e: HttpErrorResponse) => {
      this.queryCatalogStatusMessage.showErrorMessage(e.error.detail, 20000);
    });
  }

  deselectOffer(): void {
    this.selectedOffer = undefined;
  }
}
