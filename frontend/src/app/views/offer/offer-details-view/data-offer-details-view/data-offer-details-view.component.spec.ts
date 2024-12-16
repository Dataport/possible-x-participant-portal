import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DataOfferDetailsViewComponent } from './data-offer-details-view.component';

describe('DataOfferDetailsViewComponent', () => {
  let component: DataOfferDetailsViewComponent;
  let fixture: ComponentFixture<DataOfferDetailsViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DataOfferDetailsViewComponent]
    });
    fixture = TestBed.createComponent(DataOfferDetailsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
