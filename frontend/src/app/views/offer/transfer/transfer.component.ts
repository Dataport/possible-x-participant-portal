import {Component, Input} from '@angular/core';
import {IAcceptOfferResponseTO, IOfferDetailsTO} from "../../../services/mgmt/api/backend";

@Component({
  selector: 'app-transfer',
  templateUrl: './transfer.component.html',
  styleUrls: ['./transfer.component.scss']
})
export class TransferComponent {
  @Input() contract?: IAcceptOfferResponseTO = undefined;


}
