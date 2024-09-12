import { Component, forwardRef, ViewChild } from '@angular/core';
import { ApiService } from '../../../services/mgmt/api/api.service';
import { StatusMessageComponent } from '../../common-views/status-message/status-message.component';
import { HttpErrorResponse } from '@angular/common/http';
import { IOfferDetailsTO } from '../../../services/mgmt/api/backend';
import {
  AbstractControl,
  ControlValueAccessor,
  FormBuilder,
  FormControl,
  FormGroup,
  NG_VALUE_ACCESSOR, ValidationErrors,
  Validators
} from "@angular/forms";

interface SelectionFormModel {
  offerId: FormControl<string>;
}

@Component({
  selector: 'app-consume',
  templateUrl: './consume.component.html',
  styleUrl: './consume.component.scss',
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => ConsumeComponent),
    multi: true
  }]
})
export class ConsumeComponent implements ControlValueAccessor {
  @ViewChild('queryCatalogStatusMessage') private queryCatalogStatusMessage!: StatusMessageComponent;
  selectedOffer?: IOfferDetailsTO = undefined;
  selectionForm: FormGroup<SelectionFormModel>;
  selectedOfferId = '';

  private onChange = (offerId: string) => {};
  private onTouched = () => {};

  constructor(private apiService: ApiService, private fb: FormBuilder) {
    this.selectionForm = this.fb.group({
      offerId: this.fb.nonNullable.control<string>('', [Validators.required, this.validateOfferId])
    });
  }

  async selectOffer() {
    this.queryCatalogStatusMessage.showInfoMessage();
    this.apiService.selectContractOffer({
      offerId: this.selectedOfferId
    }).then(response => {
      console.log(response);
      this.queryCatalogStatusMessage.showSuccessMessage("Check console for details.", 20000);
      this.selectedOffer = response;
    }).catch((e: HttpErrorResponse) => {
      this.queryCatalogStatusMessage.showErrorMessage(e.error.detail, 20000);
    });
  }

  deselectOffer(): void {
    this.selectedOffer = undefined;
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  writeValue(offerId: string): void {
    this.selectedOfferId = offerId;
  }

  get isInvalidOfferId(): boolean {
    return (this.selectionForm.controls.offerId.value.length > 0)
      && this.selectionForm.controls.offerId.hasError('Wrong format');
  }

  validateOfferId(control: AbstractControl): ValidationErrors | null {
    if (control.value.match(/^[a-zA-Z0-9][a-zA-Z0-9\-]*$/)) {
      return null;
    }
    return {
      'Wrong format': true
    };
  }
}
