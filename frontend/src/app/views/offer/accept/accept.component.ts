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
import {ApiService} from '../../../services/mgmt/api/api.service';
import {HttpErrorResponse} from '@angular/common/http';
import {StatusMessageComponent} from '../../common-views/status-message/status-message.component';
import {
  IAcceptOfferResponseTO, IContractPartiesTO,
  IOfferDetailsTO,
  IPxExtendedServiceOfferingCredentialSubject
} from '../../../services/mgmt/api/backend';

@Component({
  selector: 'app-accept-offer',
  templateUrl: './accept.component.html',
  styleUrls: ['./accept.component.scss']
})
export class AcceptComponent implements OnChanges {
  @Input() offer?: IOfferDetailsTO = undefined;
  @Output() dismiss: EventEmitter<any> = new EventEmitter();
  @Output() negotiatedContract: EventEmitter<IAcceptOfferResponseTO> = new EventEmitter();
  @ViewChild('acceptOfferStatusMessage') acceptOfferStatusMessage!: StatusMessageComponent;

  @ViewChild('viewContainerRef', { read: ViewContainerRef, static: true }) viewContainerRef: ViewContainerRef;
  @ViewChild('accordion', { read: TemplateRef, static: true }) accordion: TemplateRef<any>;

  isAcceptButtonDisabled = false;
  contractParties?: IContractPartiesTO = undefined;
  printTimestamp?: Date;

  constructor(private apiService: ApiService) {
  }

  ngOnChanges(): void {
    if(this.offer) {
      this.viewContainerRef.createEmbeddedView(this.accordion);
      this.getContractPartiesDetails();
    } else {
      this.viewContainerRef.clear();
    }
  }

  async acceptContractOffer() {
    this.acceptOfferStatusMessage.showInfoMessage();
    console.log("'Accept Contract Offer' button pressed");
    this.isAcceptButtonDisabled = true;
    this.apiService.acceptContractOffer({
      counterPartyAddress: this.offer == undefined ? "" : this.offer.catalogOffering["px:providerUrl"],
      edcOfferId: this.offer == undefined ? "" : this.offer.edcOfferId,
      dataOffering: this.offer == undefined ? false : this.offer.dataOffering
    }).then(response => {
      console.log(response);
      this.negotiatedContract.emit(response);
      this.acceptOfferStatusMessage.showSuccessMessage("Contract Agreement ID: " + response.contractAgreementId);
    }).catch((e: HttpErrorResponse) => {
      this.acceptOfferStatusMessage.showErrorMessage(e.error.detail || e.error || e.message);
      this.isAcceptButtonDisabled = false;
    });
  };

  async getContractPartiesDetails() {
    console.log("Retrieve Contract Parties Details");
    this.apiService.getContractParties({
      providerId: this.offer.catalogOffering["gx:providedBy"].id,
    }).then(response => {
      console.log(response);
      this.contractParties = response;
    }).catch((e: HttpErrorResponse) => {
      console.error(e.error.detail || e.error || e.message);
    });
  };

  cancel(): void {
    this.dismiss.emit();
  }

  containsPII(catalogOffering: IPxExtendedServiceOfferingCredentialSubject): boolean {
    return catalogOffering["gx:aggregationOf"][0]["gx:containsPII"];
  }

  setTimestamp(){
   this.printTimestamp = new Date();
  }

  isHttpOrHttps(url: string): boolean {
    return url.startsWith('http://') || url.startsWith('https://');
  }
}
