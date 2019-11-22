/**
 * @author Faizal Vasaya
 * @class  ToasterComponent
 * The Toaster component handles the logic for toaster
 */
import { Component, OnInit, NgZone, ChangeDetectorRef } from '@angular/core';

// --------------------------------------- //
import { Toaster, ToasterType } from './toaster.model';
import { ToasterService } from './toaster.service';
import { toasterAnimation } from './toaster.animation';

@Component({
  selector: 'fhlbny-toaster',
  templateUrl: './toaster.component.html',
  styleUrls: ['./toaster.component.scss'],
  animations: [toasterAnimation]
})
export class ToasterComponent implements OnInit {
  // An array of toasts to store toaster objects
  public toasts: Toaster[];
  // A map of toast and its timeout ids
  public timeOutIds: Map<number, number> = new Map<number, number>();
  // Auto increment incremnet of toaster id;
  public toastId: number;

  constructor(private toasterService: ToasterService, private ngZone: NgZone, private cdr: ChangeDetectorRef) {
    this.toastId = 0;
    this.toasts = [];
  }

  ngOnInit() {
    /**
     * Subscribe to the getToast to get the newly added toasts, incremnets and assigns the toast id and invokes the configure timeout method
     */
    this.toasterService.getToast().subscribe((toast: Toaster) => {
      if (!toast) {
        this.toasts = [];
        return;
      }
      this.toastId++;
      toast.id = this.toastId;
      toast.state = 'in'; //Make toggle state
      this.toasts.push(toast);
      this.configureTimeout(toast);
    });
  }

  /**
   * A method to configure timeout for each toaster so that their timeouts won't run in Angular mode. It calls dismiss toast after 5 seconds of creating toaster
   * @param toast A toast for which the timeout needs to be configured
   */
  configureTimeout(toast: Toaster) {
    this.ngZone.runOutsideAngular(() => {
      const timeoutId = window.setTimeout(() => {
        this.ngZone.run(() => {
          this.cdr.markForCheck();
          this.dismissToast(toast);
        });
      }, 5000);
      const toastId = toast.id;
      if (toastId) {
        this.timeOutIds.set(toastId, timeoutId);
      }
    });
  }

  /**
   * A method to remove toaster from toaster array. It also clears the timer associated with that toaster.
   * @param toast The toaster to be removed from the toaster array
   */
  dismissToast(toast: Toaster) {
    toast.state = 'out';
    const toastId = toast.id;
    if (toastId) {
      this.toasts = this.toasts.filter(tst => tst.id != toastId);
      this.timeOutIds.delete(toastId);
    }
  }
}
