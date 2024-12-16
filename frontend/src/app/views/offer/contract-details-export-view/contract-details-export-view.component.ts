import {Component, Input} from '@angular/core';
import {DatePipe} from "@angular/common";
import {IContractDetailsTO} from "../../../services/mgmt/api/backend";

@Component({
  selector: 'app-contract-details-export-view',
  templateUrl: './contract-details-export-view.component.html',
  styleUrls: ['./contract-details-export-view.component.scss'],
  providers: [DatePipe]
})
export class ContractDetailsExportViewComponent {

  @Input() contractDetails?: IContractDetailsTO = undefined;

  constructor(private datePipe: DatePipe) {}

  getFormattedContractSigningTimestamp(): string {
    return this.datePipe.transform(this.contractDetails?.contractSigningDate, 'yyyyMMdd_HHmmss_z') || '';
  }

}
