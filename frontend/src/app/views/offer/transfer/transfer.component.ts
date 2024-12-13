import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  TemplateRef,
  ViewChild,
  ViewContainerRef
} from '@angular/core';
import {
  IAcceptOfferResponseTO, IContractDetailsTO,
  IOfferDetailsTO
} from "../../../services/mgmt/api/backend";
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
  @Input() retrievedContractDetails?: IContractDetailsTO = undefined;
  @Output() dismiss: EventEmitter<any> = new EventEmitter();
  @ViewChild('dataTransferStatusMessage') dataTransferStatusMessage!: StatusMessageComponent;
  dismissButtonLabel: string;
  isTransferButtonDisabled = false;

  @ViewChild('viewContainerRef', { read: ViewContainerRef, static: true }) viewContainerRef: ViewContainerRef;
  @ViewChild('contractDetails', { read: TemplateRef, static: true }) contractDetails: TemplateRef<any>;

  constructor(private apiService: ApiService) {
  }

  ngOnChanges(): void {
    if(this.contract) {
      this.viewContainerRef.clear();
      this.dismissButtonLabel = this.contract.dataOffering ? "Cancel" : "Close";
      this.viewContainerRef.createEmbeddedView(this.contractDetails);
    } else {
      this.viewContainerRef.clear();
    }
    this.isTransferButtonDisabled = false;
  }

  async transfer() {
    this.dataTransferStatusMessage.showInfoMessage();
    console.log("'Transfer Data Resource' button pressed");
    this.isTransferButtonDisabled = true;
    this.apiService.transferDataOffer({
      contractAgreementId: this.contract.contractAgreementId,
      counterPartyAddress: this.offer.catalogOffering["px:providerUrl"],
      edcOfferId: this.offer.edcOfferId,
    }).then(response => {
      console.log(response);
      this.dataTransferStatusMessage.showSuccessMessage("Data Transfer successful: " + response.transferProcessState);
    }).catch((e: HttpErrorResponse) => {
      this.dataTransferStatusMessage.showErrorMessage(e.error.detail || e.error || e.message);
      this.isTransferButtonDisabled = false;
    });
  }

  cancel(): void {
    this.dismiss.emit();
  }
}
