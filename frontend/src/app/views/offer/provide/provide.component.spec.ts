import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { ProvideComponent } from './provide.component';
import { ApiService } from '../../../services/mgmt/api/api.service';
import { GridModule } from '@coreui/angular';
import { FormsModule } from '@angular/forms';
import { CommonViewsModule } from '../../common-views/common-views.module';


describe('ProvideComponent', () => {
  let component: ProvideComponent;
  let fixture: ComponentFixture<ProvideComponent>;
  let apiService: jasmine.SpyObj<ApiService>;


  beforeEach(async () => {
    const apiServiceSpy = jasmine.createSpyObj('ApiService', ['getHealth', 'createOffer']);


    await TestBed.configureTestingModule({
      declarations: [ProvideComponent],
      providers: [
        { provide: ApiService, useValue: apiServiceSpy }
      ],
      imports: [ FormsModule, GridModule, CommonViewsModule ],
    })
    .compileComponents();


    fixture = TestBed.createComponent(ProvideComponent);
    component = fixture.componentInstance;
    apiService = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    fixture.detectChanges();
  });


  it('should create', () => {
    expect(component).toBeTruthy();
  });


  it('should call getHealth on apiService when getHealth is called', () => {
    apiService.getHealth.and.returnValue(of({ status: 'healthy' }));
    component.getHealth();
    expect(apiService.getHealth).toHaveBeenCalled();
  });


  it('should call createOffer on apiService when createOffer is called', async () => {
    apiService.createOffer.and.returnValue(Promise.resolve({ id: '123' }));
    await component.createOffer();
    expect(apiService.createOffer).toHaveBeenCalled();
  });

});
