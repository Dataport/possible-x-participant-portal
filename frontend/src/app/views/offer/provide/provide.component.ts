import {Component, ViewChild} from '@angular/core';
import {ApiService} from '../../../services/mgmt/api/api.service'
import {StatusMessageComponent} from '../../common-views/status-message/status-message.component';
import {HttpErrorResponse} from '@angular/common/http';
import {POLICY_MAP} from '../../../constants';

@Component({
  selector: 'app-provide',
  templateUrl: './provide.component.html',
  styleUrl: './provide.component.scss'
})
export class ProvideComponent {
  offerType: string = "";
  offerName: string = "";
  policy: string = "";
  offerDescription: string = "";
  fileName: string = "";
  policyMap = POLICY_MAP;
  @ViewChild('offerCreationStatusMessage') private offerCreationStatusMessage!: StatusMessageComponent;

  constructor(private apiService: ApiService) {
  }

  async createOffer() {
    this.offerCreationStatusMessage.showInfoMessage();

    this.apiService.createOffer({
      offerType: this.offerType,
      offerName: this.offerName,
      offerDescription: this.offerDescription,
      fileName: this.fileName,
      policy: this.policyMap[this.policy].policy
    }).then(response => {
      console.log(response);
      this.offerCreationStatusMessage.showSuccessMessage("", 20000);
    }).catch((e: HttpErrorResponse) => {
      this.offerCreationStatusMessage.showErrorMessage(e.error.message);
    });
  }

  protected hideAllMessages() {
    this.offerCreationStatusMessage.hideAllMessages();
  }

  protected getPolicyNames() {
    return Object.keys(this.policyMap);
  }

  protected getPolicyDetails(policy: string): string {
    const policyDetails = this.policyMap[policy];
    return policyDetails ? JSON.stringify(policyDetails, null, 2) : '';
  }
}
