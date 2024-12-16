import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DataOfferDetailsViewComponent } from './data-offer-details-view.component';

describe('DataOfferDetailsViewComponent', () => {
  let component: DataOfferDetailsViewComponent;
  let fixture: ComponentFixture<DataOfferDetailsViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DataOfferDetailsViewComponent]
    });
    fixture = TestBed.createComponent(DataOfferDetailsViewComponent);
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
