import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfferDetailsViewComponent } from './offer-details-view.component';

describe('OfferDetailsViewComponent', () => {
  let component: OfferDetailsViewComponent;
  let fixture: ComponentFixture<OfferDetailsViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OfferDetailsViewComponent]
    });
    fixture = TestBed.createComponent(OfferDetailsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
