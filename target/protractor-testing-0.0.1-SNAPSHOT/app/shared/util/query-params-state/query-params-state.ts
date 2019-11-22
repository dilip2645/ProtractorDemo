/**
 * @author Faizal Vasaya
 * @class  QueryParamsStateService
 * A state mangement service to manage query params for list screen. This service should not be injected but used as a static method provider.
 * It stores the following in query params:
 * 1. Page number.
 * 2. Sort state.
 * 3. Search string.
 * 4. Entries per page.
 */
import { Location } from '@angular/common';
import { Params, ActivatedRoute, Router } from '@angular/router';
//-----------------------------//
import { StateParam } from './state-param.model';
// import {  SortConfig } from '@fhlbny-ui-commons/components';
import { PageData } from 'app/shared';

export class QueryParamsStateService {
  /**
   * A static method to convert object to query parmas and sets it in the current url.
   */
  public static setQueryParams(activatedRoute: ActivatedRoute, router: Router, pageData: PageData, searchObject?: any) {
    const stateParams: StateParam = {
      itemsPerPage: pageData.itemsPerPage,
      page: pageData.currentPage
    };

    if (searchObject !== undefined) {
      Object.keys(searchObject).forEach((key, index, array) => {
        // Since the filtering component encodes uri component, hence it needs to be decoded since angular reencodes uri component when setting as query params.
        stateParams[key] = decodeURIComponent(searchObject[key]);
      });
    }
    router.navigate([], { relativeTo: activatedRoute, queryParams: { ...stateParams } });
  }
}
