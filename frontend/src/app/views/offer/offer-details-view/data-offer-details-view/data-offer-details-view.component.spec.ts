import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { DataOfferDetailsViewComponent } from './data-offer-details-view.component';
import { NameMappingService } from "../../../../services/mgmt/name-mapping.service";

describe('DataOfferDetailsViewComponent', () => {
  let component: DataOfferDetailsViewComponent;
  let fixture: ComponentFixture<DataOfferDetailsViewComponent>;
  let nameMappingService: jasmine.SpyObj<NameMappingService>;

  beforeEach(() => {
    const nameMappingServiceSpy = jasmine.createSpyObj('NameMappingService', ['getNameById']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DataOfferDetailsViewComponent],
      providers: [{ provide: NameMappingService, useValue: nameMappingServiceSpy }]
    });
    fixture = TestBed.createComponent(DataOfferDetailsViewComponent);
    component = fixture.componentInstance;
    component.catalogOffering = {
        "gx:providedBy": {id: "participantId"},
        "gx:dataProtectionRegime": [],
        "gx:aggregationOf": [
          {
            "gx:copyrightOwnedBy": ["participantId"],
            "gx:producedBy": {id: "participantId"},
            "gx:containsPII": true,
            "gx:legitimateInterest": {"gx:dataProtectionContact": "contact",
              "gx:legalBasis": "legalBasis",}

          } as any
        ]
      } as any;
    nameMappingService = TestBed.inject(NameMappingService) as jasmine.SpyObj<NameMappingService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return name', () => {
    const name = 'Test Name';
    nameMappingService.getNameById.and.returnValue(name);

    const result = component.getNameById("any id");
    expect(result).toBe(name);
  });

  it('should return name and ID string', () => {
    const id = '123';
    const name = 'Test Name';
    nameMappingService.getNameById.and.returnValue(name);

    const result = component.getNameIdStringById(id);
    expect(result).toBe(`${name} (${id})`);
  });

  it('should return copyright owner - did:web', () => {
    const copyrightOwner = ' did:web:didwebservice .dev.possible-x.de:participant:901e847f-  bded-32d5-8301-c0e2dfa8439f ';
    const name = 'Provider Org';
    nameMappingService.getNameById.and.returnValue(name);

    const result = component.getCopyrightOwnerString(copyrightOwner);
    expect(result).toBe(`${name} (${copyrightOwner.replace(/\s+/g, '')})`);
  });

  it('should return copyright owner - string', () => {
    const copyrightOwner = 'Provider Org';

    const result = component.getCopyrightOwnerString(copyrightOwner);
    expect(result).toBe(copyrightOwner);
  });
});
