import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServiceOfferPrintViewComponent } from './service-offer-print-view.component';
import {IOfferDetailsTO} from "../../../../services/mgmt/api/backend";

describe('ServiceOfferPrintViewComponent', () => {
  let component: ServiceOfferPrintViewComponent;
  let fixture: ComponentFixture<ServiceOfferPrintViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ServiceOfferPrintViewComponent]
    });
    fixture = TestBed.createComponent(ServiceOfferPrintViewComponent);
    component = fixture.componentInstance;
    component.offer = {
      catalogOffering: {
        "gx:providedBy": {id: "participantId"},
        "gx:dataProtectionRegime": [],
        "gx:aggregationOf": [
          {
            "gx:containsPII": false
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
