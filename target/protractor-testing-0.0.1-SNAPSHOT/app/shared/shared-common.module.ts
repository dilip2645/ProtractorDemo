import { NgModule } from '@angular/core';

import { ProtractorTestingSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
  imports: [ProtractorTestingSharedLibsModule],
  declarations: [JhiAlertComponent, JhiAlertErrorComponent],
  exports: [ProtractorTestingSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class ProtractorTestingSharedCommonModule {}
