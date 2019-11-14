import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';
import { Router } from '@angular/router';
import { breadcrumbFileds } from './breadcrumb.model';

@Component({
  selector: 'fhlbny-breadcrumb',
  templateUrl: './breadcrumb.component.html'
})
export class BreadcrumbComponent implements OnInit {
  @Output() refreshCurrentRoute: EventEmitter<object> = new EventEmitter<object>();

  @Input() breadcrumbData: breadcrumbFileds;

  constructor(private router: Router) {
    this.breadcrumbData = {};
  }

  ngOnInit() {}

  routeTo() {
    {
      if (this.breadcrumbData.entityUrl) {
        this.router.navigate([this.breadcrumbData.entityUrl]);
      }
    }
  }

  refresh() {
    this.refreshCurrentRoute.emit();
  }
}
