import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsumeComponent } from './consume.component';
import { ApiService } from '../../../services/mgmt/api/api.service';
import { of } from 'rxjs';

describe('ConsumeComponent', () => {
  let component: ConsumeComponent;
  let fixture: ComponentFixture<ConsumeComponent>;
  let apiService: jasmine.SpyObj<ApiService>;

  beforeEach(async () => {
    const apiServiceSpy = jasmine.createSpyObj('ApiService', ['acceptContractOffer']);

    await TestBed.configureTestingModule({
      declarations: [ConsumeComponent],
      providers: [
        { provide: ApiService, useValue: apiServiceSpy }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConsumeComponent);
    component = fixture.componentInstance;
    apiService = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call apiService on acceptContractOffer', () => {
    apiService.acceptContractOffer.and.returnValue(of("success"));
    component.acceptContractOffer();
    expect(apiService.acceptContractOffer).toHaveBeenCalled();
  });

});
