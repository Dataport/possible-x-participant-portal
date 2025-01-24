import {Component, Input} from '@angular/core';
import {
  isEverythingAllowedPolicy,
  isParticipantRestrictionPolicy,
  asParticipantRestrictionPolicy,
  isStartDatePolicy, asStartDatePolicy,
  isEndDatePolicy, asEndDatePolicy
} from '../../../../utils/policy-utils';
import {
  IEnforcementPolicyUnion,
  IPxExtendedServiceOfferingCredentialSubject
} from "../../../../services/mgmt/api/backend";
import {NameMappingService} from "../../../../services/mgmt/name-mapping.service";

@Component({
  selector: 'app-data-offer-details-view',
  templateUrl: './data-offer-details-view.component.html',
  styleUrls: ['./data-offer-details-view.component.scss']
})
export class DataOfferDetailsViewComponent {
  @Input() catalogOffering?: IPxExtendedServiceOfferingCredentialSubject = undefined;
  @Input() enforcementPolicies?: IEnforcementPolicyUnion[] = undefined;

  protected isEverythingAllowedPolicy = isEverythingAllowedPolicy;

  protected isParticipantRestrictionPolicy = isParticipantRestrictionPolicy;

  protected asParticipantRestrictionPolicy = asParticipantRestrictionPolicy;

  protected isStartDatePolicy = isStartDatePolicy;

  protected asStartDatePolicy = asStartDatePolicy;

  protected isEndDatePolicy = isEndDatePolicy;

  protected asEndDatePolicy = asEndDatePolicy;

  constructor(private readonly nameMappingService: NameMappingService) {
  }

  getNameById(id: string): string {
    return this.nameMappingService.getNameById(id);
  }

  getNameIdStringById(id: string): string {
    const name = this.getNameById(id);
    return `${name} (${id})`;
  }

  getCopyrightOwnerString(copyrightOwner: string) {
    const copyrightOwnerId = copyrightOwner.replace(/\s+/g, ""); // remove whitespaces
    const didRegex = /^did:web:[a-zA-Z0-9.-]+(:[a-zA-Z0-9.-]+)*$/;

    if (didRegex.test(copyrightOwnerId)) {
      return this.getNameIdStringById(copyrightOwnerId);
    } else {
      return copyrightOwner.trim();
    }
  }
}
