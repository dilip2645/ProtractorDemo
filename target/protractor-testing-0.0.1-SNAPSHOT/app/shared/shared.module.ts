import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ProtractorTestingSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [ProtractorTestingSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [ProtractorTestingSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProtractorTestingSharedModule {
  static forRoot() {
    return {
      ngModule: ProtractorTestingSharedModule
    };
  }
}
