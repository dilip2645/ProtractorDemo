import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICourse } from 'app/shared/model/course.model';

type EntityResponseType = HttpResponse<ICourse>;
type EntityArrayResponseType = HttpResponse<ICourse[]>;

@Injectable({ providedIn: 'root' })
export class CourseService {
  public resourceUrl = SERVER_API_URL + 'api/courses';

  constructor(protected http: HttpClient) {}

  create(course: ICourse): Observable<EntityResponseType> {
    return this.http.post<ICourse>(this.resourceUrl, course, { observe: 'response' });
  }

  update(course: ICourse): Observable<EntityResponseType> {
    return this.http.put<ICourse>(this.resourceUrl, course, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICourse>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICourse[]>(this.resourceUrl, { params: options, observe: 'response' });
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
