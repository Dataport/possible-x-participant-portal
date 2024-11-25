import {Component, OnInit, ViewChild} from '@angular/core';
import {IContractAgreementTO, IPolicy} from '../../../services/mgmt/api/backend';
import {HttpErrorResponse} from "@angular/common/http";
import {StatusMessageComponent} from "../../common-views/status-message/status-message.component";
import {ApiService} from '../../../services/mgmt/api/api.service';

@Component({
  selector: 'app-contracts',
  templateUrl: './contracts.component.html',
  styleUrls: ['./contracts.component.scss']
})
export class ContractsComponent implements OnInit {
  @ViewChild("requestContractAgreementsStatusMessage") public requestContractAgreementsStatusMessage!: StatusMessageComponent;
  contractAgreements: IContractAgreementTO[] = [];
  expandedItemId: string | null = null;
  isTransferButtonDisabled = false;

  constructor(private apiService: ApiService) {
  }

  async getContractAgreements() {
    this.contractAgreements = await this.apiService.getContractAgreements();
  }

  ngOnInit(): void {
    this.handleGetContractAgreements();
  }

  toggleAccordion(itemId: string): void {
    this.expandedItemId = this.expandedItemId === itemId ? null : itemId;
  }

  transferAgain(contractAgreement: IContractAgreementTO) {
    this.isTransferButtonDisabled = true;
    this.apiService.transferDataOfferAgain({
      contractAgreementId: contractAgreement.id,
      counterPartyAddress: null,
      edcOfferId: contractAgreement.assetId,
    }).then(response => {
      console.log(response);
      //this.dataTransferStatusMessage.showSuccessMessage("Data Transfer successful: " + response.transferProcessState);
    }).catch((e: HttpErrorResponse) => {
      console.log(e);
      //this.dataTransferStatusMessage.showErrorMessage(e.error.detail || e.error || e.message);
    });
    this.isTransferButtonDisabled = false;
  }

  getPolicyAsString(policy: IPolicy): string {
    return JSON.stringify(policy, null, 2);
  }

  private handleGetContractAgreements() {
    this.getContractAgreements().catch((e: HttpErrorResponse) => {
      this.requestContractAgreementsStatusMessage.showErrorMessage(e.error.detail);
    }).catch(_ => {
      this.requestContractAgreementsStatusMessage.showErrorMessage("Unknown error occurred");
    });
  }
}
