import { Component, ViewChild } from '@angular/core';
import { ApiService } from '../../../services/mgmt/api/api.service'
import { StatusMessageComponent } from '../../common-views/status-message/status-message.component';
import { HttpErrorResponse } from '@angular/common/http';
import { POLICY_MAP } from '../../../constants';

@Component({
  selector: 'app-provide',
  templateUrl: './provide.component.html',
  styleUrl: './provide.component.scss'
})
export class ProvideComponent {
  @ViewChild('offerCreationStatusMessage') private offerCreationStatusMessage!: StatusMessageComponent;
  offerType: string = "";
  offerName: string = "";
  policy: string = "";

  policyMap = POLICY_MAP;

  constructor(private apiService: ApiService) {}

  protected hideAllMessages(){
    this.offerCreationStatusMessage.hideAllMessages();
  }

  async createOffer() {
    this.offerCreationStatusMessage.showInfoMessage();

    this.apiService.createOffer({
      offerType: this.offerType,
      offerName: this.offerName,
      offerDescription: 'PLACEHOLDER',
      fileName: 'PLACEHOLDER',
      policy: this.policyMap[this.policy]
    }).then(response => {
      console.log(response);
      this.offerCreationStatusMessage.showSuccessMessage(`ID: ${response.id}`, 20000);
    }).catch((e: HttpErrorResponse) => {
      this.offerCreationStatusMessage.showErrorMessage(e.error.message);
    });
  }

  protected getPolicyNames() {
    return Object.keys(this.policyMap);
  }

  protected getPolicyDetails(policy: string): string {
    const policyDetails = this.policyMap[policy];
    return policyDetails ? JSON.stringify(policyDetails, null, 2) : '';
  }
}
