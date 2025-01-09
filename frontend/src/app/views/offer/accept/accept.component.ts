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
  IPxExtendedServiceOfferingCredentialSubject
} from '../../../services/mgmt/api/backend';

import {NameMappingService} from "../../../services/mgmt/name-mapping.service";

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

  @ViewChild('viewContainerRef', {read: ViewContainerRef, static: true}) viewContainerRef: ViewContainerRef;
  @ViewChild('accordion', {read: TemplateRef, static: true}) accordion: TemplateRef<any>;

  isConsumed = false;
  isPoliciesAccepted = false;
  isTnCAccepted = false;

  constructor(private apiService: ApiService, private readonly nameMappingService: NameMappingService) {
  }

  getNameIdStringById(id: string): string {
    const name = this.nameMappingService.getNameById(id);
    return `${name} (${id})`;
  }

  ngOnChanges(): void {
    if (this.offer) {
      this.viewContainerRef.createEmbeddedView(this.accordion);
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
      this.acceptOfferStatusMessage.showErrorMessage(e?.error?.detail || e?.error || e?.message);
      this.isConsumed = false;
    });
  };

  cancel(): void {
    this.dismiss.emit();
  }

  containsPII(catalogOffering: IPxExtendedServiceOfferingCredentialSubject): boolean {
    return catalogOffering["gx:aggregationOf"][0]["gx:containsPII"];
  }

  isHttpOrHttps(url: string): boolean {
    return url.startsWith('http://') || url.startsWith('https://');
  }

  isButtonDisabled(): boolean {
    return !this.isPoliciesAccepted || !this.isTnCAccepted || this.isConsumed;
  }
}
