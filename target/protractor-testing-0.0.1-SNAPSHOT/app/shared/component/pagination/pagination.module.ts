import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PaginationComponent, PageSizeSelectorComponent } from './';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  declarations: [PageSizeSelectorComponent, PaginationComponent],
  exports: [PageSizeSelectorComponent, PaginationComponent]
})
export class PaginationModule {}
