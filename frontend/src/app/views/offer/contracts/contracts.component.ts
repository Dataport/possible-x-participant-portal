import {Component, OnInit, ViewChild} from '@angular/core';
import {IContractAgreementTO, IPolicy} from '../../../services/mgmt/api/backend';
import {HttpErrorResponse} from "@angular/common/http";
import {StatusMessageComponent} from "../../common-views/status-message/status-message.component";
import {ApiService} from '../../../services/mgmt/api/api.service';
import {MatSnackBar} from "@angular/material/snack-bar";
import {Sort} from "@angular/material/sort";

@Component({
  selector: 'app-contracts',
  templateUrl: './contracts.component.html',
  styleUrls: ['./contracts.component.scss']
})
export class ContractsComponent implements OnInit {
  @ViewChild("requestContractAgreementsStatusMessage") public requestContractAgreementsStatusMessage!: StatusMessageComponent;
  contractAgreements: IContractAgreementTO[] = [];
  sortedAgreements: IContractAgreementTO[] = [];
  expandedItemId: string | null = null;
  isTransferButtonDisabled = false;


  constructor(private apiService: ApiService, private popUpMessage: MatSnackBar) {
  }

  async getContractAgreements() {
    this.contractAgreements = await this.apiService.getContractAgreements();
    this.contractAgreements = this.contractAgreements.sort((a, b) => {
      return a.contractSigningDate > b.contractSigningDate ? -1 : 1;
    });
    this.sortedAgreements = this.contractAgreements.slice();
  }

  sortData(sort: Sort) {
    const data = this.contractAgreements.slice();
    if (!sort.active || sort.direction === '') {
      this.contractAgreements = data;
      return;
    }

    this.sortedAgreements = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'contractsigned':
          return this.compare(a.contractSigningDate, b.contractSigningDate, isAsc);
        case 'offer':
          return this.compare(a.assetDetails.name, b.assetDetails.name, isAsc);
        case 'provider':
          return this.compare(a.providerDetails.name, b.providerDetails.name, isAsc);
        case 'consumer':
          return this.compare(a.consumerDetails.name, b.consumerDetails.name, isAsc);
        case 'contractagreementid':
          return this.compare(a.id, b.id, isAsc);
        default:
          return 0;
      }
    });
  }

  compare(a: number | string | Date, b: number | string | Date, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

  ngOnInit(): void {
    this.handleGetContractAgreements();
  }

  toggleAccordion(itemId: string): void {
    this.expandedItemId = this.expandedItemId === itemId ? null : itemId;
  }

  async transferAgain(contractAgreement: IContractAgreementTO) {
    this.isTransferButtonDisabled = true;
    this.apiService.transferDataOfferAgain({
      contractAgreementId: contractAgreement.id,
      counterPartyAddress: null,
      edcOfferId: contractAgreement.assetId,
    }).then(response => {
      console.log(response.transferProcessState);
      this.popUpMessage.open("Data Transfer successful: " + response.transferProcessState, 'Close', {
        duration: undefined,
      });
      this.isTransferButtonDisabled = false;
    }).catch((e: HttpErrorResponse) => {
      console.log(e);
      this.popUpMessage.open("Data Transfer failed: " + (e.error.detail || e.error || e.message), 'Close', {
        duration: undefined,
      });
      this.isTransferButtonDisabled = false;
    });
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

  isDataOffering(item: IContractAgreementTO) {
    if (item.dataOffering === true) {
      return true;
    } else {
      return false;
    }
  }
}
