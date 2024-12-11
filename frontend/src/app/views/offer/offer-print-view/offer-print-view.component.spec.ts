import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfferPrintViewComponent } from './offer-print-view.component';
import {ModalModule} from "@coreui/angular";
import {provideAnimations} from "@angular/platform-browser/animations";

describe('OfferPrintViewComponent', () => {
  let component: OfferPrintViewComponent;
  let fixture: ComponentFixture<OfferPrintViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OfferPrintViewComponent],
      imports: [ModalModule],
      providers: [provideAnimations()]
    });
    fixture = TestBed.createComponent(OfferPrintViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
