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
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
