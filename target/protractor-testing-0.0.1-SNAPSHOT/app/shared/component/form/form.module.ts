/**
 * @author Faizal Vasaya
 * @description A module to register form wide components/services.
 */
import { CommonModule, DatePipe } from '@angular/common';
import { NgModule, ModuleWithProviders } from '@angular/core';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NgbDatepickerModule } from '@ng-bootstrap/ng-bootstrap';
// --------------------------------------- //
// import { CoreModule } from '@fhlbny-ui-commons/core';
// import { NumberInputComponent } from './number-input/number-input.component';
// import { StringInputComponent } from './string-input/string-input.component';
import { FormComponent } from './form.component';
// import { DateInputComponent } from './date-input/date-input.component';
// import { SelectInputComponent } from './select-input/select-input.component';
// import { RadioInputComponent } from './radio-input/radio-input.component';
// import { CheckboxInputComponent } from './checkbox-input/checkbox-input.component';

// --------------------------------------- //

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    NgbDatepickerModule.forRoot()
    // CoreModule
  ],
  declarations: [
    // NumberInputComponent,
    // StringInputComponent,
    // DateInputComponent,
    // SelectInputComponent,
    FormComponent
    // RadioInputComponent,
    // CheckboxInputComponent
  ],
  exports: [
    // NumberInputComponent,
    // StringInputComponent,
    // DateInputComponent,
    // SelectInputComponent,
    // RadioInputComponent,
    FormComponent
    // CheckboxInputComponent
  ],
  entryComponents: [
    // NumberInputComponent,
    // StringInputComponent,
    // DateInputComponent,
    // SelectInputComponent,
    // RadioInputComponent,
    // CheckboxInputComponent
  ]
})
export class FormModule {
  /**
   * This static method is used to load all the services registered with core module.
   */
  static forRoot(): ModuleWithProviders {
    return {
      ngModule: FormModule,
      providers: [DatePipe]
    };
  }
}
