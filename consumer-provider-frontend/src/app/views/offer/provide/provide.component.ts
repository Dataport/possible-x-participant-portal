import { Component, ViewChild } from '@angular/core';
import { ApiService } from '../../../services/mgmt/api/api.service'
import { StatusMessageComponent } from '../../common-views/status-message/status-message.component';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-provide',
  templateUrl: './provide.component.html',
  styleUrl: './provide.component.scss'
})
export class ProvideComponent {
  @ViewChild('offerCreationStatusMessage') private offerCreationStatusMessage!: StatusMessageComponent;
  offerType: string = "";
  offerName: string = "";

  constructor(private apiService: ApiService) {}

  protected hideAllMessages(){
    this.offerCreationStatusMessage.hideAllMessages();
  }

  getHealth(): void {
    this.apiService.getHealth().subscribe(response => {
      console.log(response);
    });
  }

  protected async createOffer() {
    this.offerCreationStatusMessage.showInfoMessage();

    this.apiService.createOffer().then(response => {
      console.log(response);
      this.offerCreationStatusMessage.showSuccessMessage(`ID: ${response.id}`, 20000);
    }).catch((e: HttpErrorResponse) => {
      this.offerCreationStatusMessage.showErrorMessage(e.error.message);
    });
  }
}
