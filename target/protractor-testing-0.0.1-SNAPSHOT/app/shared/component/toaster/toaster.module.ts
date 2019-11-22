/**
 * @author Faizal Vasaya
 * @description A module to register Toaster wide components/services.
 */
import { CommonModule, TitleCasePipe } from '@angular/common';
import { NgModule, ModuleWithProviders } from '@angular/core';
import { NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';

// --------------------------------------- //

import { ToasterComponent } from './toaster.component';
import { ToasterService } from './toaster.service';

@NgModule({
  imports: [CommonModule, NgbAlertModule.forRoot()],
  declarations: [ToasterComponent],
  exports: [ToasterComponent],
  entryComponents: []
})
export class ToasterModule {
  /**
   * This static method is used to load all the services registered with core module.
   */
  static forRoot(): ModuleWithProviders {
    return {
      ngModule: ToasterModule,
      providers: [ToasterService, TitleCasePipe]
    };
  }
}
