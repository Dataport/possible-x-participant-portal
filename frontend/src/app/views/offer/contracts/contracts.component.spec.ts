// import { ComponentFixture, TestBed } from '@angular/core/testing';
//
// import { ConsumeComponent } from './contracts.component';
// import { IOfferDetailsTO } from '../../../services/mgmt/api/backend';
// import { NO_ERRORS_SCHEMA } from "@angular/core";
//
// describe('ConsumeComponent', () => {
//   let component: ConsumeComponent;
//   let fixture: ComponentFixture<ConsumeComponent>;
//
//   const offerDetails = {
//     edcOfferId: 'dummy',
//     counterPartyAddress: 'dummy',
//     offerType: 'dummy',
//     creationDate: new Date(Date.now()),
//     name: 'dummy',
//     description: 'dummy',
//     contentType: 'dummy'
//   } as IOfferDetailsTO;
//
//   beforeEach(async () => {
//
//     await TestBed.configureTestingModule({
//       declarations: [ ConsumeComponent ],
//       schemas: [ NO_ERRORS_SCHEMA ],
//     })
//     .compileComponents();
//
//     fixture = TestBed.createComponent(ConsumeComponent);
//     component = fixture.componentInstance;
//     fixture.detectChanges();
//   });
//
//   it('should create', () => {
//     expect(component).toBeTruthy();
//   });
//
//   it('should select offer', () => {
//     component.setSelectedOffer(offerDetails);
//
//     expect(component.selectedOffer).toEqual(offerDetails);
//   });
// });
