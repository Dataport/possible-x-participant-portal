import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServiceOfferDetailsViewComponent } from './service-offer-details-view.component';

describe('ServiceOfferDetailsViewComponent', () => {
  let component: ServiceOfferDetailsViewComponent;
  let fixture: ComponentFixture<ServiceOfferDetailsViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ServiceOfferDetailsViewComponent]
    });
    fixture = TestBed.createComponent(ServiceOfferDetailsViewComponent);
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
