import { Component, EventEmitter, Output, Input, OnInit } from '@angular/core';
import { ExportType } from './export-type';
import { ExportFormat } from './export-format.model';

@Component({
  selector: 'fhlbny-export-controls',
  templateUrl: './export-controls.component.html'
})
export class ExportControlsComponent implements OnInit {
  @Input()
  exportOptions: ExportFormat;

  @Output()
  onExport: EventEmitter<ExportType> = new EventEmitter<ExportType>();

  public availableExportTypes: any;

  constructor() {
    this.availableExportTypes = ExportType;
  }

  ngOnInit() {
    if (this.exportOptions === undefined) {
      this.exportOptions = { csv: true, pdf: true, xls: true };
    }
  }

  export(exportType: ExportType) {
    this.onExport.emit(exportType);
  }
}
