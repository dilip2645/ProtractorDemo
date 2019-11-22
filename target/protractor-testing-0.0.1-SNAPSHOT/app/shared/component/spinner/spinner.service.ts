/**
 * @author Faizal Vasaya
 * This service consists of methods to change the current status of the spinner
 */
import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { Observable } from 'rxjs';
//---------------------------//

@Injectable()
export class SpinnerService {
  public subject: Subject<boolean> = new Subject<boolean>();

  constructor() {}

  /**
   * Return the spinner subject as an observable
   */
  getSpinner(): Observable<any> {
    return this.subject.asObservable();
  }

  /**
   * Inoke this method to hide/show spinner based on boolean
   * True -> Show spinner
   * False -> Hide spinner
   */
  spin(isShown: boolean) {
    this.subject.next(isShown);
  }
}
