/**
 * @author Faizal Vasaya
 * A utility class that provides method to unsbsribe the observables registered by a component.
 */
// import { Observable } from 'rxjs/Observable';
import { takeUntil } from 'rxjs/operators';
import { Observable, ReplaySubject } from 'rxjs';
// import { ReplaySubject } from 'rxjs/ReplaySubject';

// Makes sure that the component to be destroyed had an ngOnDestroy registered.
export interface OnDestroyLike {
  ngOnDestroy(): void;
  [key: string]: any;
}

export class ObservableUnsubscriptionUtilities {
  /**
   * A method to apply new ngDestroy method to the component.
   * @param component The component whose subscribers needs to be unsubscribed on components destruction
   */
  static componentDestroyed(component: OnDestroyLike): Observable<true> {
    if (component.__componentDestroyed$) {
      return component.__componentDestroyed$;
    }
    const oldNgOnDestroy = component.ngOnDestroy;
    const stop$ = new ReplaySubject<true>();
    component.ngOnDestroy = () => {
      const a = oldNgOnDestroy && oldNgOnDestroy.apply(component);
      stop$.next(true);
      stop$.complete();
    };
    return (component.__componentDestroyed$ = stop$.asObservable());
  }

  /**
   * A method hat waits untill the component is destroyed. Once destroyed, it calls ObservableUnsubscriptionUtilities.componentDestroyed()
   * @param component The component whose subscribers needs to be unsubscribed on components destruction
   */
  static untilComponentDestroyed<T>(component: OnDestroyLike): (source: Observable<T>) => Observable<T> {
    return (source: Observable<T>) => source.pipe(takeUntil(ObservableUnsubscriptionUtilities.componentDestroyed(component)));
  }
}
