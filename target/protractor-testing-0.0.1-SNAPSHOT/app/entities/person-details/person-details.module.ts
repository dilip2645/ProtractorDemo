import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ProtractorTestingSharedModule, PanelModule, ExportModule, ModalModule, FormModule } from 'app/shared';
import { PersonDetailsListComponent, PersonDetailsUpdateComponent, personDetailsRoute } from './';
import { BreadcrumbModule } from 'app/layouts';
const ENTITY_STATES = [...personDetailsRoute];
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
  declarations: [PersonDetailsListComponent, PersonDetailsUpdateComponent],
  entryComponents: [PersonDetailsListComponent, PersonDetailsUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProtractorTestingPersonDetailsModule {}
