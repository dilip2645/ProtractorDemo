import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ExportControlsComponent } from './export-controls.component';

@NgModule({
  imports: [CommonModule],
  declarations: [ExportControlsComponent],
  exports: [ExportControlsComponent]
})
export class ExportModule {}
