import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfferDetailsViewComponent } from './offer-details-view.component';
import {ServiceOfferDetailsViewComponent} from "./service-offer-details-view/service-offer-details-view.component";

describe('OfferDetailsViewComponent', () => {
  let component: OfferDetailsViewComponent;
  let fixture: ComponentFixture<OfferDetailsViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OfferDetailsViewComponent, ServiceOfferDetailsViewComponent]
    });
    fixture = TestBed.createComponent(OfferDetailsViewComponent);
    component = fixture.componentInstance;
    component.catalogOffering = {
      "gx:providedBy": {id: "participantId"},
      "gx:dataProtectionRegime": [],
      "gx:aggregationOf": [
        {
          "gx:copyrightOwnedBy": {id: "participantId"},
          "gx:producedBy": {id: "participantId"},
          "gx:containsPII": true,
          "gx:legitimateInterest": {"gx:dataProtectionContact": "contact",
            "gx:legalBasis": "legalBasis",}

        } as any
      ]
    } as any;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
