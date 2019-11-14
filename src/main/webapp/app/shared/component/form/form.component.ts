/**
 * @author Faizal Vasaya
 * @class  FormComponent
 * The FormComponent handles the forms with varied input types
 */
import {
  Component,
  OnInit,
  Input,
  ViewChild,
  Output,
  EventEmitter,
  ViewContainerRef,
  Injector,
  ComponentFactory,
  ComponentFactoryResolver,
  AfterContentInit
} from '@angular/core';
import { FormGroup } from '@angular/forms';

// --------------------------------------- //
// import { NumberInputComponent } from './number-input/number-input.component';
// import { StringInputComponent } from './string-input/string-input.component';
// import { DateInputComponent } from './date-input/date-input.component';
// import { SelectInputComponent } from './select-input/select-input.component';
// import { RadioInputComponent } from './radio-input/radio-input.component';
// import { CheckboxInputComponent } from './checkbox-input/checkbox-input.component';
import { Permission } from './form.model';
import { FormDescription, FormType, ButtonType } from './form.model';

@Component({
  selector: 'fhlbny-form',
  templateUrl: './form.component.html'
})
export class FormComponent implements OnInit, AfterContentInit {
  // An input from parent component stating the details of the form
  @Input()
  formDescription: FormDescription[];

  // A form group of the controls
  @Input()
  formGroup: FormGroup;

  @Input()
  formType: FormType;

  @Input()
  permission?: Permission;

  @Output()
  buttonClick = new EventEmitter<ButtonType>();

  // Instance of view container in the html.
  @ViewChild('viewContainer', { read: ViewContainerRef, static: false }) inputContainer: ViewContainerRef;

  constructor(private injector: Injector, private cfr: ComponentFactoryResolver) {}

  ngOnInit() {}

  ngAfterContentInit() {
    if (!this.permission) {
      this.permission = {
        deleteFunc: [],
        updateFunc: []
      };
    }
    /**
     * Invoekd dynamically component renderer for each control in the form description
     */
    this.formDescription.forEach(fd => {
      this.renderComponent(fd);
    });
  }

  /**
   * Dynamically renders the component in the view container, only if its isVisible propery is set to true
   * NOTE: Mention the components rendered dynamically in the component.module.ts's entry components
   */
  renderComponent(fd: FormDescription) {
    fd.isVisible = fd.isVisible !== undefined ? fd.isVisible : true;
    if (fd.isVisible) {
      let factory: ComponentFactory<any>;
      // if (fd.type === InputType.number) {
      //     factory = this.cfr.resolveComponentFactory(NumberInputComponent);
      // } else if (fd.type === InputType.string) {
      //     factory = this.cfr.resolveComponentFactory(StringInputComponent);
      // } else if (fd.type === InputType.date) {
      //     factory = this.cfr.resolveComponentFactory(DateInputComponent);
      // } else if (fd.type === InputType.select) {
      //     factory = this.cfr.resolveComponentFactory(SelectInputComponent);
      // } else if (fd.type === InputType.radio) {
      //     factory = this.cfr.resolveComponentFactory(RadioInputComponent);
      // } else if (fd.type = InputType.checkbox) {
      //     factory = this.cfr.resolveComponentFactory(CheckboxInputComponent);
      // }
      const componentRef = factory.create(this.injector);
      this.inputContainer.insert(componentRef.hostView);
      componentRef.instance.control = fd.control;
      componentRef.instance.label = fd.label;
      // Assign value for component only if it is present in the passed form description
      // if (fd.values) {
      //     componentRef.instance.data = fd.values;
      // }
      // Assisgn error message for component only if it is present in the passed form description
      if (fd.errorMessages) {
        componentRef.instance.errorMessages = fd.errorMessages;
      }
      // Assign radio properties for components only if it is present in the passed form description
      // if (fd.radio) {
      //     componentRef.instance.radioProperties = fd.radio;
      // }
      // Assign checkbox properties only if present in the passed form description
      // if (fd.checkboxProperty) {
      //     componentRef.instance.checkboxProperty = fd.checkboxProperty;
      // }
    }
  }

  /**
   * Emits events based on the type of button that was clicked
   */
  submitForm() {
    if (this.formGroup.valid) {
      if (this.formType === FormType.add) {
        this.buttonClick.emit(ButtonType.add);
      } else if (this.formType === FormType.update) {
        this.buttonClick.emit(ButtonType.update);
      }
    }
  }

  /**
   * Handles click of delete button
   */
  delete() {
    if (this.formGroup.valid) {
      this.buttonClick.emit(ButtonType.delete);
    }
  }

  /**
   * Handles click of cancel button
   */
  cancel() {
    this.buttonClick.emit(ButtonType.cancel);
  }
}
