import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPersonDetails } from 'app/shared/model/person-details.model';

type EntityResponseType = HttpResponse<IPersonDetails>;
type EntityArrayResponseType = HttpResponse<IPersonDetails[]>;

@Injectable({ providedIn: 'root' })
export class PersonDetailsService {
  public resourceUrl = SERVER_API_URL + 'api/person-details';

  constructor(protected http: HttpClient) {}

  create(personDetails: IPersonDetails): Observable<EntityResponseType> {
    return this.http.post<IPersonDetails>(this.resourceUrl, personDetails, { observe: 'response' });
  }

  update(personDetails: IPersonDetails): Observable<EntityResponseType> {
    return this.http.put<IPersonDetails>(this.resourceUrl, personDetails, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPersonDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPersonDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  exportReport(type: string, req?: any): Observable<any> {
    return Observable.create(observer => {
      const options = createRequestOption(req);
      this.http
        .get(this.resourceUrl + '?exportType=' + type.toLowerCase(), {
          params: options,
          observe: 'response',
          responseType: 'blob'
        })
        .subscribe(
          response => {
            observer.next(response);
            observer.complete();
          },
          error => {
            observer.error(error);
            observer.complete();
          }
        );
    });
  }
}
