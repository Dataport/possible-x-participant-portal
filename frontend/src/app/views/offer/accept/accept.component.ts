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
  IEnforcementPolicy,
  IEverythingAllowedPolicy, IEverythingAllowedPolicyBuilder,
  IOfferDetailsTO, IParticipantRestrictionPolicy
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

  protected isEverythingAllowedPolicy: (policy: IEnforcementPolicy) => boolean
    = policy => (policy['@type'] === 'EverythingAllowedPolicy');

  protected isParticipantRestrictionPolicy: (policy: IEnforcementPolicy) => boolean
    = policy => (policy['@type'] === 'ParticipantRestrictionPolicy');

  protected asEverythingAllowedPolicy: (policy: IEnforcementPolicy) => IEverythingAllowedPolicy
    = policy => (policy as IEverythingAllowedPolicy);

  protected asParticipantRestrictionPolicy: (policy: IEnforcementPolicy) => IParticipantRestrictionPolicy
    = policy => (policy as IParticipantRestrictionPolicy);

  constructor(private apiService: ApiService) {
  }

  ngOnChanges(): void {
    if(this.offer) {
      this.viewContainerRef.createEmbeddedView(this.accordion);
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

  cancel(): void {
    this.dismiss.emit();
  }

  isHttpOrHttps(url: string): boolean {
    return url.startsWith('http://') || url.startsWith('https://');
  }
}
