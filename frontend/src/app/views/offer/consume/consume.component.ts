import {Component, ViewChild} from '@angular/core';
import {IAcceptOfferResponseTO, IContractPartiesTO, IOfferDetailsTO} from '../../../services/mgmt/api/backend';
import {MatStepper} from "@angular/material/stepper";
import {SelectComponent} from "../select/select.component";
import {AcceptComponent} from "../accept/accept.component";
import {TransferComponent} from "../transfer/transfer.component";

@Component({
  selector: 'app-consume',
  templateUrl: './consume.component.html',
  styleUrls: ['./consume.component.scss']
})
export class ConsumeComponent {
  @ViewChild("stepper") stepper: MatStepper;
  @ViewChild("select") select: SelectComponent;
  @ViewChild("accept") accept: AcceptComponent;
  @ViewChild("transfer") transfer: TransferComponent;
  selectedOffer?: IOfferDetailsTO = undefined;
  negotiatedContract?: IAcceptOfferResponseTO = undefined;
  retrievedContractParties?: IContractPartiesTO = undefined;

  setSelectedOffer(offer: IOfferDetailsTO): void {
    this.selectedOffer = offer;
    this.stepper.next();
  }

  setNegotiatedContract(contract: IAcceptOfferResponseTO): void {
    this.negotiatedContract = contract;
    this.stepper.next();
  }

  setContractParties(contractParties: IContractPartiesTO): void {
    this.retrievedContractParties = contractParties;
  }

  resetSelection() {
    this.selectedOffer = undefined;
    this.negotiatedContract = undefined;
    this.retrievedContractParties = undefined;
    this.select.queryCatalogStatusMessage.hideAllMessages();
    this.select.selectionForm.reset();
    this.accept.acceptOfferStatusMessage.hideAllMessages();
    this.transfer.dataTransferStatusMessage.hideAllMessages();
    this.stepper.reset();
  }
}
