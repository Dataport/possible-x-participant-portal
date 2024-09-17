import { Component, EventEmitter, forwardRef, Output, ViewChild } from '@angular/core';
import { HttpErrorResponse } from "@angular/common/http";
import {
  AbstractControl,
  ControlValueAccessor,
  FormBuilder,
  FormControl,
  FormGroup,
  NG_VALUE_ACCESSOR, ValidationErrors,
  Validators
} from "@angular/forms";
import { StatusMessageComponent } from "../../common-views/status-message/status-message.component";
import { ApiService } from "../../../services/mgmt/api/api.service";
import { IOfferDetailsTO } from "../../../services/mgmt/api/backend";

interface SelectionFormModel {
  offerId: FormControl<string>;
}

@Component({
  selector: 'app-select-offer',
  templateUrl: './select-offer.component.html',
  styleUrl: './select-offer.component.scss',
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => SelectOfferComponent),
    multi: true
  }]
})
export class SelectOfferComponent implements ControlValueAccessor {
  @ViewChild('queryCatalogStatusMessage') private queryCatalogStatusMessage!: StatusMessageComponent;
  selectionForm: FormGroup<SelectionFormModel>;
  selectedOfferId = '';
  @Output() selectedOffer = new EventEmitter<IOfferDetailsTO>();

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
      fhCatalogOfferId: this.selectionForm.controls.offerId.value
    }).then(response => {
      console.log(response);
      this.queryCatalogStatusMessage.showSuccessMessage("Check console for details.", 20000);
      this.selectedOffer.emit(response);
    }).catch((e: HttpErrorResponse) => {
      this.queryCatalogStatusMessage.showErrorMessage(e.error.detail, 20000);
    });
  }

  registerOnChange(fn: (offerId: string) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
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
