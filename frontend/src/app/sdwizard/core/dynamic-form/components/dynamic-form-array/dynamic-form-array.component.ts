import {Component, Input, OnInit} from '@angular/core';
import {FormArray, FormControl, FormGroup, ValidatorFn} from '@angular/forms';
import {FormField} from '@models/form-field.model';
import {Shape} from '@models/shape';
import {ValidationControlService} from '@services/validation.service';
import {Utils} from '@shared/utils';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-dynamic-form-array',
  templateUrl: './dynamic-form-array.component.html',
  styleUrls: ['./dynamic-form-array.component.scss']
})
export class DynamicFormArrayComponent implements OnInit {

  @Input() input: FormField = new FormField();
  @Input() form: FormGroup = new FormGroup({});
  @Input() shapes: Shape[] = [];
  inputs: FormArray = new FormArray([]);
  enableButton = true;
  displayAddButton = true;
  validator: ValidatorFn[];
  validationService: ValidationControlService = new ValidationControlService();

  serviceOfferingPolicyTooltip: string = 'Below you have the option of entering a policy for the use and management of this service. \n' +
    'The policy should also include the restrictions set out in the predefined policy class. \n' +
    '\n' +
    'This policy is neither technically enforced nor is its compliance monitored. However, the user of the service undertakes to comply with the guidelines listed here.\n' +
    '\n' +
    'Please take the following points into account:\n' +
    '\n' +
    '1. Access rights: Who is allowed to use the data transfer service? (e.g. only certain departments or roles)\n' +
    '\n' +
    '2. Terms of use: Under what conditions may data be transferred? (e.g. only encrypted transfers, no transfer of sensitive data without authorization)\n' +
    '\n' +
    '3. Security measures: What security precautions need to be taken? (e.g. use of VPNs, two-factor authentication)\n' +
    '\n' +
    '4. Logging and monitoring: How is the use of the service logged and monitored? (e.g. regular review of transmission logs)\n' +
    '\n' +
    '5. Data integrity: What measures are taken to ensure the integrity of the transmitted data? (e.g. checksums, error correction procedures)\n' +
    '\n' +
    '6. Responsibilities: Who is responsible for managing and monitoring compliance with these policies? (e.g. IT department, security officer)';
  dataResourcePolicyTooltip: string = 'Below you have the option of entering a policy for the use and management of your data. \n' +
    '\n' +
    'This policy is neither technically enforced nor is compliance checked. However, the user of your data undertakes to comply with the guidelines set out here.\n' +
    '\n' +
    'The policy for the data transfer service and the technically enforced policy can be entered in the following fields.\n' +
    '\n' +
    'Take the following points into account:\n' +
    '\n' +
    '1. Access rights: Who is allowed to access this data resource? (e.g. only certain departments or roles)\n' +
    '\n' +
    '2. Terms of use: How may the data be used? (e.g. only for internal analyses, not to be passed on to third parties)\n' +
    '\n' +
    '3. Security measures: What security precautions need to be taken? (e.g. encryption, regular security checks)\n' +
    '\n' +
    '4. Data retention: How long should the data be retained and when should it be deleted? (e.g. retention for 5 years, then secure deletion)\n' +
    '\n' +
    '5. Responsibilities: Who is responsible for managing and monitoring compliance with these policies? (e.g. IT department, data protection officer)';


  constructor(public translate: TranslateService) {
  }

  ngOnInit(): void {
    this.inputs = this.form.get(this.input.id) as FormArray;
    this.validator = this.validationService.getValidatorFn(this.input);
    // In the for loop we specify the limit (this.input.minCount-1) because the FormArray comes with a FormControl added by the service
    for (let i = 0; i < this.input.minCount - 1; i++) {
      // The form array has only a single FormControl
      this.inputs.push(new FormControl(this.input.value || null, this.validator));
    }

    this.displayAddButton = this.input.maxCount === undefined || this.input.maxCount > this.input.minCount;
  }

  addInput(): void {
    if (this.input.required) {
      // Remove the required validator from the new added inputs
      this.validator.pop();
    }
    this.inputs.push(new FormControl(this.input.value || '', this.validator));
    this.updateEnableButton();
  }


  deleteInput(index: number): void {
    if (this.inputs.length !== 1) {
      this.inputs.removeAt(index);
    }
    this.updateEnableButton();
  }

  updateEnableButton(): void {
    const maxCount = this.input.maxCount;
    if (maxCount !== undefined) {
      this.enableButton = this.inputs.length < maxCount;
    }
  }

  addFullWidthClass(i): boolean {
    return !this.displayAddButton || (i !== 0 && i < this.input.minCount);
  }

  addNonFullWidthClass(i): boolean {
    return this.displayAddButton && (i === 0 || this.input.minCount === 1
      || this.input.maxCount === 0 || i >= this.input.minCount);
  }

  hasRequiredValidator(control: FormControl): boolean {
    return Utils.hasRequiredField(control);
  }

  getTooltip(name: string): string | null {
    if (name === 'Service Offering Policy') {
      return this.serviceOfferingPolicyTooltip;
    } else if (name === 'Data Resource Policy') {
      return this.dataResourcePolicyTooltip;
    }
    return null;
  }
}
