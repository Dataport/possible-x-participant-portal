import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServiceOfferPrintViewComponent } from './service-offer-print-view.component';

describe('ServiceOfferPrintViewComponent', () => {
  let component: ServiceOfferPrintViewComponent;
  let fixture: ComponentFixture<ServiceOfferPrintViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ServiceOfferPrintViewComponent]
    });
    fixture = TestBed.createComponent(ServiceOfferPrintViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
