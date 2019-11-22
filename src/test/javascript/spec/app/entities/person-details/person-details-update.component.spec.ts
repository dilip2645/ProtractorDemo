/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { ProtractorTestingTestModule } from '../../../test.module';
import { PersonDetailsUpdateComponent } from 'app/entities/person-details/person-details-update.component';
import { PersonDetailsService } from 'app/entities/person-details/person-details.service';
import { PersonDetails } from 'app/shared/model/person-details.model';

describe('Component Tests', () => {
  describe('PersonDetails Management Update Component', () => {
    let comp: PersonDetailsUpdateComponent;
    let fixture: ComponentFixture<PersonDetailsUpdateComponent>;
    let service: PersonDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ProtractorTestingTestModule],
        declarations: [PersonDetailsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PersonDetailsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonDetailsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PersonDetailsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PersonDetails(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new PersonDetails();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
