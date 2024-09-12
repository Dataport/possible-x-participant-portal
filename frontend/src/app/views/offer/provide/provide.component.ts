import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {ApiService} from '../../../services/mgmt/api/api.service'
import {StatusMessageComponent} from '../../common-views/status-message/status-message.component';
import {HttpErrorResponse} from '@angular/common/http';
import {POLICY_MAP} from '../../../constants';
import { OfferingWizardExtensionComponent } from '../../../wizard-extension/offering-wizard-extension/offering-wizard-extension.component';
import { TBR_SERVICE_OFFERING_ID, TBR_DATA_RESOURCE_ID } from '../offer-data';

@Component({
  selector: 'app-provide',
  templateUrl: './provide.component.html',
  styleUrls: ['./provide.component.scss']
})
export class ProvideComponent implements AfterViewInit{
  offerType: string = "data";
  offerName: string = "";
  policy: string = "";
  offerDescription: string = "";
  fileName: string = "";
  policyMap = POLICY_MAP;
  @ViewChild('offerCreationStatusMessage') private offerCreationStatusMessage!: StatusMessageComponent;
  @ViewChild("wizardExtension") private wizardExtension: OfferingWizardExtensionComponent;

  constructor(private apiService: ApiService) {
  }

  ngAfterViewInit(): void {
      this.prefillWizardNewOffering();
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
      this.offerCreationStatusMessage.showErrorMessage(e.error.detail);
    });
  }

  protected hideAllMessages() {
    this.offerCreationStatusMessage.hideAllMessages();
  }

  prefillWizardNewOffering() {

    let gxServiceOfferingCs = {
      "gx:providedBy": {
        "@id": "did:web:someorga.eu"
      },
      type: "gx:ServiceOffering"
    }

    let prefillSd: any[] = [ gxServiceOfferingCs ];

    if (!this.isOfferingDataOffering()) {
      this.wizardExtension.loadShape(this.offerType, TBR_SERVICE_OFFERING_ID, null).then(_ => {
        this.wizardExtension.prefillFields(prefillSd);
      });
    } else {
      let gxDataResourceCs = {
        "gx:producedBy": {
          "@id": "did:web:someorga.eu"
        },
        "gx:exposedThrough": [TBR_SERVICE_OFFERING_ID],
        "gx:copyrightOwnedBy": ["did:web:someorga.eu"],
        "gx:containsPII": false,
        type: "gx:DataResource"
      }

      prefillSd.push(gxDataResourceCs);

      this.wizardExtension.loadShape(this.offerType, TBR_SERVICE_OFFERING_ID, TBR_DATA_RESOURCE_ID).then(_ => {
        this.wizardExtension.prefillFields(prefillSd);
      });
    }    
  }

  protected isOfferingDataOffering() {
    return this.offerType === "data";
  }
}
