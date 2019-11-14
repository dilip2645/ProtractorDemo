import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormControl } from '@angular/forms';

@Component({
  selector: 'fhlbny-page-size-selector',
  templateUrl: './page-size-selector.component.html'
})
export class PageSizeSelectorComponent implements OnInit {
  @Input()
  currentPageSize: number;

  @Output()
  onPageSizeChange: EventEmitter<number> = new EventEmitter<number>();

  availablePageSizes: number[] = [10, 20, 30, 40, 50];

  pageSizeSelectionForm: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit() {
    this.createForm();
    this.pageSizeSelectionForm.get('pageSizeSelector').valueChanges.subscribe(pageSize => {
      this.pageSizeChange(pageSize);
    });
  }

  createForm() {
    this.pageSizeSelectionForm = this.fb.group({
      pageSizeSelector: [this.currentPageSize]
    });
  }

  compareFn(c1: number, c2: number): boolean {
    return c1 === c2;
  }

  pageSizeChange(pageSize: number) {
    this.onPageSizeChange.emit(pageSize);
  }
}
