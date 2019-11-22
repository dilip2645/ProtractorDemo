/**
 * @author Faizal Vasaya
 * @description A module to register spinner wide components/services.
 */
import { CommonModule } from '@angular/common';
import { NgModule, ModuleWithProviders } from '@angular/core';

// --------------------------------------- //
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { SpinnerComponent } from './spinner.component';
import { SpinnerService } from './spinner.service';

@NgModule({
  imports: [CommonModule, FontAwesomeModule],
  declarations: [SpinnerComponent],
  exports: [SpinnerComponent, FontAwesomeModule],
  entryComponents: []
})
export class SpinnerModule {
  /**
   * This static method is used to load all the services registered with core module.
   */
  static forRoot(): ModuleWithProviders {
    return {
      ngModule: SpinnerModule,
      providers: [SpinnerService]
    };
  }
}
