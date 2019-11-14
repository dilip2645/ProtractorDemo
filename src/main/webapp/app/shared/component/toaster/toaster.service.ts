/**
 * @author Faizal Vasaya
 * This service consists of methods to change the current status of the toaster
 */
import { Injectable } from '@angular/core';
//import { Subject } from 'rxjs/Subject';
import { Router, NavigationStart } from '@angular/router';
//import { Observable } from 'rxjs/Observable';
//---------------------------//
import { Toaster } from './toaster.model';
import { Subject } from 'rxjs/internal/Subject';
import { Observable } from 'rxjs';

@Injectable()
export class ToasterService {
  public subject: Subject<Toaster> = new Subject<Toaster>();
  public keepAfterRouteChange = true;

  constructor(private router: Router) {
    // clear alert messages on route change unless 'keepAfterRouteChange' flag is true
    router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        if (!this.keepAfterRouteChange) {
          // clear alert messages
          this.clear();
        }
      }
    });
  }

  /**
   * Subscribe to this service to get an alert
   */
  getToast(): Observable<any> {
    return this.subject.asObservable();
  }

  /**
   * Clears all the alerts
   */
  clear() {
    this.subject.next();
  }

  /**
   * Adds alert to existing alert
   */
  toast(toast: Toaster) {
    this.subject.next(toast);
  }
}
