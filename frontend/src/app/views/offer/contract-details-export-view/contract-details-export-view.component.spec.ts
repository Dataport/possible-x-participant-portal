import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContractDetailsExportViewComponent } from './contract-details-export-view.component';

describe('ContractDetailsExportViewComponent', () => {
  let component: ContractDetailsExportViewComponent;
  let fixture: ComponentFixture<ContractDetailsExportViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ContractDetailsExportViewComponent]
    });
    fixture = TestBed.createComponent(ContractDetailsExportViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
