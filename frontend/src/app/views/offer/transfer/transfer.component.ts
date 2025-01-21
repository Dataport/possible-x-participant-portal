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
import {
  ContractDetailsExportViewComponent
} from "../contract-details-export-view/contract-details-export-view.component";

@Component({
  selector: 'app-transfer',
  templateUrl: './transfer.component.html',
  styleUrls: ['./transfer.component.scss']
})
export class TransferComponent implements OnChanges {
  @Input() contract?: IAcceptOfferResponseTO = undefined;
  @Input() offer?: IOfferDetailsTO = undefined;
  @Output() dismiss: EventEmitter<any> = new EventEmitter();
  @ViewChild('dataTransferStatusMessage') dataTransferStatusMessage!: StatusMessageComponent;
  @ViewChild(('contractDetailsExportView')) public contractDetailsExportView!: ContractDetailsExportViewComponent;
  dismissButtonLabel: string;
  isTransferButtonDisabled = false;
  contractDetailsToExport?: IContractDetailsTO = undefined;

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

  async retrieveAndSetContractDetails() {
    this.contractDetailsToExport = undefined;
    this.contractDetailsExportView.informationRetrievalStatusMessage.showInfoMessage()
    this.apiService.getContractDetailsByContractAgreementId(this.contract.contractAgreementId).then(contractDetails => {
      console.log(contractDetails);
      this.contractDetailsExportView.informationRetrievalStatusMessage.hideAllMessages();
      this.contractDetailsToExport = contractDetails;
    }).catch((e: HttpErrorResponse) => {
      console.log(e?.error?.detail || e?.error || e?.message);
      this.contractDetailsExportView.informationRetrievalStatusMessage.showErrorMessage(e?.error?.detail || e?.error || e?.message);
    });
  }
}
