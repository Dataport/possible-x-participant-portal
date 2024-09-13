import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {ApiService} from '../../../services/mgmt/api/api.service'
import {StatusMessageComponent} from '../../common-views/status-message/status-message.component';
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
  participantId = "";
  @ViewChild('offerCreationStatusMessage') private offerCreationStatusMessage!: StatusMessageComponent;
  @ViewChild("wizardExtension") private wizardExtension: OfferingWizardExtensionComponent;

  constructor(private apiService: ApiService) {
  }
  ngAfterViewInit(): void {
    this.prefillWizardNewOffering();
  }

  protected hideAllMessages() {
    this.offerCreationStatusMessage.hideAllMessages();
  }

  async prefillWizardNewOffering() {
    await this.retrieveAndSetParticipantId();

    let gxServiceOfferingCs = {
      "gx:providedBy": {
        "@id": this.participantId
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
          "@id": this.participantId
        },
        "gx:exposedThrough": [TBR_SERVICE_OFFERING_ID],
        "gx:copyrightOwnedBy": [this.participantId],
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

  async retrieveAndSetParticipantId() {
    try {
      const response = await this.apiService.getParticipantId();
      console.log(response);
      this.participantId = response.participantId;
    }catch(e) {
      console.error(e);
    };
  }
}
