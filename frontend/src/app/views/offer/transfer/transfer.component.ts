import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges, ViewChild} from '@angular/core';
import {IAcceptOfferResponseTO, IOfferDetailsTO} from "../../../services/mgmt/api/backend";
import {StatusMessageComponent} from "../../common-views/status-message/status-message.component";
import {ApiService} from "../../../services/mgmt/api/api.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-transfer',
  templateUrl: './transfer.component.html',
  styleUrls: ['./transfer.component.scss']
})
export class TransferComponent implements OnChanges {
  @Input() contract?: IAcceptOfferResponseTO = undefined;
  @Input() offer?: IOfferDetailsTO = undefined;
  @Output() dismiss: EventEmitter<any> = new EventEmitter();
  @ViewChild('dataTransferStatusMessage') private dataTransferStatusMessage!: StatusMessageComponent;
  dismissButtonLabel: string;

  constructor(private apiService: ApiService) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    const contractChanges = changes['contract'].currentValue;
    this.dismissButtonLabel = contractChanges.dataOffering ? "Cancel" : "Close";
  }

  async transfer() {
    this.dataTransferStatusMessage.showInfoMessage();
    console.log("'Transfer Data Resource' button pressed");

    this.apiService.transferDataOffer({
      contractAgreementId: this.contract.contractAgreementId,
      counterPartyAddress: this.offer.catalogOffering["px:providerUrl"],
      edcOfferId: this.offer.edcOfferId,
    }).then(response => {
      console.log(response);
      this.dataTransferStatusMessage.showSuccessMessage("Data Transfer successful: " + response.transferProcessState);
    }).catch((e: HttpErrorResponse) => {
      this.dataTransferStatusMessage.showErrorMessage(e.error.detail || e.error || e.message);
    });
  }

  cancel(): void {
    this.dismiss.emit();
  }
}
