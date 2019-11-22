/**
 * @author Faizal Vasaya
 * @class  SpinnerComponent
 * The Spinnercomponent handles the logic for spinner
 */
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';

// --------------------------------------- //
import { SpinnerService } from './spinner.service';

@Component({
  selector: 'fhlbny-spinner',
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.scss']
})
export class SpinnerComponent implements OnInit {
  public isShown: boolean;

  constructor(private spinnerService: SpinnerService, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    /**
     * Subscribes to getSpinner to get the current status of the spinner
     */
    this.spinnerService.getSpinner().subscribe((isShown: boolean) => {
      this.isShown = isShown;
      this.cdr.detectChanges();
    });
  }
}
