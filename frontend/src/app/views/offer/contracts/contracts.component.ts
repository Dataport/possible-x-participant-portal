/*
 *  Copyright 2024-2025 Dataport. All rights reserved. Developed as part of the POSSIBLE project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import {Component, OnInit, ViewChild} from '@angular/core';
import {
  IContractAgreementTO,
  IContractDetailsTO,
  IEnforcementPolicyUnion, IPolicy,
} from '../../../services/mgmt/api/backend';
import {HttpErrorResponse} from "@angular/common/http";
import {StatusMessageComponent} from "../../common-views/status-message/status-message.component";
import {ApiService} from '../../../services/mgmt/api/api.service';
import {MatSnackBar} from "@angular/material/snack-bar";
import {Sort} from "@angular/material/sort";
import {
  ContractDetailsExportViewComponent
} from "../contract-details-export-view/contract-details-export-view.component";
import {commonMessages} from "../../../../environments/common-messages";

@Component({
  selector: 'app-contracts',
  templateUrl: './contracts.component.html',
  styleUrls: ['./contracts.component.scss']
})
export class ContractsComponent implements OnInit {
  @ViewChild("requestContractAgreementsStatusMessage") public requestContractAgreementsStatusMessage!: StatusMessageComponent;
  @ViewChild(("contractDetailsExportView")) public contractDetailsExportView!: ContractDetailsExportViewComponent;
  contractAgreements: IContractAgreementTO[] = [];
  sortedAgreements: IContractAgreementTO[] = [];
  pagedItems: IContractAgreementTO[] = [];
  pageSize = 10;
  pageIndex = 0;
  expandedItemId: string | null = null;
  isTransferButtonDisabled = false;
  contractDetailsToExport?: IContractDetailsTO = undefined;

  constructor(private readonly apiService: ApiService, private readonly popUpMessage: MatSnackBar) {
  }

  async getContractAgreements() {
    this.contractAgreements = await this.apiService.getContractAgreements();
    this.contractAgreements.sort((a, b) => {
      return a.contractSigningDate > b.contractSigningDate ? -1 : 1;
    });
    this.sortedAgreements = this.contractAgreements.slice();
    this.updatePagedItems()
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
    this.pageIndex = 0;
    this.updatePagedItems()
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
    this.apiService.transferDataOffer({
      contractAgreementId: contractAgreement.id,
      counterPartyAddress: contractAgreement.assetDetails.providerUrl,
      edcOfferId: contractAgreement.assetId,
    }).then(response => {
      console.log(response.transferProcessState);
      this.popUpMessage.open("Data Transfer successful: " + response.transferProcessState, 'Close', {
        duration: undefined,
      });
      this.isTransferButtonDisabled = false;
    }).catch((e: HttpErrorResponse) => {
      console.log(e);
      let errorMessage = "";
      if (e.status === 500) {
        errorMessage = commonMessages.general_error;
      } else {
        errorMessage = e.error.details;
      }
      this.popUpMessage.open("Data Transfer failed: " + errorMessage, 'Close', {
        duration: undefined,
      });
      this.isTransferButtonDisabled = false;
    }).catch(e => {
      console.log(e);
      this.popUpMessage.open("Data Transfer failed: " + commonMessages.general_error, 'Close', {
        duration: undefined,
      });
      this.isTransferButtonDisabled = false;
    });
  }

  getPolicyAsString(policy: IPolicy): string {
    return JSON.stringify(policy, null, 2);
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
      console.log(e);
      if (e.status === 500) {
        this.contractDetailsExportView.informationRetrievalStatusMessage.showErrorMessage(commonMessages.general_error);
      } else {
        this.contractDetailsExportView.informationRetrievalStatusMessage.showErrorMessage(e.error.details);
      }
    }).catch(e => {
      console.log(e);
      this.contractDetailsExportView.informationRetrievalStatusMessage.showErrorMessage(commonMessages.general_error);
    });
  }

  isAnyPolicyInvalid(enforcementPolicies: IEnforcementPolicyUnion[]): boolean {
    return enforcementPolicies.some(policy => !policy.valid)
  }

  shouldTransferButtonBeDisabled(item: IContractAgreementTO): boolean {
    return this.isTransferButtonDisabled || this.isAnyPolicyInvalid(item.enforcementPolicies);
  }

  onPageChange(event: any): void {
    this.pageSize = event.pageSize;
    this.pageIndex = event.pageIndex;
    this.updatePagedItems();
  }

  updatePagedItems() {
    const startIndex = this.pageIndex * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.pagedItems = this.sortedAgreements.slice(startIndex, endIndex);
  }

  private handleGetContractAgreements() {
    this.getContractAgreements().catch((e: HttpErrorResponse) => {
      this.requestContractAgreementsStatusMessage.showErrorMessage(e.error.detail);
    }).catch(_ => {
      this.requestContractAgreementsStatusMessage.showErrorMessage("Unknown error occurred");
    });
  }
}
