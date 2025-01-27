import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ServiceOfferDetailsViewComponent } from './service-offer-details-view.component';
import { NameMappingService } from "../../../../services/mgmt/name-mapping.service";

describe('ServiceOfferDetailsViewComponent', () => {
  let component: ServiceOfferDetailsViewComponent;
  let fixture: ComponentFixture<ServiceOfferDetailsViewComponent>;
  let nameMappingService: jasmine.SpyObj<NameMappingService>;

  beforeEach(() => {
    const nameMappingServiceSpy = jasmine.createSpyObj('NameMappingService', ['getNameById']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ServiceOfferDetailsViewComponent],
      providers: [{ provide: NameMappingService, useValue: nameMappingServiceSpy }]
    });
    fixture = TestBed.createComponent(ServiceOfferDetailsViewComponent);
    component = fixture.componentInstance;
    component.catalogOffering = {
      "gx:providedBy": {id: "participantId"},
      "gx:dataProtectionRegime": [],
      "gx:aggregationOf": []
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
});
