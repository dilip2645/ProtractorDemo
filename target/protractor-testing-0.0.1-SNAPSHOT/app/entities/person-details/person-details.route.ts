import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PersonDetails } from 'app/shared/model/person-details.model';
import { PersonDetailsService } from './person-details.service';
import { PersonDetailsListComponent } from './person-details-list.component';
import { PersonDetailsUpdateComponent } from './person-details-update.component';
import { IPersonDetails } from 'app/shared/model/person-details.model';

@Injectable({ providedIn: 'root' })
export class PersonDetailsResolve implements Resolve<IPersonDetails> {
  constructor(private service: PersonDetailsService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPersonDetails> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<PersonDetails>) => response.ok),
        map((personDetails: HttpResponse<PersonDetails>) => personDetails.body)
      );
    }
    return of(new PersonDetails());
  }
}

export const personDetailsRoute: Routes = [
  {
    path: '',
    component: PersonDetailsListComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'PersonDetails'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'person-details-new',
    component: PersonDetailsUpdateComponent,
    resolve: {
      personDetails: PersonDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PersonDetails'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PersonDetailsUpdateComponent,
    resolve: {
      personDetails: PersonDetailsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PersonDetails'
    },
    canActivate: [UserRouteAccessService]
  }
];
