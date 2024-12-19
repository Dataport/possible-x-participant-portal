import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DataOfferPrintViewComponent} from './data-offer-print-view.component';

describe('DataOfferPrintViewComponent', () => {
  let component: DataOfferPrintViewComponent;
  let fixture: ComponentFixture<DataOfferPrintViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DataOfferPrintViewComponent]
    });
    fixture = TestBed.createComponent(DataOfferPrintViewComponent);
    component = fixture.componentInstance;
    component.offer = {
      catalogOffering: {
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
      },
      participantNames: {
        "participantId": {
          participantName: "participantName"
        }
      },
      providerDetails: {
        participantId: "participantId",
        participantName: "participantName",
        participantEmail: "participantEmail"
      }
    } as any;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
