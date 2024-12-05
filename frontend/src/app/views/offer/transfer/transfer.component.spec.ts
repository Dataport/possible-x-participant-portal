import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransferComponent } from './transfer.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {StatusMessageComponent} from "../../common-views/status-message/status-message.component";
import {Component, Input} from "@angular/core";

describe('TransferComponent', () => {
  let component: TransferComponent;
  let fixture: ComponentFixture<TransferComponent>;

  @Component({
    selector: 'app-status-message',
    template: ''
  })
  class MockStatusMessageComponent implements Partial<StatusMessageComponent>{
    @Input() successMessage: string;
    @Input() errorMessage: string;
    @Input() infoMessage: string;
    hideAllMessages() {}
    showInfoMessage() {}
    showSuccessMessage(msg: string) {}
    showErrorMessage(msg: string) {}
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TransferComponent, MockStatusMessageComponent],
      imports: [HttpClientTestingModule]
    });
    fixture = TestBed.createComponent(TransferComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
