import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsumeComponent } from './consume.component';
import { AcceptOfferComponent } from '../accept/accept-offer.component';
import { IOfferDetailsTO } from '../../../services/mgmt/api/backend';
import { CommonViewsModule } from '../../common-views/common-views.module';
import { BadgeComponent, AccordionComponent, AccordionItemComponent } from '@coreui/angular';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('ConsumeComponent', () => {
  let component: ConsumeComponent;
  let fixture: ComponentFixture<ConsumeComponent>;

  const offerDetails = {
    offerId: 'dummy',
    offerType: 'dummy',
    creationDate: new Date(Date.now()),
    name: 'dummy',
    description: 'dummy',
    contentType: 'dummy'
  } as IOfferDetailsTO;

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      declarations: [ConsumeComponent, AcceptOfferComponent ],
      imports: [ CommonViewsModule , BadgeComponent, AccordionComponent, AccordionItemComponent, BrowserAnimationsModule ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConsumeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should select offer', () => {
    component.setSelectedOffer(offerDetails);

    expect(component.selectedOffer).toEqual(offerDetails);
  });
});
