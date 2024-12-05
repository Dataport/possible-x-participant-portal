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
  IAcceptOfferResponseTO,
  IOfferDetailsTO,
  IParticipantDetailsTO,
  IPxExtendedServiceOfferingCredentialSubject
} from '../../../services/mgmt/api/backend';
import {
  isEverythingAllowedPolicy,
  isParticipantRestrictionPolicy,
  asParticipantRestrictionPolicy
} from '../../../utils/policy-utils';

@Component({
  selector: 'app-accept-offer',
  templateUrl: './accept.component.html',
  styleUrls: ['./accept.component.scss']
})
export class AcceptComponent implements OnChanges {
  @Input() offer?: IOfferDetailsTO = undefined;
  @Output() dismiss: EventEmitter<any> = new EventEmitter();
  @Output() negotiatedContract: EventEmitter<IAcceptOfferResponseTO> = new EventEmitter();
  @Output() retrievedProviderDetails: EventEmitter<IParticipantDetailsTO> = new EventEmitter();
  @ViewChild('acceptOfferStatusMessage') acceptOfferStatusMessage!: StatusMessageComponent;
  @ViewChild('retrieveConsumerDetailsMessage') retrieveConsumerDetailsMessage!: StatusMessageComponent;
  @ViewChild('retrieveProviderDetailsMessage') retrieveProviderDetailsMessage!: StatusMessageComponent;

  @ViewChild('viewContainerRef', {read: ViewContainerRef, static: true}) viewContainerRef: ViewContainerRef;
  @ViewChild('accordion', {read: TemplateRef, static: true}) accordion: TemplateRef<any>;

  isConsumed = false;
  isPoliciesAccepted = false;
  isTnCAccepted = false;
  providerDetails?: IParticipantDetailsTO = undefined;
  printTimestamp?: Date;

  protected isEverythingAllowedPolicy = isEverythingAllowedPolicy;

  protected isParticipantRestrictionPolicy = isParticipantRestrictionPolicy;

  protected asParticipantRestrictionPolicy = asParticipantRestrictionPolicy;

  constructor(private apiService: ApiService) {
  }

  ngOnChanges(): void {
    if (this.offer) {
      this.viewContainerRef.createEmbeddedView(this.accordion);
      this.getParticipantDetails();
    } else {
      this.viewContainerRef.clear();
    }
    this.isConsumed = false;
    this.isPoliciesAccepted = false;
    this.isTnCAccepted = false;
  }

  async acceptContractOffer() {
    this.isConsumed = true;
    this.acceptOfferStatusMessage.showInfoMessage();
    console.log("'Accept Contract Offer' button pressed");
    this.apiService.acceptContractOffer({
      counterPartyAddress: this.offer == undefined ? "" : this.offer.catalogOffering["px:providerUrl"],
      edcOfferId: this.offer == undefined ? "" : this.offer.edcOfferId,
      dataOffering: this.offer == undefined ? false : this.offer.dataOffering,
    }).then(response => {
      console.log(response);
      this.negotiatedContract.emit(response);
      this.acceptOfferStatusMessage.showSuccessMessage("Contract Agreement ID: " + response.contractAgreementId);
    }).catch((e: HttpErrorResponse) => {
      this.acceptOfferStatusMessage.showErrorMessage(e.error.detail || e.error || e.message);
      this.isConsumed = false;
    });
  };

  async getParticipantDetails() {
    console.log("Retrieve Provider details.");
    this.retrieveProviderDetailsMessage.hideAllMessages();

    this.apiService.getParticipantDetails$GET$participant_details_participantId(this.offer.catalogOffering["gx:providedBy"].id)
      .then(response => {
        console.log(response);
        this.retrievedProviderDetails.emit(response);
        this.providerDetails = response;
      }).catch((e: HttpErrorResponse) => {
      this.retrieveProviderDetailsMessage.showErrorMessage(e.error.detail || e.error || e.message);
    });

    //TODO: Retrieve name of participants in enforced policy
  };

  cancel(): void {
    this.dismiss.emit();
  }

  containsPII(catalogOffering: IPxExtendedServiceOfferingCredentialSubject): boolean {
    return catalogOffering["gx:aggregationOf"][0]["gx:containsPII"];
  }

  setTimestamp() {
    this.printTimestamp = new Date();
  }

  isHttpOrHttps(url: string): boolean {
    return url.startsWith('http://') || url.startsWith('https://');
  }

  isButtonDisabled(): boolean {
    return !this.isPoliciesAccepted || !this.isTnCAccepted || this.isConsumed || !this.providerDetails;
  }
}
