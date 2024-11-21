import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfferPrintViewComponent } from './offer-print-view.component';

describe('OfferPrintViewComponent', () => {
  let component: OfferPrintViewComponent;
  let fixture: ComponentFixture<OfferPrintViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OfferPrintViewComponent]
    });
    fixture = TestBed.createComponent(OfferPrintViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
