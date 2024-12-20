import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ServiceOfferDetailsViewComponent } from './service-offer-details-view.component';
import { NameMappingService } from "../../../../services/mgmt/name-mapping.service";

describe('ServiceOfferDetailsViewComponent', () => {
  let component: ServiceOfferDetailsViewComponent;
  let fixture: ComponentFixture<ServiceOfferDetailsViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ServiceOfferDetailsViewComponent],
      providers: [NameMappingService]
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
