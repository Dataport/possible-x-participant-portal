import {Component, EventEmitter, forwardRef, Output, ViewChild} from '@angular/core';
import {HttpErrorResponse} from "@angular/common/http";
import {
  AbstractControl,
  ControlValueAccessor,
  FormBuilder,
  FormControl,
  FormGroup,
  NG_VALUE_ACCESSOR,
  ValidationErrors,
  Validators
} from "@angular/forms";
import {StatusMessageComponent} from "../../common-views/status-message/status-message.component";
import {ApiService} from "../../../services/mgmt/api/api.service";
import {IOfferDetailsTO} from "../../../services/mgmt/api/backend";
import {commonMessages} from "../../../../environments/common-messages";

interface SelectionFormModel {
  offerId: FormControl<string>;
}

@Component({
  selector: 'app-select-offer',
  templateUrl: './select.component.html',
  styleUrls: ['./select.component.scss'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => SelectComponent),
    multi: true
  }]
})
export class SelectComponent implements ControlValueAccessor {
  selectionForm: FormGroup<SelectionFormModel>;
  @Output() selectedOffer = new EventEmitter<IOfferDetailsTO>();
  @ViewChild('queryCatalogStatusMessage') queryCatalogStatusMessage!: StatusMessageComponent;

  constructor(private readonly apiService: ApiService, private readonly fb: FormBuilder) {
    this.selectionForm = this.fb.group({
      offerId: this.fb.nonNullable.control<string>('', [Validators.required, this.validateOfferId])
    });
  }

  get isInvalidOfferId(): boolean {
    return (this.selectionForm.controls.offerId.value.length > 0)
      && this.selectionForm.controls.offerId.hasError('Wrong format');
  }

  async selectOffer(offerId: string) {
    this.queryCatalogStatusMessage.showInfoMessage();
    this.apiService.selectContractOffer({
      fhCatalogOfferId: offerId
    }).then(response => {
      console.log(response);
      this.queryCatalogStatusMessage.showSuccessMessage("Check console for details.");
      this.selectedOffer.emit(response);
    }).catch((e: HttpErrorResponse) => {
      console.log(e);
      if (e.status === 500) {
        this.queryCatalogStatusMessage.showErrorMessage(commonMessages.general_error);
      } else if (e.status == 404) {
        this.queryCatalogStatusMessage.showErrorMessage(commonMessages.offer_not_found);
      } else {
        this.queryCatalogStatusMessage.showErrorMessage(e.error.details);
      }
    }).catch(e => {
      console.log(e);
      this.queryCatalogStatusMessage.showErrorMessage(commonMessages.general_error);
    });
  }

  registerOnChange(fn: (value: Partial<{ offerId: string; }>) => void): void {
    this.selectionForm.valueChanges.subscribe(fn);
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  writeValue(offerId: string): void {
    offerId ? this.selectionForm.controls.offerId.setValue(offerId) : this.selectionForm.reset();
  }

  validateOfferId(control: AbstractControl): ValidationErrors | null {
    if (control.value.match(/^[a-zA-Z0-9][a-zA-Z0-9\-:]*$/)) {
      return null;
    }
    return {
      'Wrong format': true
    };
  }

  private readonly onChange = (offerId: string) => {
  };

  private onTouched = () => {
  };
}
