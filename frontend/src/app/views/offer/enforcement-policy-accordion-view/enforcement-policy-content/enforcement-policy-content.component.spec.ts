import {ComponentFixture, TestBed} from '@angular/core/testing';

import {HttpClientTestingModule} from '@angular/common/http/testing';

import {EnforcementPolicyContentComponent} from './enforcement-policy-content.component';
import {NameMappingService} from "../../../../services/mgmt/name-mapping.service";
import {IEnforcementPolicy} from "../../../../services/mgmt/api/backend";

describe('EnforcementPolicyContentComponent', () => {
  let component: EnforcementPolicyContentComponent;
  let fixture: ComponentFixture<EnforcementPolicyContentComponent>;
  let nameMappingService: jasmine.SpyObj<NameMappingService>;

  beforeEach(() => {
    const nameMappingServiceSpy = jasmine.createSpyObj('NameMappingService', ['getNameById']);

    TestBed.configureTestingModule({
      declarations: [EnforcementPolicyContentComponent],
      imports: [HttpClientTestingModule],
      providers: [{ provide: NameMappingService, useValue: nameMappingServiceSpy }]
    });
    fixture = TestBed.createComponent(EnforcementPolicyContentComponent);
    component = fixture.componentInstance;
    nameMappingService = TestBed.inject(NameMappingService) as jasmine.SpyObj<NameMappingService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set input properties correctly', () => {
    const policy: IEnforcementPolicy = { "@type": "EverythingAllowedPolicy", "valid": true };
    component.enforcementPolicy = policy;
    fixture.detectChanges();

    expect(component.enforcementPolicy).toBe(policy);
  });


  it('should return name and ID string', () => {
    const id = '123';
    const name = 'Test Name';
    nameMappingService.getNameById.and.returnValue(name);

    const result = component.getNameIdStringById(id);
    expect(result).toBe(`${name} (${id})`);
  });
});
