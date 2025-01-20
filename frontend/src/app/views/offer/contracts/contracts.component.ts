import {Component, OnInit, ViewChild} from '@angular/core';
import {
  IContractAgreementsResponseTO,
  IContractAgreementTO,
  IContractDetailsTO,
  IEnforcementPolicyUnion,
  IPolicy,
} from '../../../services/mgmt/api/backend';
import {HttpErrorResponse} from "@angular/common/http";
import {StatusMessageComponent} from "../../common-views/status-message/status-message.component";
import {ApiService} from '../../../services/mgmt/api/api.service';
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatSort, Sort} from "@angular/material/sort";
import {
  ContractDetailsExportViewComponent
} from "../contract-details-export-view/contract-details-export-view.component";

interface IndexedContractAgreement extends IContractAgreementTO {
  initialIndex: number;
}

@Component({
  selector: 'app-contracts',
  templateUrl: './contracts.component.html',
  styleUrls: ['./contracts.component.scss']
})
export class ContractsComponent implements OnInit {
  @ViewChild("requestContractAgreementsStatusMessage") public requestContractAgreementsStatusMessage!: StatusMessageComponent;
  @ViewChild(("contractDetailsExportView")) public contractDetailsExportView!: ContractDetailsExportViewComponent;
  @ViewChild(MatSort) sortDirective: MatSort;
  contractAgreements: (IContractAgreementTO | IndexedContractAgreement)[] = [];
  sortedAgreements: (IContractAgreementTO | IndexedContractAgreement)[] = [];
  totalNumberOfContractAgreements: number = 0;
  pageSize: number = 10;
  pageIndex: number = 0;
  expandedItemId: string | null = null;
  isTransferButtonDisabled = false;
  contractDetailsToExport?: IContractDetailsTO = undefined;

  constructor(private readonly apiService: ApiService, private readonly popUpMessage: MatSnackBar) {
  }

  async getContractAgreements() {
    let response: IContractAgreementsResponseTO = await this.apiService.getContractAgreements({
      offset: this.pageIndex * this.pageSize,
      limit: this.pageSize
    });
    this.totalNumberOfContractAgreements = response.totalNumberOfContractAgreements;
    this.contractAgreements = response.contractAgreements;
    this.contractAgreements.sort((a, b) => {
      return a.contractSigningDate > b.contractSigningDate ? -1 : 1;
    });
    this.contractAgreements = this.contractAgreements.map((item, index) => ({
      ...item,
      initialIndex: this.pageIndex * this.pageSize + index
    }));
    this.sortedAgreements = this.contractAgreements.slice();
  }

  sortData(sort: Sort) {
    const data = this.contractAgreements.slice();
    if (!sort.active || sort.direction === '') {
      this.contractAgreements = data;
      return;
    }

    data.sort((a, b) => {
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
        case 'validity':
          return this.compare(+this.isAnyPolicyInvalid(a.enforcementPolicies), +this.isAnyPolicyInvalid(b.enforcementPolicies), isAsc);
        default:
          return 0;
      }
    });

    this.sortedAgreements = data;
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
    return item.dataOffering === true;
  }

  isProvider(item: IContractAgreementTO) {
    return item.provider === true;
  }

  async retrieveAndSetOfferDetails(id: string) {
    this.contractDetailsToExport = undefined;
    this.contractDetailsExportView.informationRetrievalStatusMessage.showInfoMessage()
    let contractAgreement = this.contractAgreements.find(agreement => agreement.id === id);
    this.apiService.getOfferWithTimestampByContractAgreementId(id).then(response => {
      console.log(response);
      this.contractDetailsExportView.informationRetrievalStatusMessage.hideAllMessages();
      this.contractDetailsToExport = {
        id: contractAgreement.id,
        assetId: contractAgreement.assetId,
        catalogOffering: response.catalogOffering,
        offerRetrievalDate: response.offerRetrievalDate,
        policy: contractAgreement.policy,
        enforcementPolicies: contractAgreement.enforcementPolicies,
        contractSigningDate: contractAgreement.contractSigningDate,
        consumerDetails: contractAgreement.consumerDetails,
        providerDetails: contractAgreement.providerDetails,
        dataOffering: contractAgreement.dataOffering,
      } as IContractDetailsTO;
    }).catch((e: HttpErrorResponse) => {
      console.log(e?.error?.detail || e?.error || e?.message);
      this.contractDetailsExportView.informationRetrievalStatusMessage.showErrorMessage(e?.error?.detail || e?.error || e?.message);
    });
  }

  isAnyPolicyInvalid(enforcementPolicies: IEnforcementPolicyUnion[]): boolean {
    return enforcementPolicies.some(policy => !policy.valid)
  }

  shouldTransferButtonBeDisabled(item: IContractAgreementTO): boolean {
    return this.isTransferButtonDisabled || this.isAnyPolicyInvalid(item.enforcementPolicies);
  }

  onPageChange(event: any): void {
    this.pageIndex = event.pageIndex;
    this.handleGetContractAgreements();
    this.resetSort();
  }

  resetSort() {
    this.sortDirective.sort({ id: '', start: 'asc', disableClear: false });
  }

  getInitialRowNumber(item: IContractAgreementTO): string {
    return this.isValidIndexedContractAgreement(item) ? (item.initialIndex + 1).toString() : "";
  }

  isValidIndexedContractAgreement(item: IContractAgreementTO): item is IndexedContractAgreement {
    return (item as IndexedContractAgreement).initialIndex !== undefined;
  }
}
