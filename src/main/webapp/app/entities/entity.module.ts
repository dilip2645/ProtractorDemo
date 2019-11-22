import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'person-details',
        loadChildren: () => import('./person-details/person-details.module').then(m => m.ProtractorTestingPersonDetailsModule)
      },
      {
        path: 'course',
        loadChildren: () => import('./course/course.module').then(m => m.ProtractorTestingCourseModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProtractorTestingEntityModule {}
