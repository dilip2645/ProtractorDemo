/**
 * @author Faizal Vasaya
 * @description Clasess to maintain a common contract for fhlbny-form API
 */
import { AbstractControl } from '@angular/forms';
//-------------------------------//
// import { SelectInput } from "./select-input/select-input.model";
// import { CheckboxInput } from './checkbox-input/checkbox-input.model';
// import { RadioInput } from './radio-input/radio-input.model';

/**    [x: string]: any;

 * A contract for form description
 */
export class FormDescription {
  label: string;
  control: AbstractControl;
  // type: InputType;
  errorMessages?: ErrorMessage[];
  // values?: SelectInput[];
  isVisible?: boolean;
  // radio?: RadioInput[];
  // checkboxProperty?: CheckboxInput[];
}

/**
 * A contract to pass permissions for update and delete button
 */
export class Permission {
  updateFunc: string[];
  deleteFunc: string[];
}

/**
 * An enum to specify type of input that needs to be rendered
 */
// export enum InputType {
// string = 'string',
// number = 'number',
// date = 'date',
// select = 'select'
// radio = 'radio',
// checkbox = 'checkbox'
// }

/**
 * A contract to pass error messages to the individual input components
 */
export class ErrorMessage {
  error: string;
  message: string;
}

/**
 * A enum type that specifies the buttons that needs to be rendered based on the form type
 */
export enum FormType {
  add = 'add',
  update = 'update'
}

/**
 * An enum type to identify the type of button that was pressed by the user.
 */
export enum ButtonType {
  add = 'add',
  update = 'update',
  cancel = 'cancel',
  delete = 'delete'
}
