import { Component, EventEmitter, Input, Output } from '@angular/core';

import { PageData } from './page-data.model';

@Component({
  selector: 'fhlbny-pagination',
  templateUrl: './pagination.component.html'
})
export class PaginationComponent {
  @Input()
  pageData: PageData;

  @Output()
  onPageChange: EventEmitter<number> = new EventEmitter<number>();

  public pageChange(pageNum: number) {
    this.onPageChange.emit(pageNum);
  }

  getFirstItemIndex() {
    if (this.pageData.totalItems === 0) {
      return -1;
    }
    return this.pageData.currentPage * this.pageData.itemsPerPage;
  }

  getLastItemIndex() {
    return this.pageData.currentPage * this.pageData.itemsPerPage + this.pageData.numItemsOnCurrentPage - 1;
  }

  pagesRange() {
    // aot build issue without having type
    let currentPage: number = this.pageData.currentPage;
    let lastPage: number = this.pageData.totalPages;

    let startIndex: number = 0,
      endIndex: number = 0;

    if (currentPage === 0) {
      startIndex = currentPage;
    } else if (currentPage - 1 > 0) {
      startIndex = currentPage - 2;
    } else {
      startIndex = currentPage - 1;
    }

    if (currentPage === lastPage) {
      endIndex = currentPage;
    } else if (currentPage + 1 < lastPage) {
      endIndex = currentPage + 2;
    } else {
      endIndex = currentPage + 1;
    }

    return this.range(startIndex, endIndex + 1);
  }

  // Generate an exclusive range in the form [start..end-1]
  private range(start: number, end: number) {
    return Array.from({ length: end - start }, (x, k) => k + start);
  }
}
