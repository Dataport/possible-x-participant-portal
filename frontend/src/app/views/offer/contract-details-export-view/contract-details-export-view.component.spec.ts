import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContractDetailsExportViewComponent } from './contract-details-export-view.component';
import {ModalModule} from "@coreui/angular";
import {provideAnimations} from "@angular/platform-browser/animations";

describe('ContractDetailsExportViewComponent', () => {
  let component: ContractDetailsExportViewComponent;
  let fixture: ComponentFixture<ContractDetailsExportViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ContractDetailsExportViewComponent],
      imports: [ModalModule],
      providers: [provideAnimations()]
    });
    fixture = TestBed.createComponent(ContractDetailsExportViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
