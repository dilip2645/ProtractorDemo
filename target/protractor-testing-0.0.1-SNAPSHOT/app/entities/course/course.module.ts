import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ProtractorTestingSharedModule, PanelModule, ExportModule, ModalModule, FormModule } from 'app/shared';
import { CourseListComponent, CourseUpdateComponent, courseRoute } from './';
import { BreadcrumbModule } from 'app/layouts';
const ENTITY_STATES = [...courseRoute];
@NgModule({
  imports: [
    ProtractorTestingSharedModule,
    RouterModule.forChild(ENTITY_STATES),
    PanelModule,
    ExportModule,
    ModalModule,
    FormModule,
    BreadcrumbModule
  ],
  declarations: [CourseListComponent, CourseUpdateComponent],
  entryComponents: [CourseListComponent, CourseUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProtractorTestingCourseModule {}
