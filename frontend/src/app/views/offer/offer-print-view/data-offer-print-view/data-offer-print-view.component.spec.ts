import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DataOfferPrintViewComponent } from './data-offer-print-view.component';

describe('DataOfferPrintViewComponent', () => {
  let component: DataOfferPrintViewComponent;
  let fixture: ComponentFixture<DataOfferPrintViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DataOfferPrintViewComponent]
    });
    fixture = TestBed.createComponent(DataOfferPrintViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
